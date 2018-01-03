package com.yinker.tinyv.service.stratgy;

public interface StratgyConstants {
	
	//号码状态
	int STATUS_PASS = 0;	//通过
	int STATUS_REJECT = 1;	//拒绝
	int STATUS_WAIT = 2;	//继续等待
	
	//策略阈值
	int THRES_DUE_DAYS = 5;	//逾期天数阈值
	double THRES_DUE_GANG_RATE = 0.25;	//逾期成员比例阈值
}
