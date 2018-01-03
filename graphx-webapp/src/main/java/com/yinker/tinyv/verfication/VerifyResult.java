package com.yinker.tinyv.verfication;

public class VerifyResult {
	private boolean pass;
	private String msg;
	
	public VerifyResult(boolean pass, String msg){
		this.pass = pass;
		this.msg = msg;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
