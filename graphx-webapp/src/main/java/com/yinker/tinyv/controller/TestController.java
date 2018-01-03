package com.yinker.tinyv.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.alibaba.fastjson.JSON;
import com.yinker.tinyv.dao.impl.mysql.GangMember3rdPartyInfoDAOImpl;
import com.yinker.tinyv.service.stratgy.TrustRankStratgyService;
import com.yinker.tinyv.vo.BlackListBean;

/**
 * GangNumber resource
 * @author Zhu Xiuwei
 */
@Path("/test")
public class TestController {

	/**
	 * 给定一个电话号码，查找团伙成员通话情况。
//	 * @param numberString
	 * @return 电话号码所在的团伙成员童话情况。
	 */
    @GET
    @Path("empty")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response returnEmptyJson() {
    	Response response = Response.ok()
                .entity(JSON.toJSONString(new String[]{}))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .build();
    	return response;
    }
 
    @GET
    @Path("sql")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response testMysql() {
    	GangMember3rdPartyInfoDAOImpl g = new GangMember3rdPartyInfoDAOImpl();
    	System.out.println(1111);
    	try {
    		//test getByNumbers
//    		List<String> nums = new ArrayList<String>();
//    		nums.add("18888667844");
//    		nums.add("15117630004");
//    		nums.add("18308402877");
//    		nums.add("18687239998");
//			List<GangMember3rdPartyInfo> l = g.getByNumbers(nums);
    		
    		//test getLoanCount
    		int loanCount = g.getLoanCount("15810560726");
    		System.out.println("15810560726: " + loanCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	Response response = Response.ok()
                .entity(JSON.toJSONString(new String[]{}))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .build();
    	return response;
    }
    
    @GET
    @Path("hbase")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response testHBase() {
    	TrustRankStratgyService trustRankStratgyService = new TrustRankStratgyService();
    	BlackListBean bean = new BlackListBean();
		bean.setMobile("12345678");
		bean.setMiguan("9.0");
		bean.setTongd("181");
		bean.setDueday(-1);
		trustRankStratgyService.savaBlackListBean(bean);
    	Response response = Response.ok()
                .entity(JSON.toJSONString(new String[]{}))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .build();
    	return response;
    }
    
    public static void main(String[] args) {
	}
    
}
