package com.rds.code.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rds.code.utils.model.MailInfo;
import com.rds.code.utils.syspath.ConfigPath;

public class MailUtils {
	   private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";
        public static List<MailInfo> getMailStr(String mail_code,String mail_type){
        	HttpClient httpClient = new DefaultHttpClient();
    	    String mail_url = PropertiesUtils.readValue(FILE_PATH, "mail_url");
        	mail_url = String.format(mail_url, mail_code,
        			mail_type);
			HttpGet httpGet = new HttpGet(mail_url);
			try {
				HttpResponse response = httpClient.execute(httpGet);
				HttpEntity entity = response.getEntity();
				String str = EntityUtils.toString(entity, Charset.forName("UTF-8"));
				JSONObject object = JSONObject.parseObject(str);
				Object obj = object.get("data");
				List<MailInfo> list=JSONArray
						.parseArray(obj.toString(), MailInfo.class);
				return list;
			} catch (Exception e) {
			}
			return Collections.emptyList();
        }
}
