//package com.yinker.tinyv.controller;
//
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;
//import com.yinker.tinyv.common.JdbcUtils;
//import com.yinker.tinyv.common.TxQueryRunner;
//import com.yinker.tinyv.dao.impl.mysql.MysqlConnect;
//import org.apache.commons.dbutils.QueryRunner;
//import org.apache.commons.dbutils.handlers.ScalarHandler;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
///**
// * Created by ThinkPad on 2017/6/22.
// */
//@Path("/testdemo")
//public class TestDemo {
//    public static void main(String[] args) {
//        try {
//            // JdbcUtils.init();
//
//            QueryRunner qr = new TxQueryRunner();
//            String sql = "select COUNT(case when REPAY_STATUS ='002005002' then 1 end) as a from  LOAN_REPAY_PLAN where USER_ID in (SELECT id from USER_INFO where MOBILE=?) order by CREATE_TIME desc;";
//            Object obj=qr.query(sql,new ScalarHandler(),"13551300235");
//            Number  number=(Number)obj;
//            System.out.println(number.intValue());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static Session init() {
//        JSch jsch = new JSch();
//        Session session = null;
//        try {
//            session = jsch.getSession("tangwei", "10.1.5.62", 22);
//            session.setPassword("MhxzKhl");
//            session.setConfig("StrictHostKeyChecking", "no");
//            session.setPortForwardingL(3306, "10.0.100.73", 3306);
//            session.connect();
//
//        } catch (JSchException e) {
//            e.printStackTrace();
//        }
//        return session;
//    }
//    @POST
//    @Path("number/{number}")
//    public static String getCount(@PathParam("number") String phone) throws Exception {
//        Session session=init();
//        String url = "jdbc:mysql://localhost:3306/xiaodai";
//        String user = "fengkong";
//        String pwd = "Fe9Kd2TndO5GsW";
//        MysqlConnect mysqlconn = MysqlConnect.getConnFromPool(url, user, pwd);
//        String sql = "select COUNT(case when REPAY_STATUS ='002005002' then 1 end) as a from  LOAN_REPAY_PLAN where USER_ID in (SELECT id from USER_INFO where MOBILE='%s') order by CREATE_TIME desc;";
//        sql= String.format(sql, phone);
//        PreparedStatement statement = mysqlconn.connect().prepareStatement(sql);
//        ResultSet res = statement.executeQuery();
//        int count = res.getMetaData().getColumnCount();
//        System.out.println(count);
//        int result=0;
//        while (res.next()) {
//            for (int i = 1; i <= count; i++) {
//                result =res.getInt("a");
//            }
//
//        }
//        System.out.println(result);
//        session.disconnect();
//        return "index.jsp";
//    }
//}
