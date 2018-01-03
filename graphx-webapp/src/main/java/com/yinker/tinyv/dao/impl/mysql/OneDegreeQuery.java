package com.yinker.tinyv.dao.impl.mysql;

import com.yinker.tinyv.common.TxQueryRunner;
import com.yinker.tinyv.utils.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * user: angelo.liu
 * date: 2017/7/21 16:26
 */
public class OneDegreeQuery {

    private static String QUERY_SQL = "SELECT\n" +
            "\ty.mobile\n" +
            "\t,y.RISK_SCORE\n" +
            "FROM\n" +
            "\t(SELECT\n" +
            "\t\tx.*\n" +
            "\tFROM\n" +
            "\t\t(SELECT\n" +
            "\t\t\tui.MOBILE as mobile,\n" +
            "\t\t\tCR.RISK_SCORE RISK_SCORE,\n" +
            "\t\t\tCR.CREATE_TIME CREATE_TIME\n" +
            "\t\tFROM xiaodai.USER_INFO as ui\n" +
            "\t\tjoin CREDIT_RISK as CR ON CR.USER_ID = ui.ID AND CR.DEL_FLAG=0 and CR.RISK_TYPE='004002001'\n" +
            "\t\twhere ui.MOBILE in (%s)\n" +
            "\t\tUNION ALL\n" +
            "\t\tSELECT\n" +
            "\t\t\tui.MOBILE as mobile,\n" +
            "\t\t\tLR.RISK_SCORE RISK_SCORE,\n" +
            "\t\t\tLR.CREATE_TIME CREATE_TIME\n" +
            "\t\tFROM xiaodai.USER_INFO as ui\n" +
            "\t\tjoin LOAN_RISK as LR ON LR.USER_ID = ui.ID AND LR.DEL_FLAG=0 and LR.RISK_TYPE='004002001'\n" +
            "\t\twhere ui.MOBILE in (%s)\n" +
            "\t\t) as x\n" +
            "\tORDER BY x.CREATE_TIME DESC\n" +
            "\t) y\n" +
            "GROUP BY y.mobile";

    public static Map<String,Object> queryDegreeInfo(List<String> mobileList){
        Map<String,Object> returnMap = new HashMap<>();
        int result = 0;
        int riskScore = 0;
        int sum = 0;
        int num = 0;
        Double avg;
        String mobileValue = null;
        QueryRunner qr = new TxQueryRunner("weibo");

        try {
            String mobile = CollectionUtils.numberListToString(mobileList);

            String sql = String.format(QUERY_SQL,mobile);
            List<Map<String, Object>> values = qr.query(sql, new MapListHandler());
            if (values != null && values.size() != 0) {
                result = values.size();
                if(result >= 5){
                    for(int i=0; i<values.size();i++){
                        riskScore = Integer.parseInt(values.get(i).get("RISK_SCORE").toString());
                        mobileValue = values.get(i).get("mobile").toString();
                        returnMap.put("score",riskScore);
                        returnMap.put("mobile",mobileValue);
                        if(riskScore >= num){
                            num = riskScore;
                        }
                        sum += riskScore;
                    }
                    sum -= num;
                    avg = Integer.valueOf(sum).doubleValue() / values.size() -1;
                    if(avg >= 120){
                        returnMap.put("statusValue",1);
                    }
                    returnMap.put("avgScore",avg);
                }else {
                    for (int i=0;i<values.size();i++){
                        riskScore = Integer.parseInt(values.get(i).get("RISK_SCORE").toString());
                        mobileValue = values.get(i).get("mobile").toString();

                    }
                    returnMap.put("score",riskScore);
                    returnMap.put("mobile",mobileValue);
                    returnMap.put("statusValue",0);
                    returnMap.put("avgScore","");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnMap;
    }

}
