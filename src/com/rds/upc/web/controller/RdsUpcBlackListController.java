package com.rds.upc.web.controller;

import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.upc.service.RdsUpcBlackListService;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("upc/blackList")
public class RdsUpcBlackListController extends RdsUpcAbstractController {
	
	@Setter
	@Autowired
	private RdsUpcBlackListService rdsUpcBlackListService;
	
	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params) throws Exception{
		return 0 ;
//		return rdsUpcBlackListService.queryAll(params);
	}
	
	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params) throws Exception{
		params = this.pageSet(params);
		return rdsUpcBlackListService.queryAllPage(params);
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params){
		try {
			rdsUpcBlackListService.delete(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
		
	}
	
	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params){
		Object id = params.get("id");
		if(id==null||id.equals("")){
			return insert(params);
		}else {
			return update(params);
		}
	}
	
	private Object insert(Map<String, Object> params){
		try {
			params.put("id", RdsUpcUUIDUtil.getUUID());
			int result = rdsUpcBlackListService.insert(params);
			if (result > 0) {
				return this.setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.INSERT_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
					+ "\n" + e.getMessage());
		}
	}
	
	private Object update(Map<String, Object> params){
		try {
			int result = rdsUpcBlackListService.update(params);
			if (result > 0) {
				return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

}
