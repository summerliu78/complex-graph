package com.yinker.tinyv.dao;

import java.io.IOException;

import com.yinker.tinyv.vo.BlackListBean;
import com.yinker.tinyv.vo.TrustLogBean;
import com.yinker.tinyv.vo.TrustRankResult;

public interface ITrustRankDAO {
	
	/**
	 * 根据电话号码找TrustRankResult。
	 * @throws IOException
	 */
	TrustRankResult getTrustRankResult(String mobile) throws IOException;
	
	/**
	 * 存储Black List
	 * @param bean
	 * @throws IOException
	 */
	void savaBlackListBean(BlackListBean bean) throws IOException;
	
	/**
	 * 存储TrustLog
	 * @param log
	 * @throws IOException
	 */
	void saveTrustRankLog(TrustLogBean log) throws  IOException;
}
