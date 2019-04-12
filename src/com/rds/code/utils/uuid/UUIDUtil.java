package com.rds.code.utils.uuid;

import java.util.UUID;

/**
 * 
* @ClassName: UUIDUtil 
* @Description: descrition UUID生成类
* @author chen wei
* @date 2015年3月16日 下午9:38:45 
*
 */
public class UUIDUtil {

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
