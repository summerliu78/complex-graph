package com.yinker.tinyv.dao.impl.mysql;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yinker.tinyv.common.TxQueryRunner;
import com.yinker.tinyv.dao.IGangMember3rdPartyInfoDAO;
import com.yinker.tinyv.vo.GangMember3rdPartyInfo;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class GangMember3rdPartyInfoDAOImpl implements IGangMember3rdPartyInfoDAO{
	
	private MysqlConnect mysqlConnectWeibo =  MysqlConnect.getConnFromPool(MySqlParams.DATABASE_URL_WEIBO, MySqlParams.USERNAME_WEIBO, MySqlParams.PASSWORD_WEIBO);
	private MysqlConnect mysqlConnectHao123 = MysqlConnect.getConnFromPool(MySqlParams.DATABASE_URL_HAO123, MySqlParams.USERNAME_HAO123, MySqlParams.PASSWORD_HAO123);

	/**
	 * 给定一组电话号码，找到这组电话的GangMember3rdPartyInfo
	 * @throws IOException
	 */
	public List<GangMember3rdPartyInfo> getByNumbers(List<String> numbers) throws Exception{
		List<GangMember3rdPartyInfo> res = new ArrayList<GangMember3rdPartyInfo>();
		System.out.println(new Date() + " numbers count: " + numbers.size());
		if(null == numbers || numbers.size() == 0)
			return res;
		
		String sql = String.format("SELECT ui.MOBILE as \"mobile\", case when ui.SEX = \"001003001\" THEN \"男\" else \"女\" end as \"sex\", "
				+ "(SELECT BIRTHDAY FROM xiaodai.CREDIT_APPLY WHERE USER_ID = ui.ID limit 1) as \"birthday\", ui.MOBILE_HOME as \"mobile_home\","
				+ "(SELECT ID_CARD FROM xiaodai.USER_ACCOUNT WHERE USER_ID = ui.ID) as \"id_card\", "
				+ "(SELECT ID_HOME FROM xiaodai.USER_ACCOUNT WHERE USER_ID = ui.ID) as \"id_home\", "
				+ "(SELECT REAL_NAME FROM xiaodai.USER_ACCOUNT WHERE USER_ID = ui.ID) as \"real_name\","
				+ "(SELECT min(APPLY_TIME) FROM xiaodai.CREDIT_APPLY WHERE USER_ID = ui.ID) as \"apply_time\","
				+ "(SELECT (SELECT  CASE WHEN ACT_DAY IS NOT NULL then OVERDUE_DAY WHEN NOW()< DUE_DAY then '0' ELSE DATEDIFF(DATE(NOW()),DUE_DAY) end as OVERDUE_DAY FROM xiaodai.LOAN_REPAY_PLAN where LOAN_ID=lar.LOAN_ID and DEL_FLAG=0  order by CREATE_TIME desc limit 1) FROM LOAN_AUDIT_RESULT as lar where lar.USER_ID = ui.ID and AUDIT_STATUS = '002010001' ORDER BY CREATE_TIME DESC limit 1) as \"due_dates\","
				+ "(SELECT CASE APPLY_STATUS WHEN '001002001' THEN '未提交' WHEN '001002002' THEN '待授信-自动授信' WHEN '001002003' THEN '待授信-人工授信' when '001002004' then '授信通过' when '001002005' then '拒绝授信-可立即重申' when '001002006' then '拒绝授信-可30天后重申' when '001002007' then '永久拒绝' when '001002008' then '申请失效'end FROM xiaodai.CREDIT_APPLY WHERE USER_ID=ui.ID and APPLY_STATUS<>'001002001' order by id desc limit 1) as \"reply_status\","
				+ "(SELECT CASE REPAY_STATUS WHEN '002005001' THEN '待还款'  when '002005002' then '已还款' end FROM xiaodai.LOAN_REPAY_PLAN WHERE USER_ID=ui.ID and DEL_FLAG = 0 order by CREATE_TIME  desc limit 1) as \"repay_status\","
				+ "(SELECT RISK_SCORE FROM xiaodai.CREDIT_RISK where USER_ID=ui.ID and DEL_FLAG=0 and RISK_TYPE='004002001' ORDER BY CREATE_TIME DESC limit 1) as \"tongd\" ,"
				+ "(SELECT APPLY_TIME FROM xiaodai.LOAN_INFO where USER_ID=ui.ID ORDER BY ID limit 1) as \"first_apply_loan_date\" "
				+ "FROM xiaodai.USER_INFO as ui where ui.MOBILE in (%s);", numberListToString(numbers));
		System.out.println(sql);
		PreparedStatement statement_weibo = null,  statement_hao123 = null;
		ResultSet rs_weibo = null,rs_hao123 = null;
		try {

			//hao123
			statement_hao123 = mysqlConnectHao123.connect().prepareStatement(sql);
		    rs_hao123 = statement_hao123.executeQuery();
		    ResultSetMetaData data=rs_hao123.getMetaData(); 
	        int col = rs_hao123.getMetaData().getColumnCount();
	        System.out.println(new Date() + " col count from hao123: " + col);
	        while (rs_hao123.next()) {
	        	GangMember3rdPartyInfo g = new GangMember3rdPartyInfo();
	            for (int i = 1; i <= col; i++) {
	            	String columnName = data.getColumnName(i); 
	            	relationalMap(g, columnName, rs_hao123.getString(columnName));
					//System.out.println(g.toString());
				}
	            res.add(g);
	        }
	        
	        //weibo
	        statement_weibo = mysqlConnectWeibo.connect().prepareStatement(sql);
	        rs_weibo = statement_weibo.executeQuery();
			 data=rs_weibo.getMetaData();
	        col = rs_weibo.getMetaData().getColumnCount();
	        System.out.println(new Date() + " col count from weibo: " + col);
	        while (rs_weibo.next()) {
	        	GangMember3rdPartyInfo g = new GangMember3rdPartyInfo();
	        	for (int i = 1; i <= col; i++) {
	        		String columnName = data.getColumnName(i); 
	        		relationalMap(g, columnName, rs_weibo.getString(columnName));
					//System.out.println(g);
				}
	        	res.add(g);

	        }

		} catch (SQLException e) {
		    e.printStackTrace();
		} finally {
			if(rs_hao123 != null) rs_hao123.close();
			if(rs_weibo != null) rs_weibo.close();
			if(statement_weibo != null) statement_weibo.close();
			mysqlConnectWeibo.disconnect();
			mysqlConnectHao123.disconnect();
		}
		return res;
	}
	
	/**
	 * 给定一组电话号码，找到这组电话的同盾分
	 * @throws IOException
	 */
	@Override
	public Map<String, Double> getTongDunNumbers(List<String> numbers) throws Exception {
		Map<String, Double> res = new HashMap<String, Double>();
		System.out.println(new Date() + " numbers count: " + numbers.size());
		if(null == numbers || numbers.size() == 0)
			return res;
		
		String sql = String.format("SELECT ui.MOBILE as \"mobile\",  (SELECT RISK_SCORE FROM xiaodai.CREDIT_RISK where USER_ID=ui.ID and DEL_FLAG=0 and RISK_TYPE='004002001' "
				+ "ORDER BY CREATE_TIME DESC limit 1) as \"tongd\"  FROM xiaodai.USER_INFO as ui where ui.MOBILE in (%s);", numberListToString(numbers));
		System.out.println(sql);
		PreparedStatement statement_weibo = null,  statement_hao123 = null;
		ResultSet rs_weibo = null, rs_hao123 = null;
		try {
			//hao123
			statement_hao123 = mysqlConnectHao123.connect().prepareStatement(sql);
		    rs_hao123 = statement_hao123.executeQuery();
		    ResultSetMetaData data=rs_hao123.getMetaData(); 
	        int col = rs_hao123.getMetaData().getColumnCount();
	        System.out.println(new Date() + " col count from hao123: " + col);
	        while (rs_hao123.next()) {
	        	String num = "";
	        	double val = 0;
	            for (int i = 1; i <= col; i++) {
	            	String columnName = data.getColumnName(i); 
	            	if(columnName.toLowerCase().equals("mobile"))
	            		num = rs_hao123.getString(columnName);
	            	else if(columnName.toLowerCase().equals("tongd"))
	            		val = rs_hao123.getDouble(columnName);
	            }
	            res.put(num, val);
	        }
	        
	        //weibo
	        statement_weibo = mysqlConnectWeibo.connect().prepareStatement(sql);
	        rs_weibo = statement_weibo.executeQuery();
	        data=rs_weibo.getMetaData(); 
	        col = rs_weibo.getMetaData().getColumnCount();
	        System.out.println(new Date() + " col count from weibo: " + col);
	        while (rs_weibo.next()) {
	        	String num = "";
	        	double val = 0;
	            for (int i = 1; i <= col; i++) {
	            	String columnName = data.getColumnName(i); 
	            	if(columnName.toLowerCase().equals("mobile"))
	            		num = rs_weibo.getString(columnName);
	            	else if(columnName.toLowerCase().equals("tongd"))
	            		val = rs_weibo.getDouble(columnName);
	            }
	            res.put(num, val);
	        }
		} catch (SQLException e) {
		    e.printStackTrace();
		} finally {
			if(rs_hao123 != null) rs_hao123.close();
			if(rs_weibo != null) rs_weibo.close();
			if(statement_weibo != null) statement_weibo.close();
			mysqlConnectWeibo.disconnect();
			mysqlConnectHao123.disconnect();
		}
		return res;
	}
	
	@Override
	/**
	 * 给一个号码，找出这个号码申请借款次数。
	 */
	public int getLoanCount(String number) throws Exception {
		String sql = String.format("select count(LOAN_NO) as loan_count from LOAN_INFO as li, USER_INFO as ui where ui.mobile in (\"%s\") and ui.id=li.user_id;"
				, number);
		System.out.println(sql);
		PreparedStatement statement_weibo = null,  statement_hao123 = null;
		ResultSet rs_weibo = null, rs_hao123 = null;
		int res = 0;
		try {
			//hao123
			statement_hao123 = mysqlConnectHao123.connect().prepareStatement(sql);
		    rs_hao123 = statement_hao123.executeQuery();
		    ResultSetMetaData data=rs_hao123.getMetaData(); 
	        int col = rs_hao123.getMetaData().getColumnCount();
	        System.out.println(new Date() + " col count from hao123: " + col);
	        while (rs_hao123.next()) {
	        	res += rs_hao123.getInt("loan_count");
	        }
	        
	        //weibo
	        statement_weibo = mysqlConnectWeibo.connect().prepareStatement(sql);
	        rs_weibo = statement_weibo.executeQuery();
	        data=rs_weibo.getMetaData(); 
	        col = rs_weibo.getMetaData().getColumnCount();
	        System.out.println(new Date() + " col count from weibo: " + col);
	        while (rs_weibo.next()) {
	        	res += rs_weibo.getInt("loan_count");
	        }
		} catch (SQLException e) {
		    e.printStackTrace();
		} finally {
			if(rs_hao123 != null) rs_hao123.close();
			if(rs_weibo != null) rs_weibo.close();
			if(statement_weibo != null) statement_weibo.close();
			mysqlConnectWeibo.disconnect();
			mysqlConnectHao123.disconnect();
		}
		return res;
	}
	
	private void relationalMap(GangMember3rdPartyInfo g, String columnName, String value){
		switch(columnName.toLowerCase()){
		case "mobile":
			g.setNumber(value);
			break;
		case "sex":
			g.setSex(value);
			break;
		case "birthday":
			g.setBirthday(value);
			break;
		case "mobile_home":
			g.setMobile_home(value);
			break;
		case "id_card":
			g.setId_card(value);
			break;
		case "id_home":
			g.setId_home(value);
			break;
		case "real_name":
			g.setReal_name(value);
			break;
		case "apply_time":
			g.setApply_time(value);
			break;
		case "due_dates":
			g.setDue_dates(value);
			break;
		case "reply_status":
			g.setReply_status(value);
			break;
		case "repay_status":
			g.setRepay_status(value);
			break;
		case "tongd":
			g.setTongd(value);
			break;
		case "first_apply_loan_date":
			g.setFirst_apply_loan_date(value);
			break;
		}
	}
	
	private String numberListToString(List<String> numbers){
		StringBuilder numbersArr = new StringBuilder();
		for(String number: numbers)
			numbersArr.append("\"").append(number).append("\"").append(",");	//必须用String，因为DB里的字段是Varchar。用整数的话，会不走索引。
		String num = numbersArr.toString();
		num = num.substring(0, num.length() - 1);
		return num;
	}
	
	public static void main(String[] args) throws Exception {
		List<String> nums = new ArrayList<String>();
		nums.add("18888667844");
		nums.add("15117630004");
		nums.add("18308402877");
		nums.add("18687239998");
		StringBuilder numbersArr = new StringBuilder();
		for(String number: nums)
			numbersArr.append(number).append(",");
		String num = numbersArr.toString();
		num = num.substring(0, num.length() - 1);
		System.out.println(num);
	}

	
}
