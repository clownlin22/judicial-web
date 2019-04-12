package com.rds.statistics.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yxb
 * @className
 * @description
 * @date 2017/8/15
 */
public interface RdsStatisticsWagesMapper {
	List<Object> queryAllPage(Map<String, Object> params);

	int queryAllCount(Map<String, Object> params);

	boolean insertWages(Map<String, Object> params);
	
	List<Object> queryAll(Map<String, Object> params);
	
	boolean deleteWages(Map<String, Object> params);
	
	boolean insertWagesAttachment(Map<String, Object> params);
	
	boolean updateWagesAttachment(Map<String, Object> params);
	
	List<Object> queryWagesAttachment(Map<String, Object> params);
}
