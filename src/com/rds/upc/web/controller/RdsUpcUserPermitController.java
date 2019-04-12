package com.rds.upc.web.controller;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.upc.service.RdsUpcUserPermitService;

@Controller
@RequestMapping("upc/userpermit")
public class RdsUpcUserPermitController extends RdsUpcAbstractController {
	
	@Setter
	@Autowired
	private RdsUpcUserPermitService rdsUpcUserPermitService;
	
	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params ){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Object json = rdsUpcUserPermitService.queryAll(params);
			result.put("success", true);
			result.put("result", true);
			result.put("json", json);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", true);
			result.put("result", false);
			result.put("msg", "获取用户权限配置失败!\n"+e.getMessage());
			return result;
		}
	}
	
	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params){
		
		try {
			rdsUpcUserPermitService.insert(params);
			return this.setModel(true, "保存成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, "保存失败\n"+e.getMessage());
		}
	}
	
	

}
