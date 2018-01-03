package com.yinker.tinyv.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.yinker.tinyv.vo.GangMember;

public interface IGangMemberDAO {
	
	/**
	 * 给定日期，找到这天全部的团伙成员信息。
	 * @throws IOException
	 */
	List<GangMember> getGangMumbersOfDay(Date date) throws IOException;
	
	/**
	 * 给定日期，以及电话号码，找团伙成员。
	 * @throws IOException
	 */
	GangMember getByNubmer(Date date, String number) throws IOException;
	
	
	
}
