package com.yinker.tinyv.dao.impl.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class MysqlConnect {
    
	private String dbUrl;
	private String userName;
	private String pwd;
	private DataSource source;
	private Connection connection;

	private static Map<String, MysqlConnect> pool = new HashMap<String, MysqlConnect>();
	
	public static MysqlConnect getConnFromPool(String dbUrl, String userName, String pwd){
		String key = dbUrl;
		if(!pool.containsKey(dbUrl)){
			MysqlConnect conn = new MysqlConnect(dbUrl,  userName,  pwd);
			pool.put(key, conn);
		}
		return pool.get(key);
	}
	
	private MysqlConnect(String dbUrl, String userName, String pwd){
    	System.out.println(new Date() + ": SQL: new MysqlConnect().");
		this.userName = userName;
		this.dbUrl = dbUrl;
		this.pwd = pwd;
		source = getDataSource();
	}
	

    // create DataSource
    private DataSource getDataSource(){
    	System.out.println(new Date() + ": SQL: getDataSource().");
    	PoolProperties p = new PoolProperties();
        p.setUrl(this.dbUrl);
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername(this.userName);
        p.setPassword(this.pwd);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(500);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
          "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
          "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DataSource datasource = new DataSource();
        datasource.setPoolProperties(p);
        return datasource;
    }

    // connect database
    public Connection connect() throws SQLException {
        if (connection == null) {
        	System.out.println(new Date() + ": SQL: get connect from source");
            try {
                Class.forName(MySqlParams.DATABASE_DRIVER);
                connection = source.getConnection();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    	System.out.println(new Date() + ": SQL: get connect from source done");
        return connection;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}