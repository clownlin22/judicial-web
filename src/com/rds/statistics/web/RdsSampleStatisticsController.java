package com.rds.statistics.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.statistics.service.RdsSampleStatisticsService;

/**
 * @author yxb
 * @className
 * @description
 * @date 2017/8/16
 */
@Controller
@RequestMapping("statistics/sampleStatistics")
public class RdsSampleStatisticsController {

	@Autowired
	private RdsSampleStatisticsService rdsSampleStatisticsService;

	@RequestMapping("query")
	@ResponseBody
	public Object query(@RequestBody Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsSampleStatisticsService.queryAll(params));
		map.put("total", rdsSampleStatisticsService.queryAllCount(params));
		return map;
	}

	@RequestMapping("querySample")
	@ResponseBody
	public Object querySample(@RequestBody Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsSampleStatisticsService.queryAllSample(params));
		map.put("total", rdsSampleStatisticsService.queryAllSampleCount(params));
		return map;
	}

	
	@RequestMapping("exportInfo")
	@ResponseBody
	public void exportInfo(@RequestParam String sample_in_per,
			@RequestParam String month, @RequestParam String deptname,
			HttpServletResponse httpResponse) {
		rdsSampleStatisticsService.export(sample_in_per, month,deptname, httpResponse);
	}
	
	@RequestMapping("exporSampletInfo")
	@ResponseBody
	public void exporSampletInfo(@RequestParam String sample_in_per_id,
			@RequestParam String month, 
			HttpServletResponse httpResponse) {
		rdsSampleStatisticsService.exportSample(sample_in_per_id, month, httpResponse);	}
}
