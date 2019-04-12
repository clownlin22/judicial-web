package com.rds.upc.web.controller;

import java.util.HashMap;
import java.util.Map;

import com.rds.upc.model.RdsUpcMessageModel;

public abstract class RdsUpcAbstractController {
	
	protected RdsUpcMessageModel setModel(boolean success, boolean result, String message){
		
		RdsUpcMessageModel model = new RdsUpcMessageModel();
		model.setSuccess(success);
		model.setResult(result);
		model.setMessage(message);
		return model;
	}
	
	protected RdsUpcMessageModel setModel(boolean result, String message){
		
		RdsUpcMessageModel model = new RdsUpcMessageModel();
		model.setResult(result);
		model.setMessage(message);
		return model;
	}
	
	protected Map<String, Object> pageSet(Map<String, Object> map) throws Exception{
		if(map == null){
			throw new Exception("分页参数为空。");
		}
		Integer start = (Integer)map.get("start");
		Integer limit = (Integer)map.get("limit");
		map.putAll(this.pageSet(start, limit));
		return map;
	} 
	
	protected Map<String, Object> pageSet(Integer start,Integer limit){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("start", start);
		result.put("end", limit);
		return result;
	}
	
}
