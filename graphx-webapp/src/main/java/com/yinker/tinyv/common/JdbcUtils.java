package com.yinker.tinyv.common;

/**
 * Created by hjj on 2017/6/22.
 */
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JdbcUtils {

    private static DataSource ds = new ComboPooledDataSource();
    private static DataSource dshao=new ComboPooledDataSource();
    private static DataSource dsbbpay=new ComboPooledDataSource();
    private static ThreadLocal<Connection> tl;
    private static ThreadLocal<Connection>tlhao;
    private static ThreadLocal<Connection>tlbbpay;
    /*public static Session init() {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession("tangwei", "10.1.5.62", 22);
            session.setPassword("MhxzKhl");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPortForwardingL(3306, "10.0.100.73", 3306);
            session.connect();

        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }*/


    public static void initDataBase(String databasename){
        if(databasename==null||databasename.trim().length()==0){
            return;
        }
        if(databasename.toLowerCase().trim().equals("weibo")){
            ComboPooledDataSource dsConn = (ComboPooledDataSource) ds;
            try {
                dsConn.setDriverClass("com.mysql.jdbc.Driver");
                dsConn.setJdbcUrl("jdbc:mysql://10.0.100.73:3306/xiaodai?autoReconnect=true");
//                dsConn.setJdbcUrl("jdbc:mysql://localhost:3306/xiaodai?autoReconnect=true");
                dsConn.setUser("fengkong");
                dsConn.setPassword("Fe9Kd2TndO5GsW");
            } catch (Exception e) {

                e.printStackTrace();
            }
            tl = new ThreadLocal<Connection>();
        }else if(databasename.toLowerCase().trim().equals("hao123")){
            ComboPooledDataSource dsConn = (ComboPooledDataSource) dshao;
            try {
                dsConn.setDriverClass("com.mysql.jdbc.Driver");
                dsConn.setJdbcUrl("jdbc:mysql://10.0.100.72:3306/xiaodai?autoReconnect=true");
//                dsConn.setJdbcUrl("jdbc:mysql://localhost:3307/xiaodai?autoReconnect=true");
                dsConn.setUser("hweixin_read");
                dsConn.setPassword("E0N3uRiSkfT5GkC");
            } catch (Exception e) {

                e.printStackTrace();
            }
            tlhao = new ThreadLocal<Connection>();
        }else if(databasename.toLowerCase().trim().equals("bbpay")){
            ComboPooledDataSource dsConn = (ComboPooledDataSource) dsbbpay;
            try {
                dsConn.setDriverClass("com.mysql.jdbc.Driver");
                dsConn.setJdbcUrl("jdbc:mysql://10.0.16.152:6606/xiaodai?useCursorFetch=true&dontTrackOpenResources=true&defaultFetchSize=2000&autoReconnect=true");
//                dsConn.setJdbcUrl("jdbc:mysql://localhost:3308/xiaodai?useCursorFetch=true&dontTrackOpenResources=true&defaultFetchSize=2000&autoReconnect=true");
                dsConn.setUser("fk_bbqb_read");
                dsConn.setPassword("SFW%f3^(zsbJ=");
            } catch (Exception e) {

                e.printStackTrace();
            }
            tlbbpay = new ThreadLocal<Connection>();
        }

    }

    public static DataSource getDataSource() {
        return ds;
    }

    public static Connection getConnection(String databasename) throws SQLException {

        if("weibo".equals(databasename)){
            Connection conn = tl.get();
            if (conn != null)
                return conn;
            return ds.getConnection();
        }else if("hao123".equals(databasename)){
            Connection conn = tlhao.get();
            if (conn != null)
                return conn;
            return dshao.getConnection();
        }else {
            Connection conn = tlbbpay.get();
            if (conn != null)
                return conn;
            return dsbbpay.getConnection();
        }



    }

    /*
     * 开启事务
     */
    public static void beginTransaction(String databasename) throws SQLException {
        if("weibo".equals(databasename)){
            Connection conn = tl.get();// 获取当前事务连接
            if (conn != null)
                throw new SQLException("已经 开启事务，不能重复开启");
            conn = ds.getConnection();// 获取一个链接
            conn.setAutoCommit(false);// 需手动提交
            tl.set(conn); // 当前事务放到tl中
        }else if("hao123".equals(databasename)){
            Connection conn = tlhao.get();// 获取当前事务连接
            if (conn != null)
                throw new SQLException("已经 开启事务，不能重复开启");
            conn = ds.getConnection();// 获取一个链接
            conn.setAutoCommit(false);// 需手动提交
            tlhao.set(conn); // 当前事务放到tl中
        }else {
            Connection conn = tlbbpay.get();// 获取当前事务连接
            if (conn != null)
                throw new SQLException("已经 开启事务，不能重复开启");
            conn = ds.getConnection();// 获取一个链接
            conn.setAutoCommit(false);// 需手动提交
            tlbbpay.set(conn); // 当前事务放到tl中
        }

    }

    /*
     * 提交事务
     */
    public static void commitTransaction(String databasename) throws SQLException {
        if("weibo".equals(databasename)){
            Connection conn = tl.get();
            if (conn == null)
                throw new SQLException("没事事务不提交");
            conn.commit();// 提交事务
            conn.close();// 关闭事务
            conn = null;// 事务结束
            tl.remove();//
        }else if("hao123".equals(databasename)){
            Connection conn = tlhao.get();
            if (conn == null)
                throw new SQLException("没事事务不提交");
            conn.commit();// 提交事务
            conn.close();// 关闭事务
            conn = null;// 事务结束
            tlhao.remove();//
        }else {
            Connection conn = tlbbpay.get();
            if (conn == null)
                throw new SQLException("没事事务不提交");
            conn.commit();// 提交事务
            conn.close();// 关闭事务
            conn = null;// 事务结束
            tlbbpay.remove();//
        }

    }

    /**
     * 回滚事务
     *
     *
     * */

    public static void rollbackTransaction(String databasename) throws SQLException {
        if("weibo".equals(databasename)){
            Connection conn = tl.get();
            if (conn == null)
                throw new SQLException("没有事务不能回滚");
            conn.rollback();
            conn.close();
            conn = null;
            tl.remove();
        }else if("hao123".equals(databasename)){
            Connection conn = tlhao.get();
            if (conn == null)
                throw new SQLException("没有事务不能回滚");
            conn.rollback();
            conn.close();
            conn = null;
            tlhao.remove();
        }else {
            Connection conn = tlbbpay.get();
            if (conn == null)
                throw new SQLException("没有事务不能回滚");
            conn.rollback();
            conn.close();
            conn = null;
            tlbbpay.remove();
        }

    }

    public static void releaseConnection(Connection connection,String databasename)
            throws SQLException {
        if("weibo".equals(databasename)){
            Connection conn = tl.get();
            if (connection != conn) {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
        }else if("hao123".equals(databasename)){
            Connection conn = tlhao.get();
            if (connection != conn) {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
        }else {
            Connection conn = tlbbpay.get();
            if (connection != conn) {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
        }

    }

}
