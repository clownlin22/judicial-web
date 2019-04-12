package com.rds.upc.web.common;

import java.util.UUID;

/**
 * descrition UUID生成类
 * 
 * @author tyhjao@163.com
 * @version 1.0
 * @date 2009-5-22
 */
public class RdsUpcUUIDUtil {

	/**
	 * 生成UUID
	 * 
	 * @return String
	 */
	public final static String getUUID() {
		String strUUID = UUID.randomUUID().toString().replaceAll("\\-", "");
		return strUUID;
	}
}
