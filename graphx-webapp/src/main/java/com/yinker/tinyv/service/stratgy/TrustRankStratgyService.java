package com.yinker.tinyv.service.stratgy;

import java.io.IOException;
import java.util.Date;

import com.yinker.tinyv.dao.ITrustRankDAO;
import com.yinker.tinyv.dao.impl.hbase.TrustRankDAO;
import com.yinker.tinyv.vo.BlackListBean;
import com.yinker.tinyv.vo.TrustLogBean;
import com.yinker.tinyv.vo.TrustRankResult;

public class TrustRankStratgyService{

	private ITrustRankDAO trustRankDAO = new TrustRankDAO();
	
	/**
	 * 利用TrustRank来做策略。
	 */
	public TrustRankResult resultForMobile(String mobile) {
		TrustRankResult result = null;
		try {
			result = trustRankDAO.getTrustRankResult(mobile);
			System.out.println(new Date() + ": TrustRankStratgyService - resultForMobile： " + result);
		} catch (Exception e) {
			System.out.println("TrustRankStratgyService: Exception in resultForMobile: " + mobile);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 存储BlackList
	 * @param bean
	 */
	public void savaBlackListBean(BlackListBean bean)  {
		try {
			trustRankDAO.savaBlackListBean(bean);
		} catch (IOException e) {
			System.out.println("Exception when savaBlackListBean: " + bean);
			e.printStackTrace();
		}
	}
	
	public void saveTrustRankLog(TrustLogBean log) {
		try {
			trustRankDAO.saveTrustRankLog(log);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
