package com.yinker.tinyv.common;

/**
 * Created by hjj on 2017/6/22.
 */

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;


public class TxQueryRunner extends QueryRunner  {
    private String dataBaseName="weibo";
    public TxQueryRunner(String dataBaseName) {
        this.dataBaseName=dataBaseName;
        JdbcUtils.initDataBase(dataBaseName);
    }


    @Override
    public int[] batch(String sql, Object[][] params) throws SQLException {
        Connection con = JdbcUtils.getConnection(dataBaseName);
        int[] result = super.batch(con, sql, params);
        JdbcUtils.releaseConnection(con,dataBaseName);
        return result;
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params)
            throws SQLException {
        Connection con = JdbcUtils.getConnection(dataBaseName);
        T result = super.query(con, sql, rsh, params);
        JdbcUtils.releaseConnection(con,dataBaseName);
        return result;
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh) throws SQLException {
        Connection con = JdbcUtils.getConnection(dataBaseName);
        T result = super.query(con, sql, rsh);
        JdbcUtils.releaseConnection(con,dataBaseName);
        return result;
    }

    @Override
    public int update(String sql) throws SQLException {
        Connection con = JdbcUtils.getConnection(dataBaseName);
        int result = super.update(con, sql);
        JdbcUtils.releaseConnection(con,dataBaseName);
        return result;
    }

    @Override
    public int update(String sql, Object param) throws SQLException {
        Connection con = JdbcUtils.getConnection(dataBaseName);
        int result = super.update(con, sql, param);
        JdbcUtils.releaseConnection(con,dataBaseName);
        return result;
    }

    @Override
    public int update(String sql, Object... params) throws SQLException {
        Connection con = JdbcUtils.getConnection(dataBaseName);
        int result = super.update(con, sql, params);
        JdbcUtils.releaseConnection(con,dataBaseName);
        return result;
    }
}
