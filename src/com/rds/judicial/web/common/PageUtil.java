package com.rds.judicial.web.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 页码设置工具
 * @author ThinK 2015年4月16日
 */
public class PageUtil {
	public static Map<String, Object> pageSet(Map<String, Object> map)
			throws Exception {
		if (map == null) {
			throw new Exception("分页参数为空。");
		}
		Integer start = (Integer) map.get("start");
		Integer limit = (Integer) map.get("limit");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("start", start);
		result.put("end", limit);
		map.putAll(result);
		return map;
	}
}
