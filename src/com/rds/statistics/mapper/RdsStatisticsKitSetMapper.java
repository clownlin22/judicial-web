package com.rds.statistics.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yxb
 * @className
 * @description
 * @date 2018/3/22
 */
public interface RdsStatisticsKitSetMapper {
	List<Object> queryAllPage(Map<String, Object> params);

	int queryAllCount(Map<String, Object> params);

	boolean insertKitSet(Map<String, Object> params);

	boolean updateKitSet(Map<String, Object> params);
	
	List<Object> queryAll(Map<String, Object> params);
	
	boolean deleteKitSet(Map<String, Object> params);
	
	boolean comfirmKitSet(Map<String, Object> params);
	
	List<String> queryDeptList();
	
}
