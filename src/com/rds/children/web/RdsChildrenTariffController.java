package com.rds.children.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.children.service.RdsChildrenTariffService;

/**
 * 儿童基因套餐
 * @author 少明
 *
 */
@Controller
@RequestMapping("children/tariff/")
public class RdsChildrenTariffController {
	
	@Autowired
	private RdsChildrenTariffService tservice;
	
	/**
	 * 获取套餐信息
	 * @param params
	 * @return
	 */
	@RequestMapping("gettariffinfo")
	@ResponseBody
	public Map<String, Object> getTariffInfo(@RequestBody Map<String, Object> params){
		
		return tservice.getTariffInfo(params);
	}
	
	/**
	 * 新增套餐
	 * @param params
	 * @return
	 */
	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> params){
		return tservice.save(params);
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestBody Map<String, Object> params){
		return tservice.delete(params);
	}
}
