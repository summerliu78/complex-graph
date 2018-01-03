package com.yinker.tinyv.dao.impl.hbase;

import com.alibaba.fastjson.JSON;
import com.yinker.tinyv.dao.IOneDegreeDAO;
import com.yinker.tinyv.utils.HashUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * description:
 * user: angelo.liu
 * date: 2017/7/24 17:18
 */
public class OneDegreeDAO implements IOneDegreeDAO{
    @Override
    public void saveOneDegreeLog(Map<String,Object> log) throws IOException {
        HTable table = null;
        try {
//            table=new HTable(HbaseConn.GetConfiguration(), "TRUSTRANK:trustranklog");
            table = (HTable) HbaseConn.GetConnection().getTable(TableName.valueOf("TRUSTRANK:onedegree"));
            String localDate = LocalDate.now().toString();//2017-06-08
            localDate=localDate.replace("-","");//20170608
            String localDateMD5= HashUtil.md5(localDate);
            localDateMD5=localDateMD5.substring(0,2);
            String mobileValue=(String)log.get("mobile");
            byte[] row = Bytes.toBytes(localDateMD5+localDate+log.get("mobile"));
            String column = JSON.toJSONString(log);
            Put put = new Put(row);
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("onedegree_info"), Bytes.toBytes(column));
            put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("mobile"),Bytes.toBytes(mobileValue));
            table.put(put);
        } finally {
            if (table != null) table.close();
        }
    }
}
