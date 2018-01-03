package com.yinker.tinyv.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yinker.tinyv.dao.IGangMember3rdPartyInfoDAO;
import com.yinker.tinyv.dao.impl.mysql.GangMember3rdPartyInfoDAOImpl;
import com.yinker.tinyv.vo.EChartJsonJavaV2;
import com.yinker.tinyv.vo.GangMember3rdPartyInfo;

public class GangMember3rdPartyInfoService {
	private IGangMember3rdPartyInfoDAO gm3rdDao = new GangMember3rdPartyInfoDAOImpl();
	
	/**
	 * 给定一组电话号码，找到这组电话的GangMember3rdPartyInfo
	 * @throws IOException
	 */
	public Map<String, GangMember3rdPartyInfo> getByNumbers(List<String> numbers) throws Exception{
		List<GangMember3rdPartyInfo> list = gm3rdDao.getByNumbers(numbers);
		Map<String, GangMember3rdPartyInfo> map = new HashMap<String, GangMember3rdPartyInfo>();
		
		//申请授信时间排序，然后申请时间重新按照order赋值。
		Collections.sort(list, GangMember3rdPartyInfo.applyOrderComparator());
		int applyOrder = 1;
		for(GangMember3rdPartyInfo info: list){
			if(info.getApply_time() == null)
				info.setApply_time("-");
			if(!info.getApply_time().equals("-")){
				//info.setApply_time(applyOrder + "");
				applyOrder++;
			}
			map.put(info.getNumber(), info);
		}
		
		//清洗地区信息。只保留到“市”或者“地区”
		for(GangMember3rdPartyInfo info: list){
			String id_home = info.getId_home();
			if(null != id_home){
				if(id_home.contains("市"))
					info.setId_home(id_home.substring(0, id_home.indexOf("市") + 1));
				else if(id_home.contains("地区"))
					info.setId_home(id_home.substring(0, id_home.indexOf("地区") + 2));
			}
		}
		return map;
	}
	
	/**
	 * Add third party info to EJson Object.
	 * @param 、numberString
	 * @return
	 * @throws Exception
	 */
	public void add3rdPartyInfoToJsonNodes(EChartJsonJavaV2 json, Map<String, GangMember3rdPartyInfo> thirdPartyInfos) throws Exception {
		for (EChartJsonJavaV2.Node node: json.getNodes()) {
			GangMember3rdPartyInfo info = thirdPartyInfos.get(node.getId());
			EChartJsonJavaV2.Node.Attributes attr = new EChartJsonJavaV2.Node.Attributes(info);
			attr.setDegree(node.getSize() + "");
			attr.setIs_new(node.getCategory_name());
			attr.fillEmptyFileds();
			node.setAttributes(attr);
		}
    }
	
	/**
	 * 给定一组电话号码，找到这组电话的同盾分。
	 * @throws IOException
	 */
	public Map<String, Double> getTongDunNumbers(List<String> numbers) throws Exception{
		Map<String, Double> map = gm3rdDao.getTongDunNumbers(numbers);
		return map;
	}
	
	/**
	 * 判断一个用户是否是新用户。 新用户定义：借款次数<=1次。
	 * @param mobile 用户手机号
	 * @return 是否是新用户
	 * @throws Exception
	 */
	public boolean isUserNew(String mobile) throws Exception {
		return gm3rdDao.getLoanCount(mobile) < 2;
	}
	
	//test
	public static void main(String[] args) {
//		String json = "{\"links\":[{\"source\":\"18888667844\",\"target\":\"18302586494\"},{\"source\":\"18888667844\",\"target\":\"18798767552\"},{\"source\":\"18888667844\",\"target\":\"18798888513\"},{\"source\":\"15285185827\",\"target\":\"15685138342\"},{\"source\":\"15284674546\",\"target\":\"18212796685\"},{\"source\":\"18798767552\",\"target\":\"15117440428\"},{\"source\":\"18798767552\",\"target\":\"18302586494\"},{\"source\":\"18798767552\",\"target\":\"18798888513\"},{\"source\":\"18798767552\",\"target\":\"18888667844\"},{\"source\":\"18798767552\",\"target\":\"18212796685\"},{\"source\":\"18798767552\",\"target\":\"18798738207\"},{\"source\":\"15685138342\",\"target\":\"15285185827\"},{\"source\":\"15685138342\",\"target\":\"18758372561\"},{\"source\":\"15685138342\",\"target\":\"18302586494\"},{\"source\":\"15685138342\",\"target\":\"18798888513\"},{\"source\":\"18798888513\",\"target\":\"18302586494\"},{\"source\":\"18798888513\",\"target\":\"18798767552\"},{\"source\":\"18798888513\",\"target\":\"18758372561\"},{\"source\":\"18798888513\",\"target\":\"18888667844\"},{\"source\":\"18758372561\",\"target\":\"18798888513\"},{\"source\":\"18758372561\",\"target\":\"18302586494\"},{\"source\":\"18758372561\",\"target\":\"18798767552\"},{\"source\":\"18758372561\",\"target\":\"15685138342\"},{\"source\":\"18212796685\",\"target\":\"18798767552\"},{\"source\":\"18212796685\",\"target\":\"15284674546\"},{\"source\":\"18302586494\",\"target\":\"18798767552\"},{\"source\":\"18302586494\",\"target\":\"18798888513\"},{\"source\":\"18302586494\",\"target\":\"18888667844\"},{\"source\":\"18798738207\",\"target\":\"18798767552\"},{\"source\":\"15117440428\",\"target\":\"18798767552\"}],\"nodes\":[{\"attributes\":{},\"category\":0,\"category_name\":\"非新发现\",\"id\":\"15117440428\",\"name\":\"15117440428\",\"size\":6},{\"attributes\":{},\"category\":0,\"category_name\":\"非新发现\",\"id\":\"15285185827\",\"name\":\"15285185827\",\"size\":6},{\"attributes\":{},\"category\":0,\"category_name\":\"非新发现\",\"id\":\"18758372561\",\"name\":\"18758372561\",\"size\":18},{\"attributes\":{},\"category\":0,\"category_name\":\"非新发现\",\"id\":\"15284674546\",\"name\":\"15284674546\",\"size\":6},{\"attributes\":{},\"category\":0,\"category_name\":\"非新发现\",\"id\":\"18888667844\",\"name\":\"18888667844\",\"size\":18},{\"attributes\":{},\"category\":0,\"category_name\":\"非新发现\",\"id\":\"18798888513\",\"name\":\"18798888513\",\"size\":27},{\"attributes\":{},\"category\":0,\"category_name\":\"非新发现\",\"id\":\"18798767552\",\"name\":\"18798767552\",\"size\":39},{\"attributes\":{},\"category\":0,\"category_name\":\"非新发现\",\"id\":\"18798738207\",\"name\":\"18798738207\",\"size\":6},{\"attributes\":{},\"category\":0,\"category_name\":\"非新发现\",\"id\":\"18302586494\",\"name\":\"18302586494\",\"size\":24},{\"attributes\":{},\"category\":0,\"category_name\":\"非新发现\",\"id\":\"15685138342\",\"name\":\"15685138342\",\"size\":18},{\"attributes\":{},\"category\":0,\"category_name\":\"非新发现\",\"id\":\"18212796685\",\"name\":\"18212796685\",\"size\":12}]}";
//		EChartJsonJavaV2 jsonObj = JSON.parseObject(json, EChartJsonJavaV2.class);
//    	System.out.println(jsonObj);
		
		EChartJsonJavaV2.Node.Attributes attr = new EChartJsonJavaV2.Node.Attributes();
		attr.setDegree("3");
		attr.setSex("男");
		attr.fillEmptyFileds();
		System.out.println(attr.getDegree());
		System.out.println(attr.getSex());
		System.out.println(attr.getApplay_credit_time());

		String id_home = "四川省梅州市哈哈市";
		if(null != id_home){
			if(id_home.contains("市"))
				System.out.println(id_home.substring(0, id_home.indexOf("市") + 1));
			else if(id_home.contains("地区"))
				System.out.println(id_home.substring(0, id_home.indexOf("地区") + 2));
		}
	}
}
