package com.yinker.tinyv.dao.impl.hbase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;

/**
 * @author Zhu Xiuwei
 */
public class HbaseConn{
	
	private static HConnection conn = null;
	private static Configuration conf = null;
	
	public static HConnection GetConnection(){
		if(null == conn)
			initConneciton();
		return conn;
	}
	
	public static Configuration GetConfiguration(){
		if(null == conf)
			initConneciton();
		return conf;
	}
	
	private static void initConneciton(){
		conf = new HBaseConfiguration(getHHConfig());
		try {
			conn = HConnectionManager.createConnection(conf);
			HBaseAdmin admin = new HBaseAdmin(conf);
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从HBase配置文件加载Configuration对象。要保证配置文件在ClassPath里： -
	 * 从Spark提交时，需要配置spark.driver.extraClassPath（由driver读取HBase）或者spark.executor.extraClassPath（由executor读取HBase）指向配置文件目录。
	 * - 在Eclipse IDE里测试时，需要把保存配置文件的目录添加到Build path。 右键点击项目 -> Build Path ->
	 * Link Source，指向配置文件目录。
	 */
	private static Configuration getHHConfig() {
		System.out.println(new Date() + " HBase getHHConfig() start.");
		Configuration conf = HBaseConfiguration.create();
		InputStream confResourceAsInputStream = conf.getConfResourceAsInputStream("hbase-site-prod-NOEXIST.xml");
		int available = 0;
		try {
			available = confResourceAsInputStream.available();
		} catch (Exception e) {
			System.out.println("WARNING: configuration files not found locally");
		} finally {
			IOUtils.closeQuietly(confResourceAsInputStream);
		}
		// 如果配置文件找不到，用线下环境HBase。仅供debug使用。
		if (available == 0) {
			System.out.println("WARNING: Use test environment HBase as configuration files not found locally");
			conf = new Configuration();
            conf.set("hbase.zookeeper.quorum", "datanode04.yinker.com,datanode02.yinker.com,datanode01.yinker.com");  //线上 
//            conf.set("hbase.zookeeper.quorum", "slavenode4,slavenode1,slavenode3");  //线下 
			conf.set("hbase.zookeeper.property.clientPort", "2181");
			conf.set("zookeeper.znode.parent", "/hbase-unsecure");
		}
		System.out.println(new Date() + " HBase getHHConfig() end.");
		return conf;
	}
}
