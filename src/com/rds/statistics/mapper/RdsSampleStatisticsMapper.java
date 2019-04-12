package com.rds.statistics.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author yxb
 * @className
 * @description
 * @date 2017/8/15
 */
public interface RdsSampleStatisticsMapper {
	List<Object> queryAll(Map<String, Object> params);

	int queryAllCount(Map<String, Object> params);

	int sampleStatistisc(String sys_date);
	
	List<Object> queryAllSample(Map<String, Object> params);
	
	int queryAllSampleCount(Map<String, Object> params);
	
}
