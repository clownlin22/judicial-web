package com.rds.judicial.web.controller;

import java.io.File;
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
import com.rds.judicial.service.RdsJudicialCaseStateService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("judicial/caseState")
public class RdsJudicialCaseStateController extends RdsJudicialAbstractController {
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String USER_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "user_permit");
	@Setter
	@Autowired
	private RdsJudicialCaseStateService rdsJudicialCaseStateService;

	@Override
	public Integer insert(Map<String, Object> params) throws Exception {
		return 0;
	}

	@Override
	public Integer update(Map<String, Object> params) throws Exception {
		return 0;
	}

	@Override
	public Object delete(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryModel(@RequestBody Map<String, Object> params) throws Exception {
		return rdsJudicialCaseStateService.queryModel(params);
	}

	@Override
	public Object queryAll(@RequestBody Map<String, Object> params)
			throws Exception {
		return null;
		// return rdsJudicialAgentService.queryAll(params);
	}

	@Override
	public Object queryAllPage(@RequestBody Map<String, Object> params) throws Exception {
		return null;
	}
	
	@RequestMapping("querypage")
	@ResponseBody
	public Object queryPage(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
		params.put("usertype", user.getUsertype());
		if(!USER_PERMIT.contains(user.getUsercode()))
		{
			params.put("userid", user.getUserid());
		}
		return rdsJudicialCaseStateService.queryAllPage(this.pageSet(params));
	}
	
	@RequestMapping("queryCompareResultCount")
	@ResponseBody
	public boolean queryCompareResultCount(
			@RequestBody Map<String, Object> params) {
		return rdsJudicialCaseStateService.queryCompareResultCount(params) > 0 ? true
				: false;
	}
}
