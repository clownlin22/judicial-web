package com.rds.upc.web.controller;

import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.upc.service.RdsUpcCompanyService;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("upc/company")
public class RdsUpcCompanyController extends RdsUpcAbstractController {
	
	@Setter
	@Autowired
	private RdsUpcCompanyService rdsUpcCompanyService;
	
	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params) throws Exception{
		return rdsUpcCompanyService.queryAll(params);
	}
	
	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params) throws Exception{
		params = this.pageSet(params);
		return rdsUpcCompanyService.queryAllPage(params);
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params){
		try {
			rdsUpcCompanyService.delete(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
		
	}
	
	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params){
		Object companyid = params.get("companyid");
		if(companyid==null||companyid.equals("")){
			return insert(params);
		}else {
			return update(params);
		}
	}
	
	private Object insert(Map<String, Object> params){
		try {
			params.put("companyid", RdsUpcUUIDUtil.getUUID());
			int result = rdsUpcCompanyService.insert(params);
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
			int result = rdsUpcCompanyService.update(params);
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

	/**
	 * 验证编号
	 */
	@RequestMapping("verifyId_code")
	@ResponseBody
	public boolean verifyId_code(@RequestBody Map<String, Object> params) {
		return rdsUpcCompanyService.verifyId_code(params);
	}
}
