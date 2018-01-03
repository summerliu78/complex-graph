package com.yinker.tinyv.vo;
/**
 * Trust Rank结果类
 */
public class TrustRankResult {
	private String mobile;
	private String score;
	private String status;

	public TrustRankResult(String mobile, String score, String status) {
		this.mobile = mobile;
		this.score = score;
		this.status = status;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return String.format("mobile; %s, score; %s, status; %s.", mobile, score, status);
	}
}