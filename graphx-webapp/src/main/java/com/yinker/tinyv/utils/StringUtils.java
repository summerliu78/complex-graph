package com.yinker.tinyv.utils;

public class StringUtils {
	public static boolean isNullOrEmpty(String s){
		if(null == s)
			return true;
		else{
			return s.trim().equals("");
		}
	}
	
	public int toIntOrElse(String source, int defaultVal) {
	    if(source == null )
	      return defaultVal;
	    int res = defaultVal;
	    try{
	      res = Integer.parseInt(source);
	    }catch(Exception e){}  //do nothing
	    return res;
	  }
	
	public static void main(String[] args) {
		System.out.println(StringUtils.isNullOrEmpty(""));
		System.out.println(StringUtils.isNullOrEmpty(null));
		System.out.println(StringUtils.isNullOrEmpty("a"));
		System.out.println(StringUtils.isNullOrEmpty(" "));
		System.out.println(StringUtils.isNullOrEmpty("  	 "));
	}
}
