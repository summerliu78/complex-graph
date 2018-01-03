package com.yinker.tinyv.controller;

import com.alibaba.fastjson.JSON;
import com.yinker.tinyv.service.GangMember3rdPartyInfoService;
import com.yinker.tinyv.service.GangNumberService;
import com.yinker.tinyv.vo.EChartJsonJavaV2;
import com.yinker.tinyv.vo.EChartJsonJavaV2.Node;
import com.yinker.tinyv.vo.EmailReportVO;
import com.yinker.tinyv.vo.GangMember;
import com.yinker.tinyv.vo.GangMember3rdPartyInfo;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * GangNumber resource
 * @author Zhu Xiuwei
 */
@Path("/gangs")
public class GangNumberController {
	private Logger LOG = Logger.getLogger(GangNumberController.class);
	private final String NOTFOUND = "NOT_FOUND";
	private GangNumberService gmService = new GangNumberService();
	private GangMember3rdPartyInfoService gm3rdService = new GangMember3rdPartyInfoService();

	/**
	 * 给定一个电话号码，查找团伙成员通话情况。
	 * @param numberString
	 * @return 电话号码所在的团伙成员童话情况。
	 */
    @GET
    @Path("number/{number}")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByNumber(@PathParam("number") String numberString) {
    	System.out.println(new Date() + ": getByNumber Requests coming in: " + numberString);
    	LOG.info(new Date() + ": getByNumber Requests coming in: " + numberString);
    	GangMember member = null;
    	try {
    		member = gmService.getByNumber(numberString);
    		System.out.println("member: " + member);
    		LOG.info("member: " + member);
		} catch (Exception e) {
			return getOkResponse("ERROR", "Graphx-webapp server internal error: " + e.getMessage());
		}
    	System.out.println(new Date() + ": Query HBase done!");
		LOG.info(new Date() + ": Query HBase done!");
    	if(member == null)	//no record found 
    		return getOkResponse(NOTFOUND, NOTFOUND);
    	
    	//v1
//		EChartJsonJava jsonObj = JSON.parseObject(members.getGroupNumberDetailCalls(), EChartJsonJava.class);
//		if(jsonObj != null){
//    		jsonObj.setStatus("OK");
//    		jsonObj.setMessage("OK");
//    		Response response = getOkResponse(jsonObj);
//        	System.out.println(new Date() + ": getByNumber Retrun now.");
//        	return response;
    		
    	//获取电话号码第三方信息，并保存到JSON。
    	EChartJsonJavaV2 jsonObj = JSON.parseObject(member.getGroupNumberDetailCalls().replace("attributes\":[]", "attributes\":{}"), EChartJsonJavaV2.class);
    	List<String> numbers = new ArrayList<String>();
    	for(Node node: jsonObj.getNodes())
    		numbers.add(node.getId());
    	try {
			Map<String, GangMember3rdPartyInfo> thirdInfos = gm3rdService.getByNumbers(numbers);
			gm3rdService.add3rdPartyInfoToJsonNodes(jsonObj, thirdInfos);
			gmService.adjutNodeSizes(jsonObj);
		} catch (Exception e) {
			System.out.println("Exception when add 3rd party info: " + e );
			LOG.info("Exception when add 3rd party info: " + e);
			e.printStackTrace();
		}
    	
		jsonObj.setStatus("OK");
		jsonObj.setMessage("OK");
		Response response = getOkResponse(jsonObj);
    	System.out.println(new Date() + ": getByNumber Retrun now.");
    	LOG.info(new Date() + ": getByNumber Retrun now.");
    	return response;
    }
    
    /**
	 * 发邮件用 
     * @return
     */
    @GET
    @Path("report/email")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNumbersForEmail() {
    	System.out.println(new Date() + ": getNumbersForEmail Requests coming in!");
    	LOG.info(new Date() + ": getNumbersForEmail Requests coming in!");
    	List<GangMember> members = new ArrayList<GangMember>();
    	try {
    		members = gmService.getNumbersToday();
    		System.out.println(new Date() + ": getNumbersForEmail GangMember count: " + members.size());
			LOG.info(new Date() + ": getNumbersForEmail GangMember count: " + members.size());
    	} catch (Exception e) {
			return getOkResponse("ERROR", "Graphx-webapp server internal error: " + e.getMessage());
		}
    	List<String> newNumbers = new ArrayList<String>();
    	
    	//生成EmailReportVONode
    	Map<String, EmailReportVO> nodes = new HashMap<String, EmailReportVO>();
    	for(GangMember member: members){
    		EmailReportVO node = new EmailReportVO();
    		node.fillAttributesFromGangMember(member);
    		if(member.isNew()){
    			newNumbers.add(member.getNumber());
    		}
    		nodes.put(member.getNumber(), node);
    	}
    	
    	//对于new Node，需要加入第三方数据。
    	try {
			Map<String, GangMember3rdPartyInfo> thirdInfoMap = gm3rdService.getByNumbers(newNumbers);
			for(String number: thirdInfoMap.keySet()){
				nodes.get(number).fillAttributesFrom3rdPartyInfo(thirdInfoMap.get(number));
			}
		} catch (Exception e) {
			System.out.println("Exception when query third party info: " + e);
			LOG.info("Exception when query third party info: " + e);
			e.printStackTrace();
		} 
    	
    	//填补空字段。
    	for(EmailReportVO vo: nodes.values())
    		vo.fillEmptyFields();
    	
    	System.out.println(new Date() + ": getNumbersForEmail Retrun now. Result count: " + nodes.size());
    	LOG.info(new Date() + ": getNumbersForEmail Retrun now. Result count: " + nodes.size());
    	Response response = Response.ok()
                .entity(JSON.toJSONString(nodes))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .build();
    	return response;
    }
    
    /**
     * 找出今天找到的全部电话号码。	 
     * @return
     */
    @GET
    @Path("numbers")  
    @Produces(MediaType.TEXT_HTML)
    public Response getNumbersToday() {
    	System.out.println(new Date() + ": getNumbersToday Requests coming in!");
    	LOG.info(new Date() + ": getNumbersToday Requests coming in!");
    	List<GangMember> members = new ArrayList<GangMember>();
    	try {
    		members = gmService.getNumbersToday();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("ERROR:"+ "Graphx-webapp server internal error: " + e.getMessage());
			return getOkResponse("ERROR", "Graphx-webapp server internal error: " + e.getMessage());
		}
    	System.out.println(new Date() + ": Query HBase done!");
    	System.out.println(new Date() + ": Member count: " + members.size());
    	LOG.info(new Date() + ": Query HBase done!");
    	LOG.info(new Date() + ": Member count: " + members.size());

    	if(members.size() == 0)	//no record for today
    		return getOkResponse(NOTFOUND, NOTFOUND);
    	
    	StringBuilder sb = new StringBuilder();
    	for(GangMember mem: members){
    		if(mem.getGid().equals(mem.getNumber()))	//一个组只返回一个电话号码。
    			sb.append(mem.toString()).append("\r\n");
    	}
    	
    	Response response = Response.ok()
                .entity(sb.toString())
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .build();
    	System.out.println(new Date() + ": getNumbersToday Retrun now.");
    	LOG.info(new Date() + ": getNumbersToday Retrun now.");
    	return response;
    }
    
    /**
     * 给定status和message，生成Response。
     */
    private Response getOkResponse(String status, String message){
    	EChartJsonJavaV2 jsonObj = new EChartJsonJavaV2();
    	jsonObj.setStatus(status);
		jsonObj.setMessage(message);
		Response response = Response.ok()
                .entity(JSON.toJSONString(jsonObj))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .build();
		return response;
    }
    
    /**
     * 给定EChartJsonJava，生成Response。
     */
    private Response getOkResponse(EChartJsonJavaV2 jsonObj){
		Response response = Response.ok()
                .entity(JSON.toJSONString(jsonObj))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .build();
		return response;
    }
    
}
