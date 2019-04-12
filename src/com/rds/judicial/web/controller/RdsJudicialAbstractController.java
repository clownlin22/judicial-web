package com.rds.judicial.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.Getter;
import lombok.Setter;

import com.rds.upc.model.RdsUpcMessageModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;

public abstract class RdsJudicialAbstractController {
	
	@Getter
	@Setter
	private String KEY = "id";
	
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
	
	@RequestMapping("insert")
	@ResponseBody
	public abstract Integer insert(@RequestBody Map<String, Object> params)  throws Exception;
	
	@RequestMapping("update")
	@ResponseBody
	public abstract Integer update(@RequestBody Map<String, Object> params)  throws Exception;
	
	@RequestMapping("delete")
	@ResponseBody
	public Object deleteCtrl(@RequestBody Map<String, Object> params){
		try {
			this.delete(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}
	
	public abstract Object delete(Map<String, Object> params) throws Exception;
	
	@RequestMapping("queryall")
	@ResponseBody
	public abstract Object queryAll(@RequestBody Map<String, Object> params) throws Exception;
	
	@RequestMapping("queryallpage")
	@ResponseBody
	public abstract Object queryAllPage(@RequestBody Map<String, Object> params) throws Exception;
	
	@RequestMapping("querymodel")
	@ResponseBody
	public abstract Object queryModel(@RequestBody Map<String, Object> params) throws Exception;
	
	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params,HttpSession session){
		try {
			String createper="";
			if (session.getAttribute("user") != null) {
				RdsUpcUserModel user = (RdsUpcUserModel) session
						.getAttribute("user");
				createper = user.getUserid();
	        }
			params.put("createper",createper);
			int result = 0;
			
			Object key = params.get(getKEY());
			if(key==null||key.equals("")){
				result = insert(params);
			}else {
				result = update(params);
			}
			
			if (result > 0) {
				return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
			}else if(result == -1){
				return this.setModel(false, RdsUpcConstantUtil.REPERT);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
		
	}
	
}
