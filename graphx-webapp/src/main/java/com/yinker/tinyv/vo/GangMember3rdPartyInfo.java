package com.yinker.tinyv.vo;

public class GangMember3rdPartyInfo {
	private String number = "-";	//电话号
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
	
	//按照申请时间排序的比较器
	public static java.util.Comparator<GangMember3rdPartyInfo> applyOrderComparator() {   
	    return new java.util.Comparator<GangMember3rdPartyInfo>() {
			@Override
			public int compare(GangMember3rdPartyInfo o1, GangMember3rdPartyInfo o2) {
				if(o1.apply_time == null && o2.apply_time == null)
					return 0;
				else if(o1.apply_time == null && o2.apply_time != null)
					return -1;
				else if(o1.apply_time != null && o2.apply_time == null)
					return 1;
				else
					return o1.apply_time.compareTo(o2.apply_time);
			}   
	    };
	}
	
	@Override
	public String toString(){
		return String.format("number: %s, sex: %s, birthday: %s, mobile_home: %s, real_name: %s, id_card: %s, id_home: %s,"
				+ " apply_time: %s, due_dates: %s, reply_status: %s, repay_status: %s, tongd: %s, miguan: %s, first_apply_loan_date: %s, ", 
				number, sex, birthday, mobile_home, real_name, id_card, id_home, apply_time, due_dates,
				reply_status, repay_status, tongd, miguan, first_apply_loan_date);
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
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
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
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
	
}
