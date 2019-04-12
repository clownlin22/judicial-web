package com.rds.alcohol.web;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.service.RdsAlcoholExperimentService;
import com.rds.judicial.web.common.PageUtil;

/**
 * @description 酒精实验
 * @author fushaoming 2015年6月8日
 */
@Controller
@RequestMapping("alcohol/experiment")
public class RdsAlcoholExperimentController {

	@Setter
	@Autowired
	private RdsAlcoholExperimentService experService;

	/**
	 * 获取可实验案例
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("querycaseinfo")
	@ResponseBody
	public RdsAlcoholResponse queryCaseInfo(
			@RequestBody Map<String, Object> params) throws Exception {
		return experService.queryCaseInfo(PageUtil.pageSet(params));
	}

	/**
	 * 生成校验曲线
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("addRegression")
	@ResponseBody
	public Map<String, Object> addRegression(
			@RequestBody Map<String, Object> params) {
		return experService.addRegression(params);
	}
	/**
	 * 新增实验
	 * @param params
	 * @return
	 */
	@RequestMapping("addExperiment")
	@ResponseBody
	public Map<String, Object> addExperiment(@RequestBody Map<String, Object> params){
		return experService.addExperiment(params);
	}
	/**
	 * 新增实验
	 * @param params
	 * @return
	 */
	@RequestMapping("isregpastdue")
	@ResponseBody
	public Map<String, Object> isRegPastDue(@RequestBody Map<String, Object> params){
		return experService.isRegPastDue(params);
	}
	/**
	 * 查询实验明细
	 * @param params
	 * @return
	 */
	@RequestMapping("queryExperDetail")
	@ResponseBody
	public Map<String, Object> queryExperDetail(@RequestBody Map<String, Object> params){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", experService.queryExperDetail(params));
		return map;
	}
	
	/**
	 * 作废实验数据
	 * @param params
	 * @return
	 */
	@RequestMapping("deleteExper")
	@ResponseBody
	public Map<String, Object> deleteExper(@RequestBody Map<String, Object> params){
		return experService.deleteExper(params);
	}
}
