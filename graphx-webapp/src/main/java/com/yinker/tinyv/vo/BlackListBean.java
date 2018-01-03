package com.yinker.tinyv.vo;
/**
 * TrustRank BlackList Bean
 */
public class BlackListBean{
    private String mobile;
    private String tongd;
    private String miguan;
    private Integer dueday;
    public String toString(){
    	return String.format("mobile: %s, tongdun: %s, miguan: %s, due_day: %s.", mobile, tongd, miguan, dueday + "");
    }
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public Integer getDueday() {
		return dueday;
	}
	public void setDueday(Integer dueday) {
		this.dueday = dueday;
	}
    
}