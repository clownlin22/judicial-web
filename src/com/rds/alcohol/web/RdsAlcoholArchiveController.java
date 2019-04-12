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

import com.rds.alcohol.model.RdsAlcoholArchiveModel;
import com.rds.alcohol.model.RdsAlcoholArchiveReadModel;
import com.rds.alcohol.model.RdsAlcoholCaseInfoModel;
import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.service.RdsAlcoholArchiveService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("alcohol/archive")
public class RdsAlcoholArchiveController {
    
	@Autowired
	private RdsAlcoholArchiveService RdsAlcoholArchiveService;
	
	@RequestMapping("getCaseInfo")
	@ResponseBody
	public RdsAlcoholResponse getCaseInfo(@RequestBody Map<String,Object> params,HttpSession session){
		String sql_str="";
		if(session.getAttribute("user")!=null){
			RdsUpcUserModel user=(RdsUpcUserModel)session.getAttribute("user");
			sql_str=user.getSql_str2();
		}
		params.put("sql_str", sql_str);
		return RdsAlcoholArchiveService.getCaseInfo(params);
	}
	
	
	@RequestMapping("addArchiveInfo")
	@ResponseBody
	public boolean addArchiveInfo(@RequestBody Map<String, Object> params,HttpSession session){
		String case_in_per="";
		if(session.getAttribute("user")!=null){
			RdsUpcUserModel user=(RdsUpcUserModel)session.getAttribute("user");
			case_in_per=user.getUserid();
		}
		params.put("archive_per", case_in_per);
		if(StringUtils.isEmpty(case_in_per)){
			return false;
		}
		return RdsAlcoholArchiveService.addArchiveInfo(params);
	}
	
	@RequestMapping("getArchiveInfo")
	@ResponseBody
	public RdsAlcoholResponse getArchiveInfo(@RequestBody Map<String,Object> params){
		return RdsAlcoholArchiveService.getArchiveInfo(params);
	}
	
	@RequestMapping("getReadInfo")
	@ResponseBody
	public List<RdsAlcoholArchiveReadModel> getReadInfo(@RequestBody Map<String,Object> params){
		return RdsAlcoholArchiveService.getReadInfo(params);
	}
	
	@RequestMapping("addReadInfo")
	@ResponseBody
	public boolean addReadInfo(@RequestBody Map<String, Object> params){
		return RdsAlcoholArchiveService.addReadInfo(params);
	}
	
}
