package com.rds.judicial.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.model.RdsJudicialExceptionTypeModel;
import com.rds.judicial.service.RdsJudicialExceptionTypeService;

@RequestMapping("judicial/exceptiontype")
@Controller
public class RdsJudicialExceptionTypeController {

	@Autowired
	private RdsJudicialExceptionTypeService tService;
	
	@RequestMapping("save")
	@ResponseBody
	public boolean save(@RequestBody Map<String, Object> params){
		
		RdsJudicialExceptionTypeModel tModel = new RdsJudicialExceptionTypeModel();
		tModel.setType_desc(params.get("type_desc").toString());
		return tService.addExceptionType(tModel);
	}
	
	@RequestMapping("update")
	@ResponseBody
	public boolean update(@RequestBody Map<String, Object> params){
		
		RdsJudicialExceptionTypeModel tModel = new RdsJudicialExceptionTypeModel();
		tModel.setType_id(params.get("type_id").toString());
		tModel.setType_desc(params.get("type_desc").toString());
		return tService.updateExceptionType(tModel);
	}
	
	@RequestMapping("getType")
	@ResponseBody
	public Map<String, Object> getType(@RequestBody Map<String, Object> params){
		return tService.getType(params);
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public boolean delete(@RequestBody Map<String, Object> params){
		return tService.deleteExceptionType(params);
	}
}
