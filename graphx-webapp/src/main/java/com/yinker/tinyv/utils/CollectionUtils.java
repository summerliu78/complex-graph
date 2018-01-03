package com.yinker.tinyv.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ThinkPad on 2017/7/21.
 */
public class CollectionUtils {
    public static List<String> setToList(Set<String> numbers){
        List<String> mobiles=new ArrayList<String>();
        for(String mob:numbers){
            mobiles.add(mob);
        }
       return mobiles;
    }
    public static String numberListToString(List<String> numbers){
        StringBuilder numbersArr = new StringBuilder();
        for(String number: numbers)
            numbersArr.append("\"").append(number).append("\"").append(",");	//必须用String，因为DB里的字段是Varchar。用整数的话，会不走索引。
        String num = numbersArr.toString();
        num = num.substring(0, num.length() - 1);
        return num;
    }
}
