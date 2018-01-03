package com.yinker.tinyv.vo;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.yinker.tinyv.utils.StringUtils;

/**
 * VO for email report. 
 * @author Zhu Xiuwei
 */
public class EmailReportVO {
	
	private String gid = "-";	//团伙号
	private String sex = "-";	//性别
	private String birthday = "-";	//生日
	private String mobile_home = "-";	//手机号归属地
	private String id_card = "-";	//身份证
	private String id_home = "-";	//身份证归属地
	private String real_name = "-";	//用户姓名
	private String apply_time = "-";	//授信申请时间	
	private String due_dates = "-";		//逾期天数
	private String reply_status = "-";	//审批状态
	private String repay_status = "-";	//还款状态
	private String tongd = "-";	//同盾分
	private String miguan = "-";	//蜜罐分
	private String first_apply_loan_date = "-";	//首次申请借款日期
	private String isnew = "-";	//是否新发现
	private String degree = "-";	//度
	private String runtime= "-";	//scc运行时间
	
	public void fillAttributesFromGangMember(GangMember member){
		this.gid = member.getGid();
		this.isnew = member.isNew() ? "是": "否";
		this.degree = member.getDegree() + "";
		this.runtime = member.getAlgoRuntime();
	}
	
	public void fillAttributesFrom3rdPartyInfo(GangMember3rdPartyInfo info){
		this.sex = info.getSex();
		this.birthday = info.getBirthday();
		this.mobile_home = info.getMobile_home();
		this.id_card = info.getId_card();
		this.id_home = info.getId_home();
		this.real_name = info.getReal_name();
		this.apply_time = info.getApply_time();
		this.due_dates = info.getDue_dates();
		this.reply_status = info.getReply_status();
		this.repay_status = info.getRepay_status();
		this.tongd = info.getTongd();
		this.miguan = info.getMiguan();
		this.first_apply_loan_date = info.getFirst_apply_loan_date();
	}
	
	public void fillEmptyFields(){
		if(StringUtils.isNullOrEmpty(sex))
			sex = "-";
		if(StringUtils.isNullOrEmpty(birthday))
			birthday = "-";
		if(StringUtils.isNullOrEmpty(mobile_home))
			mobile_home = "-";
		if(StringUtils.isNullOrEmpty(id_card))
			id_card = "-";
		if(StringUtils.isNullOrEmpty(id_home))
			id_home = "-";
		if(StringUtils.isNullOrEmpty(real_name))
			real_name = "-";
		if(StringUtils.isNullOrEmpty(apply_time))
			apply_time = "-";
		if(StringUtils.isNullOrEmpty(due_dates))
			due_dates = "-";
		if(StringUtils.isNullOrEmpty(reply_status))
			reply_status = "-";
		if(StringUtils.isNullOrEmpty(repay_status))
			repay_status = "-";
		if(StringUtils.isNullOrEmpty(tongd))
			tongd = "-";
		if(StringUtils.isNullOrEmpty(miguan))
			miguan = "-";
		if(StringUtils.isNullOrEmpty(first_apply_loan_date))
			first_apply_loan_date = "-";
		if(StringUtils.isNullOrEmpty(gid))
			gid = "-";
		if(StringUtils.isNullOrEmpty(isnew))
			isnew = "-";
		if(StringUtils.isNullOrEmpty(degree))
			degree = "-";
		if(StringUtils.isNullOrEmpty(runtime))
			runtime = "-";
	}
	
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getMobile_home() {
		return mobile_home;
	}
	public void setMobile_home(String mobile_home) {
		this.mobile_home = mobile_home;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getId_home() {
		return id_home;
	}
	public void setId_home(String id_home) {
		this.id_home = id_home;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getApply_time() {
		return apply_time;
	}
	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}
	public String getDue_dates() {
		return due_dates;
	}
	public void setDue_dates(String due_dates) {
		this.due_dates = due_dates;
	}
	public String getReply_status() {
		return reply_status;
	}
	public void setReply_status(String reply_status) {
		this.reply_status = reply_status;
	}
	public String getRepay_status() {
		return repay_status;
	}
	public void setRepay_status(String repay_status) {
		this.repay_status = repay_status;
	}
	public String getTongd() {
		return tongd;
	}
	public void setTongd(String tongd) {
		this.tongd = tongd;
	}
	public String getMiguan() {
		return miguan;
	}
	public void setMiguan(String miguan) {
		this.miguan = miguan;
	}
	public String getFirst_apply_loan_date() {
		return first_apply_loan_date;
	}
	public void setFirst_apply_loan_date(String first_apply_loan_date) {
		this.first_apply_loan_date = first_apply_loan_date;
	}
	public String getIsnew() {
		return isnew;
	}
	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
		
	
	public static void main(String[] args) {
		EmailReportVO node1 = new EmailReportVO();
		EmailReportVO node2 = new EmailReportVO();
		Map<String, EmailReportVO> nodes = new HashMap<String, EmailReportVO>();
		nodes.put("123", node1);
		nodes.put("456", node2);
		System.out.println(JSON.toJSONString(nodes));
	}

}
