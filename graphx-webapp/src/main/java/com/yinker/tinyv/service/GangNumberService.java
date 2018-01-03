package com.yinker.tinyv.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.yinker.tinyv.dao.IGangMemberDAO;
import com.yinker.tinyv.dao.impl.hbase.GangMemberDAOImpl;
import com.yinker.tinyv.vo.EChartJsonJavaV2;
import com.yinker.tinyv.vo.GangMember;

public class GangNumberService {

	private IGangMemberDAO gmDao = new GangMemberDAOImpl();

	/**
	 * 根据电话号码，查找一个GangMember
	 * @param numberString GangMember的电话号码
	 * @return 找到的GangMember。找不到返回Null。
	 * @throws IOException
	 */
	public GangMember getByNumber(String numberString) throws Exception {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		Date d = sdf.parse("20170501");
    	GangMember member = gmDao.getByNubmer(new Date(), numberString);
    	if(member == null)	//no record for today
    		return null;
    	EChartJsonJavaV2 jsonObj = JSON.parseObject(member.getGroupNumberDetailCalls().replace("attributes\":[]", "attributes\":{}"), EChartJsonJavaV2.class);
		member.setGroupNumberDetailCalls(jsonObj);
		return member;
    }
	
	/**
	 * 调整各node的degree值，好更美观在EChartUI显示。
	 * @param jsonObj
	 * @throws Exception
	 */
	public void adjutNodeSizes(EChartJsonJavaV2 jsonObj) throws Exception {
		if(jsonObj != null){
    		//调整degree的值，一律归按比例调整到最大40。
    		int maxDegree = Integer.MIN_VALUE, max_thres = 40;
    		for (EChartJsonJavaV2.Node node: jsonObj.getNodes()) {
				if(node.getSize() > maxDegree)
					maxDegree = node.getSize();
			}
    		double rate = (double)max_thres / (double)maxDegree;
    		if(rate > 1){	//如果最大的degree小于40，一律按比例调大。
    			for (EChartJsonJavaV2.Node node: jsonObj.getNodes()) 
    				node.setSize(node.getSize() * (int)rate);
    		}
		}
    }
	
	/**
	 * 返回今天全部找到的gang members. 
	 * @return
	 * @throws IOException
	 */
	public List<GangMember> getNumbersToday() throws IOException {
    	return gmDao.getGangMumbersOfDay(new Date());
    }
	
}
