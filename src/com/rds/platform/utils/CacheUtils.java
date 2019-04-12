package com.rds.platform.utils;

import com.rds.code.utils.syspath.ConfigPath;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CacheUtils {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";
	
	private static CacheUtils cache = null;
	
	private Map<String, String> config = null;
	
	private CacheUtils(){
		config = new HashMap<String,String>();
		
		loadConfig();
	}

	public static synchronized CacheUtils getInstace() {
		if (cache == null) {
			cache = new CacheUtils();
		}
		return cache;
	}
	
	private void loadConfig(){
		try {
			String dir = System.getProperty("user.dir");
			config.put("user.dir", dir);
//			String config_path = dir+File.separator+"config.properties";
			String config_path = FILE_PATH;
			config.put("api", PropertiesUtils.readValue(config_path, "api"));
			config.put("name", PropertiesUtils.readValue(config_path, "name"));
			config.put("pwd", PropertiesUtils.readValue(config_path, "pwd"));
			config.put("sign", PropertiesUtils.readValue(config_path, "sign"));
			config.put("signkey", PropertiesUtils.readValue(config_path, "signkey"));
			config.put("fcs-host", PropertiesUtils.readValue(config_path, "fcs-host"));
			config.put("fcs-port", PropertiesUtils.readValue(config_path, "fcs-port"));
			config.put("feedbackapi", PropertiesUtils.readValue(config_path, "feedbackapi"));
//			Log.log("Load system config successful!");
		} catch (Exception e) {
			e.printStackTrace();
//			Log.log("Load system config field!");
		}
		
		
//		System.out.println("path:"+config_path);
	}
	
	public Map<String, String> getCache(){
		return config;
	}
	
	public void put(String key, String value){
		config.put(key, value);
	}

}