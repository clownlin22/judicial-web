package com.rds.judicial.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.model.RdsJudicialCaseExceptionInfo;
import com.rds.judicial.model.RdsJudicialCaseExceptionModel;
import com.rds.judicial.model.RdsJudicialDicValuesModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialCaseExceptionService;
import com.rds.upc.model.RdsUpcUserModel;

@RequestMapping("judicial/caseException")
@Controller
public class RdsJudicialCaseExceptionController {

	@Autowired
	private RdsJudicialCaseExceptionService RdsJudicialCaseExceptionService;
	
	@RequestMapping("getCaseException")
	@ResponseBody
	private RdsJudicialResponse getCaseException(@RequestBody Map<String,Object> params,HttpSession session){
//		if (session.getAttribute("user") != null) {
//			RdsUpcUserModel user = (RdsUpcUserModel) session
//					.getAttribute("user");
//			params.put("user_type", user.getUsertype());
//			params.put("userid", user.getUserid());
//        }
		 return RdsJudicialCaseExceptionService.getCaseException(params);
	}
	
	@RequestMapping("getOtherException")
	@ResponseBody
	private List<RdsJudicialCaseExceptionModel> getOtherException(@RequestBody Map<String,Object> params){
		return RdsJudicialCaseExceptionService.getOtherException(params);
	}
	
	@RequestMapping("saveExceptionInfo")
	@ResponseBody
	private boolean saveExceptionInfo(@RequestBody Map<String,Object> params,HttpSession session){
		String exception_per = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			exception_per = user.getUserid();
        }
		params.put("exception_per", exception_per);
		return RdsJudicialCaseExceptionService.saveExceptionInfo(params);
	}
	
	@RequestMapping("getExceptionTypes")
	@ResponseBody
	private List<RdsJudicialDicValuesModel> getExceptionTypes(){
		return RdsJudicialCaseExceptionService.getExceptionTypes();
	}
	
	@RequestMapping("deleteExceptionInfo")
	@ResponseBody
	private boolean deleteExceptionInfo(@RequestBody Map<String,Object> params){
		return RdsJudicialCaseExceptionService.deleteExceptionInfo(params);
	}
	
	@RequestMapping("handleExceptionInfo")
	@ResponseBody
	private boolean handleExceptionInfo(@RequestBody Map<String,Object> params){
		return RdsJudicialCaseExceptionService.handleExceptionInfo(params);
	}
	
	@RequestMapping("updateExceptionInfo")
	@ResponseBody
	private boolean updateExceptionInfo(@RequestBody Map<String,Object> params){
		return RdsJudicialCaseExceptionService.updateExceptionInfo(params);
	}

	/**
	 * 查看案例详细信息
	 * @param params
	 * @return
	 */
	@RequestMapping("getCaseInfo")
	@ResponseBody
	private RdsJudicialCaseExceptionInfo getCaseInfo(@RequestBody Map<String,Object> params){
		 return RdsJudicialCaseExceptionService.getCaseInfo(params);
	}
	
	@RequestMapping("getExportException")
	@ResponseBody
	private RdsJudicialResponse getExportException(@RequestBody Map<String,Object> params){
		return RdsJudicialCaseExceptionService.getExportException(params);
	}
	
	
	@RequestMapping("exportException")
	public void exportException(HttpServletResponse response, HttpServletRequest request ,
			HttpSession session) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("starttime", request.getParameter("starttime"));
		params.put("endtime", request.getParameter("endtime"));
		RdsJudicialCaseExceptionService.exportCaseException(params, response);
	}
}
