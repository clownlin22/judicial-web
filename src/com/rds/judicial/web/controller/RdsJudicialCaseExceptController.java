package com.rds.judicial.web.controller;

import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.service.RdsJudicialCaseExceptService;

@Controller
@RequestMapping("judicial/caseStatus")
public class RdsJudicialCaseExceptController {
	
	@Setter
	@Autowired
	private RdsJudicialCaseExceptService ceService;
	
	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> params){
		return ceService.save(params);
	}
	
	@RequestMapping("queryAll")
	@ResponseBody
	public Map<String, Object> queryAll(@RequestBody Map<String, Object> params){
		return ceService.queryAll(params);
	}
	
	@RequestMapping("setNormal")
	@ResponseBody
	public Map<String, Object> setNormal(@RequestBody Map<String, Object> params){
		return ceService.setNormal(params);
	}
}
