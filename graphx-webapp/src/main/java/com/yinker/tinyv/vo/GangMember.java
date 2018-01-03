package com.yinker.tinyv.vo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.yinker.tinyv.UniversalConfig;
import com.yinker.tinyv.dao.IGangMemberDAO;
import com.yinker.tinyv.dao.impl.hbase.GangMemberDAOImpl;
import com.yinker.tinyv.vo.EChartJsonJavaV2.Link;
import com.yinker.tinyv.vo.EChartJsonJavaV2.Node;

/**
 * GangNumber Jave Bean
 * @author Zhu Xiuwei
 */
public class GangMember {
	private String number;  //Phone number
    private boolean isNew;  //If the number is newly found
    private String gid;  //Gang ID the number belongs to
    private int degree;  //Degree of the number in graph
    private String groupNumberDetailCalls;   //所在团伙成员之间的通话记录
    private String algoRuntime;	//算法运行时间
    private Long count;//hbase存储数据的总数，由于内存不足问题增加的字段，方便在邮件展示

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public GangMember(String number, boolean isNew, String gid, int degree, String groupNumberDetailCalls, String algoRuntime){
    	this.number = number;
    	this.isNew = isNew;
    	this.gid = gid;
    	this.degree = degree;
    	this.groupNumberDetailCalls = groupNumberDetailCalls;
    	this.algoRuntime = algoRuntime;
    }
    
    /**
     * 从groupNumberDetailCalls这个JSON字符串里，提取出这个号码所在的gang number的全部号码。
     */
    public Set<String> getGangNumbers(){
    	EChartJsonJavaV2 jsonObj = JSON.parseObject(groupNumberDetailCalls.replace("attributes\":[]", "attributes\":{}"), EChartJsonJavaV2.class);
    	List<Link> links = jsonObj.getLinks();
    	Set<String> nums = new HashSet<String>();
    	for(Link link: links){
    		nums.add(link.getSource());
    		nums.add(link.getTarget());
    	}
    	return nums;
    }
    
    @Override
    public String toString(){
    	return String.format("Number: %s, gid: %s, isNew: %s, degree: %s, algoRuntime: %s, groupNumberDetailCalls: %s", number, gid, isNew + "", degree + "", algoRuntime, groupNumberDetailCalls);
    }

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public String getGroupNumberDetailCalls() {
		return groupNumberDetailCalls;
	}

	public void setGroupNumberDetailCalls(EChartJsonJavaV2 jsonObj) {
		IGangMemberDAO gmDao = new GangMemberDAOImpl();
		
		//调整EChartJsonJavaV2各个Node的category_name，即isNew的值。
		for(Node node: jsonObj.getNodes()){
			if(node.getCategory_name().equals(UniversalConfig.IS_NEW_CHN)){
				try {
					GangMember member = gmDao.getByNubmer(new Date(), node.getId());
					if(member.isNew == false){
						node.setCategory_name(UniversalConfig.IS_NOT_NEW_CHN);
					}
					else{
						long ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(member.getAlgoRuntime()).getTime();
						if((System.currentTimeMillis() - ts > UniversalConfig.IS_NEW_THRESHOLD_MILLSEC)){
							System.out.println(String.format("号码%s的时间戳为%s，超过阈值%s,设置isNew=false。", node.getId(), ts + "", UniversalConfig.IS_NEW_THRESHOLD_MILLSEC + ""));
							node.setCategory_name(UniversalConfig.IS_NOT_NEW_CHN);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		this.groupNumberDetailCalls = JSON.toJSONString(jsonObj);
	}
	public String getAlgoRuntime() {
		return algoRuntime;
	}

	public void setAlgoRuntime(String algoRuntime) {
		this.algoRuntime = algoRuntime;
	}
}
