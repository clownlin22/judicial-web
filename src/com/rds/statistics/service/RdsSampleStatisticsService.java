package com.rds.statistics.service;

import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * @author yxb
 * @className
 * @description
 * @date 2017/8/15
 */
public interface RdsSampleStatisticsService {
	List<Object> queryAll(Map<String, Object> params);

	int queryAllCount(Map<String, Object> params);

	void export(@RequestParam String sample_in_per, @RequestParam String month, @RequestParam String deptname,
			HttpServletResponse httpResponse);

	int sampleStatistisc();
	
	List<Object> queryAllSample(Map<String, Object> params);
	
	int queryAllSampleCount(Map<String, Object> params);
	
	void exportSample(@RequestParam String sample_in_per_id, @RequestParam String month, 
			HttpServletResponse httpResponse);
}
