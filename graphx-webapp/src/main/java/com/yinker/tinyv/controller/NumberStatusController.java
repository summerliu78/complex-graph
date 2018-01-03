package com.yinker.tinyv.controller;

import com.alibaba.fastjson.JSON;
import com.yinker.tinyv.UniversalConfig;
import com.yinker.tinyv.common.TxQueryRunner;
import com.yinker.tinyv.dao.impl.mysql.OneDegreeQuery;
import com.yinker.tinyv.service.OneDegreeService;
import com.yinker.tinyv.service.stratgy.TrustRankStratgyService;
import com.yinker.tinyv.utils.CollectionUtils;
import com.yinker.tinyv.verfication.PojoVerifier;
import com.yinker.tinyv.verfication.VerifyResult;
import com.yinker.tinyv.vo.BlackListBean;
import com.yinker.tinyv.vo.ComplexNetworkInput;
import com.yinker.tinyv.vo.TrustLogBean;
import com.yinker.tinyv.vo.TrustRankResult;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * GangNumber resource
 *
 * @author Zhu Xiuwei
 */
@Path("/result")
public class NumberStatusController {
    private Logger LOG = Logger.getLogger(NumberStatusController.class);
    private TrustRankStratgyService trustRankStratgyService = new TrustRankStratgyService();
    private OneDegreeService oneDegreeService=new OneDegreeService();
    /**
     * 给定一个电话号码，查找号码在复杂网络中的标记状态。
     * @param in 手机号的同盾分、蜜罐分、通话记录信息。
     * @param numberString 手机号
     * @return 号码在复杂网络中的标记状态。
     */
//    @POST
//    @Path("number/{number}")  
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getByNumber(ComplexNetworkInput in, @PathParam("number") String numberString) {
//    	System.out.println(new Date() + " NumberStatusController: getByNumber Requests coming in: " + numberString);
//    	int status = 0;
//    	
//    	//验证输入的ComplexNetworkInput
//    	VerifyResult verify = PojoVerifier.verify(in);
//    	if(!verify.isPass()){
//    		return Response.status(400)
//    				.entity(verify.getMsg())
//                    .header("Content-Type", "application/json; charset=utf-8")
//                    .header("Access-Control-Allow-Origin", "*")
//                    .build();
//    	}
//    	
//    	NumberResult rs = numberStatusService.getStatus(numberString);
//    	if(rs == null || rs.getStatus() == 2){	//ES里没有保存状态，或还是等待状态, 需要用策略计算。
//    		status = (int)complexNetworkStratgyService.resultForMobile(numberString);
//    		numberStatusService.saveStatus(numberString, status);	//策略重新计算的结果，到ES中更新状态。
//    	}else
//    		status = rs.getStatus();
//    	
//    	System.out.println(new Date() + ": NumberStatusController: getByNumber status: " + status);
//    	Map<String, Object> m = new HashMap<String, Object>();
//    	String msg = "";
//    	switch(status){
//    		case 0:
//    			msg = UniversalConfig.STATUS_PASS;
//    			break;
//    		case 1:
//    			msg = UniversalConfig.STATUS_REJECT;
//    			break;
//    		case 2:
//    			msg = UniversalConfig.STATUS_WAIT;
//    			break;
//    	}
//    	m.put("mobile", numberString);
//    	m.put("status", status);
//    	m.put("msg", msg);
//    	Response response = Response.ok()
//                .entity(JSON.toJSONString(m))
//                .header("Content-Type", "application/json; charset=utf-8")
//                .header("Access-Control-Allow-Origin", "*")
//                .build();
//		return response;
//    }

    /**
     * 给定一个电话号码，查找号码在复杂网络中的标记状态。
     *
     * @param in           手机号的同盾分、蜜罐分、通话记录信息。
     * @param numberString 手机号
     * @return 号码在复杂网络中的标记状态。
     */
    @POST
    @Path("number/{number}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByNumber2(ComplexNetworkInput in, @PathParam("number") String numberString) {
        System.out.println(new Date() + " NumberStatusController: getByNumber2 Requests coming in: " + numberString);
        LOG.info(new Date() + " NumberStatusController: getByNumber2 Requests coming in: " + numberString);
        //查数据库
        List<String> mobileList = getMobiles(in);
        Map<String,Object> MapValue = OneDegreeQuery.queryDegreeInfo(mobileList);
        MapValue.put("mobile",numberString);

        //验证输入的ComplexNetworkInput
        VerifyResult verify = PojoVerifier.verify(in);
        if (!verify.isPass()) {
            return Response.status(400)
                    .entity(verify.getMsg())
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }

        //如果符合黑名单用户条件，记黑名单
        try {

            if (Double.parseDouble(in.getMiguan()) <= 10 || Double.parseDouble(in.getTongd()) >= 180) {
                System.out.println("Save a mobile to blacklist: " + numberString + ", tongd: " + in.getTongd() + ", miguan: " + in.getMiguan());
                LOG.info("Save a mobile to blacklist: " + numberString + ", tongd: " + in.getTongd() + ", miguan: " + in.getMiguan());
                BlackListBean bean = new BlackListBean();
                bean.setMobile(numberString);
                bean.setMiguan(in.getMiguan());
                bean.setTongd(in.getTongd());
                bean.setDueday(-1);
                trustRankStratgyService.savaBlackListBean(bean);
            }
        } catch (Throwable t) {
            System.out.println("Exception when write blacklist:");
            LOG.info("Exception when write blacklist:");
            t.printStackTrace();
        }
        Map<String, Integer> trust = getCount(numberString);
        TrustRankResult result = trustRankStratgyService.resultForMobile(numberString);
        int status = result == null ? 2 : Integer.parseInt(result.getStatus());
        System.out.println(new Date() + ": NumberStatusController: getByNumber2 status: " + status + ". " + numberString);
        LOG.info(new Date() + ": NumberStatusController: getByNumber2 status: " + status + ". " + numberString);
        Map<String, Object> m = new HashMap<String, Object>();
        String msg = "";
        int returnStatus = status;
        switch (status) {
            case 0:
                msg = UniversalConfig.STATUS_PASS;
                break;
            case 1:

                if (trust.get("count") >= 1 && trust.get("overdueday") < 7) {
                    returnStatus = 0;
                }
                msg = UniversalConfig.STATUS_REJECT;
                break;
            case 2:
                returnStatus = 0;
                msg = UniversalConfig.STATUS_PASS;
                break;
        }
        int statusValue = Integer.parseInt(MapValue.get("statusValue").toString());
        if(returnStatus == 0 && statusValue == 0){
            m.put("status", 0);
        }else {
            m.put("status", 1);

        }
        m.put("mobile", numberString);
//        m.put("status", returnStatus);
        m.put("msg", msg);

        //TrustLog结果存入HBASE表
        String score = result == null ? "" : result.getScore();
        TrustLogBean log = new TrustLogBean();
        log.setMidleStatus(returnStatus + "");
        log.setMobile(numberString);
        log.setRealStatus(status + "");
        log.setScore(score);
        trustRankStratgyService.saveTrustRankLog(log);
        oneDegreeService.saveOneDegreeLog(MapValue);

        //记录HDFS
        Response response = Response.ok()
                .entity(JSON.toJSONString(m))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Access-Control-Allow-Origin", "*")
                .build();
        return response;
    }


    public Map<String, Integer> getCount(String phone) {
        QueryRunner qr = new TxQueryRunner("weibo");
        int result = 0;
        int overduDay = 0;
        Map<String, Integer> trust = new HashMap<String, Integer>();
        try {
            String sql = "select id,CASE WHEN ACT_DAY IS NOT NULL then OVERDUE_DAY WHEN NOW()< DUE_DAY then 0 ELSE DATEDIFF(DATE(NOW()),DUE_DAY) end as OVERDUE_DAY  from  LOAN_REPAY_PLAN where USER_ID in (SELECT id from USER_INFO where MOBILE=?) order by OVERDUE_DAY desc;";
            List<Map<String, Object>> values = qr.query(sql, new MapListHandler(), phone);
            if (values != null && values.size() != 0) {
                result = values.size();
                Number dueDay = (Number) values.get(0).get("OVERDUE_DAY");
                overduDay = dueDay.intValue();
            }

            trust.put("count", result);
            trust.put("overdueday", overduDay);
            LOG.info("you input phone number is：" + phone + "  payback times is" + result);
            System.out.println("payback times is=" + result + "  newoverdueday=" + overduDay);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return trust;
    }

    public List<String> getMobiles(ComplexNetworkInput in) {
        Set<String> mobiles = new HashSet<String>();
        for (ComplexNetworkInput.Call call : in.getCalls()) {
            if(call.getMobile()!=null){
                mobiles.add(call.getOtherNum());
            }

        }
        List<String> mobilesList= CollectionUtils.setToList(mobiles);

        return mobilesList;
    }
}
