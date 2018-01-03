package com.yinker.tinyv.service;

import java.io.IOException;
import java.util.Date;

import com.yinker.xiaov.tinyv.ESTelemetryClient.GenericSearch;
import com.yinker.xiaov.tinyv.ESTelemetryClient.bean.NumberResult;

public class NumberStatusService {
	
	//private GenericSearch gs = new GenericSearch("10.1.8.199:9300", "tinyv");
	private GenericSearch gs = new GenericSearch("10.2.19.112:9300", "tinyv-elasticsearch-server");
	private String indexName = "complex_net";
	private String typeName = "num_result";
	
	/**
	 * Get status of a number
	 * @param number
	 * @return
	 */
	public NumberResult getStatus(String number){
		NumberResult nr = gs.getNumberResultById(indexName, typeName, number);
		return nr;
	}
	
	/**
	 * 保存号码的Status到ES。
	 * @param number 手机号
	 * @param status 状态码
	 */
	public void saveStatus(String number, int status){
		NumberResult nr = getStatus(number);
		if(nr == null){
			nr = new NumberResult(number, status, System.currentTimeMillis());
		}else{
			nr.setStatus(status);	//keep insertTime in ES unchanged.
		}
		try {
			gs.bulkInsert(nr, indexName, typeName, number);
		} catch (IOException e) {
			System.out.println(new Date() + ": Exception occurred when insert number " + number + " to ES.");
			e.printStackTrace();
		}finally{
			gs.close();
		}
	}
}
