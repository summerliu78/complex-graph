package com.yinker.tinyv.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplexNetworkInput {
	private String tongd = "";
	private String miguan = "";
	private List<Call> calls;
	
	@Override
	public String toString(){
		return String.format("%s,%s,%s", tongd, miguan, calls);
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Call{
		private long userId;
		private String mobile;
		private String otherNum;
		private String callChannel;
		private int callTime;
		private long callDatetime;
		private long createTime;
		private long updateTime;
		
		@Override
		public String toString(){
			return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
					userId, mobile, otherNum, callChannel, callTime, callDatetime, createTime, updateTime);
		}
		
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getOtherNum() {
			return otherNum;
		}
		public void setOtherNum(String otherNum) {
			this.otherNum = otherNum;
		}
		public String getCallChannel() {
			return callChannel;
		}
		public void setCallChannel(String callChannel) {
			this.callChannel = callChannel;
		}
		public int getCallTime() {
			return callTime;
		}
		public void setCallTime(int callTime) {
			this.callTime = callTime;
		}
		public long getCallDatetime() {
			return callDatetime;
		}
		public void setCallDatetime(long callDatetime) {
			this.callDatetime = callDatetime;
		}
		public long getCreateTime() {
			return createTime;
		}
		public void setCreateTime(long createTime) {
			this.createTime = createTime;
		}
		public long getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(long updateTime) {
			this.updateTime = updateTime;
		}
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
	public List<Call> getCalls() {
		return calls;
	}
	public void setCalls(List<Call> calls) {
		this.calls = calls;
	}
	
}
