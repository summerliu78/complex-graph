package com.yinker.tinyv.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.yinker.tinyv.vo.GangMember3rdPartyInfo;

public interface IGangMember3rdPartyInfoDAO {
	
	/**
	 * 给定一组电话号码，找到这组电话的GangMember3rdPartyInfo
	 * @throws IOException
	 */
	List<GangMember3rdPartyInfo> getByNumbers(List<String> numbers) throws Exception;
	
	/**
	 * 给定一组电话号码，找到这组电话的同盾分
	 * @throws IOException
	 */
	Map<String, Double> getTongDunNumbers(List<String> numbers) throws Exception;
	
	/**
	 * 给一个号码，找出这个号码申请借款次数。
	 */
	int getLoanCount(String number) throws Exception;
	
}
