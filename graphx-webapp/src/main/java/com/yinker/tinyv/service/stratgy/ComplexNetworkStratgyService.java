package com.yinker.tinyv.service.stratgy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yinker.tinyv.service.GangMember3rdPartyInfoService;
import com.yinker.tinyv.service.GangNumberService;
import com.yinker.tinyv.utils.StringUtils;
import com.yinker.tinyv.vo.GangMember;
import com.yinker.tinyv.vo.GangMember3rdPartyInfo;

public class ComplexNetworkStratgyService{

	private GangMember3rdPartyInfoService thirdInfoService = new GangMember3rdPartyInfoService();
	private GangNumberService gangNumberService = new GangNumberService();
	private StringUtils stringUtils = new StringUtils();
	
	public double resultForMobile(String mobile) {
		System.out.println(new Date() + " resultForMobile: " + mobile);
		//首先获取mobile所在组的成员号码。
		GangMember member = null;
		try {
			member = gangNumberService.getByNumber(mobile);
		} catch (Exception e) {
			System.out.println("Exception in resultForMobile: " + mobile);
			e.printStackTrace();
			return StratgyConstants.STATUS_PASS;
		}
		if(null == member)	//复杂网络里没有号码
			return StratgyConstants.STATUS_PASS;
		
		//然后查询全部成员号码的第三方信息
		Map<String, GangMember3rdPartyInfo> infos = new HashMap<String, GangMember3rdPartyInfo>();
		List<String> numbers = new ArrayList<String>(member.getGangNumbers()); //全部成员号码
		try {
			infos = thirdInfoService.getByNumbers(numbers);
		} catch (Exception e) {
			System.out.println("Exception in resultForMobile: " + mobile);
			e.printStackTrace();
			return StratgyConstants.STATUS_PASS;
		}
		
		/*
		 * 策略：
		 * 只考虑已授信成员的最近一次贷款表现。如果超过一定阈值比例的成员逾期天数大于某阈值，则所有成员都拒掉。
		 */
		int totalNum = 0;		//全部“已授信”成员数量 
		int totalDueNum = 0;	//全部“已授信”成员中，逾期超过阈值的成员的数量。
		for(String num: infos.keySet()){
			GangMember3rdPartyInfo thirdInfo = infos.get(num);
			String status = thirdInfo.getReply_status();	//授信状态
			System.out.println("Reply_status of mobile: " + mobile + ": " + status);
			if(status != null && status.equals("已授信")){
				totalNum ++;
				if(stringUtils.toIntOrElse(thirdInfo.getDue_dates(), 0) >= StratgyConstants.THRES_DUE_DAYS){
					totalDueNum ++;
				}
			}
		}
		double rate = totalNum > 0 ? (double)totalDueNum / (double)totalNum : 0;
		System.out.println("rate of mobile: " + mobile + ": " + rate);
		if(rate >= StratgyConstants.THRES_DUE_GANG_RATE)
			return StratgyConstants.STATUS_REJECT;	//拒绝
		else
			return StratgyConstants.STATUS_PASS;	//通过
	}
	
}
