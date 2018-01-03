package com.yinker.tinyv.dao.impl.hbase;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.yinker.tinyv.dao.ITrustRankDAO;
import com.yinker.tinyv.utils.HashUtil;
import com.yinker.tinyv.vo.BlackListBean;
import com.yinker.tinyv.vo.TrustLogBean;
import com.yinker.tinyv.vo.TrustRankResult;

public class TrustRankDAO implements ITrustRankDAO {

	private final String TABLE = "graph_trustrank_res";
	private final String TABLE_BL = "graph_blackwhitelist";
	private final byte[] COLUMNFAMILY_INFO = Bytes.toBytes("info");
	private final byte[] COLUMNFAMILY_BL = Bytes.toBytes("blacklist");
	private final byte[] MOBILE_QUALIFIER = Bytes.toBytes("mobile");
	private final byte[] SCORE_QUALIFIER = Bytes.toBytes("score");
	private final byte[] STATUS_QUALIFIER = Bytes.toBytes("status");
	private final byte[] TONGD_QUALIFIER = Bytes.toBytes("tongd");
	private final byte[] MIGUAN_QUALIFIER = Bytes.toBytes("miguan");
	private final byte[] DUEDAY_QUALIFIER = Bytes.toBytes("dueday");
	
	@Override
	public TrustRankResult getTrustRankResult(String mobile) throws IOException {
		System.out.println(new Date() + " getTrustRankResult() start:" + mobile);
		TrustRankResult res = null;
		HTable gang_table = (HTable) HbaseConn.GetConnection().getTable(TABLE);
		try {
			Get get = new Get(Bytes.toBytes(mobile));
			Result rr = gang_table.get(get);
			res = createTrustRankResultFromResult(rr);
		} finally {
			if (gang_table != null)
				gang_table.close();
		}
		System.out.println(new Date() + " getTrustRankResult() end:" + res);
		return res;
	}

	@Override
	public void savaBlackListBean(BlackListBean bean) throws IOException {
		HTable table = null;
	    try{
	    	table = new HTable(HbaseConn.GetConfiguration(), TABLE_BL);
	    	table.setAutoFlushTo(false);  //disable auto flush for better performance
	    	byte[] row  = Bytes.toBytes(bean.getMobile());
	        Put put = new Put(row);
	        put.addColumn(COLUMNFAMILY_BL, MOBILE_QUALIFIER, Bytes.toBytes(bean.getMobile()));
	        put.addColumn(COLUMNFAMILY_BL, TONGD_QUALIFIER, Bytes.toBytes(bean.getTongd()));
	        put.addColumn(COLUMNFAMILY_BL, MIGUAN_QUALIFIER, Bytes.toBytes(bean.getMiguan()));
	        put.addColumn(COLUMNFAMILY_BL, DUEDAY_QUALIFIER, Bytes.toBytes(bean.getDueday() + ""));
	        table.put(put);
	    }finally{
	    	if(null != table)  table.close();  //trigger flushCommits
	    }
	  }

	@Override
    public void saveTrustRankLog(TrustLogBean log) throws IOException {
        HTable table = null;
        try {
//            table=new HTable(HbaseConn.GetConfiguration(), "TRUSTRANK:trustranklog");
            table = (HTable) HbaseConn.GetConnection().getTable(TableName.valueOf("TRUSTRANK:trustranklog"));
            String localDate = LocalDate.now().toString();//2017-06-08
            localDate=localDate.replace("-","");//20170608
            String localDateMD5= HashUtil.md5(localDate);
            localDateMD5=localDateMD5.substring(0,2);
            byte[] row = Bytes.toBytes(localDateMD5+localDate+log.getMobile());
            Put put = new Put(row);
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("mobile"), Bytes.toBytes(log.getMobile()));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("middlestatus"), Bytes.toBytes(log.getMidleStatus()));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("realstatus"), Bytes.toBytes(log.getRealStatus()));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("score"), Bytes.toBytes(log.getScore()));
            table.put(put);
        } finally {
            if (table != null) table.close();
        }
    }
	
	// 从Result生成TrustRankResult
	private TrustRankResult createTrustRankResultFromResult(Result rr) {
		if (null == rr || rr.isEmpty())
			return null;
		String mobile = Bytes.toString(rr.getValue(COLUMNFAMILY_INFO, MOBILE_QUALIFIER));
		String score = Bytes.toString(rr.getValue(COLUMNFAMILY_INFO, SCORE_QUALIFIER));
		String status = Bytes.toString(rr.getValue(COLUMNFAMILY_INFO, STATUS_QUALIFIER));
		return new TrustRankResult(mobile, score, status);
	}
}
