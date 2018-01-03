package com.yinker.tinyv;

public interface UniversalConfig {
	long IS_NEW_THRESHOLD_MILLSEC = 15 * 60 * 1000;	//当一个Hbase row的“年龄(TimeStamp)”超过这个阈值，则即使HBase里isNew=true，依旧取值为isNew=false。
	String IS_NEW_CHN = "新发现";
	String IS_NOT_NEW_CHN = "非新发现";
	
	String STATUS_PASS = "PASS";
	String STATUS_REJECT = "REJECT";
	String STATUS_WAIT = "WAIT";
}
