package com.rds.judicial.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.service.RdsJudicialCaseDailyService;

@Controller
@RequestMapping("judicial/casedaily")
public class RdsJudicialCaseDailyController {

	@Autowired
	private RdsJudicialCaseDailyService dService;
	
	@RequestMapping("getinfo")
	@ResponseBody
	public Map<String, Object> getinfo(){
		return dService.getInfo();
	}
	
	@RequestMapping("getAllinfo")
	@ResponseBody
	public Map<String, Object> getAllinfo(@RequestBody Map<String, Object> params){
		return dService.getAllinfo(params);
	}
	
	@RequestMapping("getdaysstatistics")
	@ResponseBody
	public Map<String, Object> getDaysStatistics(@RequestBody Map<String, Object> params){
		return dService.getDaysStatistics(params);
	}
	
	@RequestMapping("getNowinfo")
	@ResponseBody
	public  Map<String, Object> getNowinfo(@RequestBody Map<String, Object> params){
		return dService.getNowinfo(params);
	}
	
	@RequestMapping("getPerformance")
	@ResponseBody
	public Map<String, Object> getPerformance(@RequestBody Map<String, Object> params){
		return dService.getPerformance(params);
	}
	
	@RequestMapping("getCompany")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getCompany(HttpSession session){
		return dService.getCompany(session);
	}
}
