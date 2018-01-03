package com.yinker.tinyv.dao;

import java.io.IOException;
import java.util.Map;

/**
 * description:
 * user: angelo.liu
 * date: 2017/7/24 17:16
 */
public interface IOneDegreeDAO {

    /**
     * 存储ave_Log
     * @param logMap
     * @throws IOException
     */
    void saveOneDegreeLog(Map<String,Object> logMap) throws  IOException;
}
