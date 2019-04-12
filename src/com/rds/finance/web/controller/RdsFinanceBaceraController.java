package com.rds.finance.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.finance.service.RdsFinanceBaceraService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("finance/bacera")
public class RdsFinanceBaceraController {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String USER_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsFinanceBaceraService rdsFinanceBaceraService;

	@RequestMapping("insert")
	@ResponseBody
	public boolean insert(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if(null == user.getUserid())
			return false;
		params.put("input_person", user.getUserid());
		return rdsFinanceBaceraService.insert(params);
	}

	@RequestMapping("update")
	@ResponseBody
	public boolean update(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if(null == user.getUserid())
			return false;
		params.put("input_person", user.getUserid());
		return rdsFinanceBaceraService.update(params);
	}
	
	@RequestMapping("confirmCase")
	@ResponseBody
	public boolean confirmCase(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if(null == user.getUserid())
			return false;
		params.put("confirm_per", user.getUserid());
		return rdsFinanceBaceraService.confirmCase(params);
	}
	

	@RequestMapping("delete")
	@ResponseBody
	public boolean delete(@RequestBody Map<String, Object> params) throws Exception {
		return rdsFinanceBaceraService.delete(params);
	}

	@RequestMapping("queryAllPage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (!USER_PERMIT.contains(user.getUsercode()))
			params.put("userid", user.getUserid());
		params = this.pageSet(params);
		return rdsFinanceBaceraService.queryAllPage(params);
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
