package com.yinker.tinyv.dao.impl.hbase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.yinker.tinyv.UniversalConfig;
import com.yinker.tinyv.dao.IGangMemberDAO;
import com.yinker.tinyv.vo.GangMember;

/**
 * HBase工具类
 * @author Zhu Xiuwei
 */
public class GangMemberDAOImpl implements IGangMemberDAO{

	private final String GANG_NUMBER_TABLE = "graph_gang_all";
	private final String GANG_NUMBER_TABLE_MORE = "graph_gang_all_More";
	private final byte[] INFO_COLUMNFAMILY = Bytes.toBytes("info");
	private final byte[] ISNEW_QUALIFIER = Bytes.toBytes("isnew");
	private final byte[] GID_QUALIFIER = Bytes.toBytes("gid");
	private final byte[] DEGREE_QUALIFIER = Bytes.toBytes("degree");
	private final byte[] CALLS_QUALIFIER = Bytes.toBytes("calls");
	
	@Override
	public List<GangMember> getGangMumbersOfDay(Date date) throws IOException {
		System.out.println(new Date() + " getGangNumbersOfDay() start.");
		List<GangMember> arr = new ArrayList<GangMember>();
		ResultScanner scanner = null;
		HTable gang_table = (HTable)HbaseConn.GetConnection().getTable(GANG_NUMBER_TABLE);
		int count = 0;
		try{
			Scan scan = new Scan();
			scanner = gang_table.getScanner(scan);
			Result rr = scanner.next();
			while (rr != null) {
				GangMember gangNumber = createGangNumberFromResult(rr);
				arr.add(gangNumber);
				rr = scanner.next();
				count ++;
			}
		}finally{
			if(scanner != null)
				scanner.close();
			if(gang_table != null)
				gang_table.close();
		}
		System.out.println(new Date() + " getGangNumbersOfDay() end. Result count: " + count);
		return arr;	
	}
	
	@Override
	public GangMember getByNubmer(Date date, String number) throws IOException {
		System.out.println(new Date() + " getByNubmer() start.");
		String rawKey = number;
		GangMember member = null;
		HTable gang_table = (HTable)HbaseConn.GetConnection().getTable(GANG_NUMBER_TABLE);
		try{
			Get get = new Get(Bytes.toBytes(rawKey));
			Result rr = gang_table.get(get);
			member = createGangNumberFromResult(rr);
		}finally{
			if(gang_table != null)
				gang_table.close();
		}
		if(member == null){	//如果GANG_NUMBER_TABLE表中不存在，尝试GANG_NUMBER_TABLE_MORE表。
			gang_table = (HTable)HbaseConn.GetConnection().getTable(GANG_NUMBER_TABLE_MORE);
			try{
				Get get = new Get(Bytes.toBytes(rawKey));
				Result rr = gang_table.get(get);
				member = createGangNumberFromResult(rr);
			}finally{
				if(gang_table != null)
					gang_table.close();
			}
		}
		System.out.println(new Date() + " getByNubmer() end.");
		return member;
	}
	
	//从Result生成GangNumber
	private GangMember createGangNumberFromResult(Result rr){
		if(null == rr || rr.isEmpty())
			return null;
		String isnew = Bytes.toString(rr.getValue(INFO_COLUMNFAMILY, ISNEW_QUALIFIER));
		boolean isNewBool = isnew.equals("1") ? true : false;
		long ts = rr.raw()[0].getTimestamp();
		
		String gid = Bytes.toString(rr.getValue(INFO_COLUMNFAMILY, GID_QUALIFIER));
		String degreeString = Bytes.toString(rr.getValue(INFO_COLUMNFAMILY, DEGREE_QUALIFIER));
		int degree = 1;
		try {
			degree = Integer.parseInt(degreeString);
		} catch (Exception e) {
		}
		String phone = Bytes.toString(rr.getRow());
		String calls = Bytes.toString(rr.getValue(INFO_COLUMNFAMILY, CALLS_QUALIFIER));
		if(isNewBool && (System.currentTimeMillis() - ts > UniversalConfig.IS_NEW_THRESHOLD_MILLSEC)){
			System.out.println(String.format("号码%s的时间戳为%s，超过阈值%s,设置isNew=false。", phone, ts + "", UniversalConfig.IS_NEW_THRESHOLD_MILLSEC + ""));
			isNewBool = false;
		}
		return new GangMember(phone, isNewBool, gid, degree, calls, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts));
	}
}
