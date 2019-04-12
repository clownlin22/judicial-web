package com.rds.alcohol.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.model.RdsAlcoholVerifyInfoModel;
import com.rds.alcohol.service.RdsAlcoholVerifyService;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.upc.model.RdsUpcUserModel;

@RequestMapping("alcohol/verify")
@Controller
public class RdsAlcoholVerifyController {
	@Autowired
	private RdsAlcoholVerifyService rdsAlcoholVerifyService;

	@RequestMapping("getVerifyCaseInfo")
	@ResponseBody
	public RdsAlcoholResponse getVerifyCaseInfo(
			@RequestBody Map<String, Object> params,HttpSession session) {
		String sql_str="";
		if(session.getAttribute("user")!=null){
			RdsUpcUserModel user=(RdsUpcUserModel)session.getAttribute("user");
			sql_str=user.getSql_str2();
		}
		params.put("sql_str", sql_str);
		return rdsAlcoholVerifyService.getVerifyCaseInfo(params);
	}

	@RequestMapping("verifyCaseInfo")
	@ResponseBody
	public boolean verifyCaseInfo(@RequestBody Map<String, Object> params,
			HttpSession session) {
		params.put("verify_id", UUIDUtil.getUUID());
		String case_in_per = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			case_in_per = user.getUserid();
		}
		params.put("verify_per", case_in_per);
		if(StringUtils.isEmpty(case_in_per)){
			return false;
		}
		return rdsAlcoholVerifyService.verifyCaseInfo(params);
	}
	
	@RequestMapping("getVerifyInfo")
	@ResponseBody
	public List<RdsAlcoholVerifyInfoModel> getVerifyInfo(@RequestBody Map<String, Object> params){
		return rdsAlcoholVerifyService.getVerifyInfo(params);
	}
}
