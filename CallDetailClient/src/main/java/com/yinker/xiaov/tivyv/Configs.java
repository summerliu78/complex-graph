package com.yinker.xiaov.tivyv;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configs {
	private static Map<String, String> configMap = new HashMap<String, String>();
	private static boolean initialized = false;
	private static Logger logger = Logger.getLogger(Configs.class);

	/**
	 * Get config value from config file: config.properties
	 * @param key	Config key
	 * @return	Corresponding config value
	 */
	public static String getConfig(String key){
		if(!initialized){
			new Configs();
			initialized = true;
		}
		return configMap.get(key);
	}
	
	private Configs(){
		//Initialize properties from config file
		Properties properties = new Properties();
		try {
			String basePath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getParentFile().getAbsolutePath();
			System.out.println("basepath="+basePath);
			String configPath = basePath + File.separator + "conf" + File.separator + "config.properties";
			System.out.println("configPath="+configPath);
			logger.info("configPath: " + configPath);
			InputStream in = new FileInputStream(configPath);
			properties.load(in);
			Iterator it = properties.keySet().iterator();
			while(it.hasNext()){
				String k = it.next().toString();
				configMap.put(k, properties.get(k).toString().trim());
			}
		} catch (Exception e) {
		}
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println(Configs.getConfig("EnvIDs"));
		boolean sendICMAlert = true;
		System.out.println(sendICMAlert);
	}

}
