package com.yinker.tinyv.service;

import com.yinker.tinyv.dao.IOneDegreeDAO;
import com.yinker.tinyv.dao.ITrustRankDAO;
import com.yinker.tinyv.dao.impl.hbase.OneDegreeDAO;
import com.yinker.tinyv.dao.impl.hbase.TrustRankDAO;
import com.yinker.tinyv.vo.TrustLogBean;

import java.io.IOException;
import java.util.Map;

/**
 * description:
 * user: angelo.liu
 * date: 2017/7/24 17:49
 */
public class OneDegreeService {

    private IOneDegreeDAO oneDegreeDAO = new OneDegreeDAO();
    public void saveOneDegreeLog(Map<String,Object> logMap) {
        try {
            oneDegreeDAO.saveOneDegreeLog(logMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
