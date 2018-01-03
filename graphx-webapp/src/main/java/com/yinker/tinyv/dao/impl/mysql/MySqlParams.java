package com.yinker.tinyv.dao.impl.mysql;

public interface MySqlParams {
	// init database constants
    static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    static final String MAX_POOL = "250";
    
    //weibo
    static final String DATABASE_URL_WEIBO = "jdbc:mysql://10.0.100.73:3306/xiaodai";
    static final String USERNAME_WEIBO = "fengkong";
    static final String PASSWORD_WEIBO = "Fe9Kd2TndO5GsW";

    //hao123
    static final String DATABASE_URL_HAO123 = "jdbc:mysql://10.0.100.72:3306/xiaodai";
    static final String USERNAME_HAO123 = "hweixin_read";
    static final String PASSWORD_HAO123 = "E0N3uRiSkfT5GkC";

}
