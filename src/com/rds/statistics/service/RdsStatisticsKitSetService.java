package com.rds.statistics.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * @author yxb
 * @className
 * @description
 * @date 2018/03/22
 */
public interface RdsStatisticsKitSetService {
	List<Object> queryAllPage(Map<String, Object> params);

	int queryAllCount(Map<String, Object> params);

	boolean insertKitSet(Map<String, Object> params);

	boolean deleteKitSet(Map<String, Object> params);

	boolean updateKitSet(Map<String, Object> params);
	
	boolean comfirmKitSet(Map<String, Object> params);
	
	List<String> queryDeptList();
	
	void exportKit(Map<String,Object> params,HttpServletResponse httpResponse);
	
}
