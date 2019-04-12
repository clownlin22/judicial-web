package com.rds.code.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface RdsBaseInerfaceController {
	
	@RequestMapping("insert")
	@ResponseBody
	public Integer insert(Map<String, Object> params)  throws Exception;
	
	@RequestMapping("update")
	@ResponseBody
	public Integer update(Map<String, Object> params)  throws Exception;
	
	@RequestMapping("save")
	@ResponseBody
	public Object save(Map<String, Object> params);
	
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(Map<String, Object> params);
	
	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(Map<String, Object> params) throws Exception;
	
	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(Map<String, Object> params) throws Exception;
	
	@RequestMapping("querymodel")
	@ResponseBody
	public Object queryModel(Map<String, Object> params) throws Exception;

}
