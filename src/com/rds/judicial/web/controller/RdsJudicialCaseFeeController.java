package com.rds.judicial.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialCaseFeeService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("judicial/casefee")
public class RdsJudicialCaseFeeController {

	@Setter
	@Autowired
	private RdsJudicialCaseFeeService RdsJudicialCaseFeeService;


	@RequestMapping("getCaseFeeInfo")
	@ResponseBody
	public RdsJudicialResponse getCaseFeeInfo(
			@RequestBody Map<String, Object> params) {
		return RdsJudicialCaseFeeService.getCaseFeeInfo(params);
	}

	@RequestMapping("saveCaseFee")
	@ResponseBody
	public boolean saveCaseFee(@RequestBody Map<String, Object> params) {
		return RdsJudicialCaseFeeService.saveCaseFee(params);
	}
	
	@RequestMapping("caseFeeConfirm")
	@ResponseBody
	public boolean caseFeeConfirm(@RequestBody Map<String, Object> params,HttpSession session) {
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("update_user", user.getUserid());
        }else{
        	return false;
        }
		return RdsJudicialCaseFeeService.caseFeeConfirm(params);
	}
	
	@RequestMapping("insertOAnum")
	@ResponseBody
	public boolean insertOAnum(@RequestBody Map<String, Object> params){
		return RdsJudicialCaseFeeService.insertOAnum(params);
	}
	
	/**
	 * 导出财务信息
	 * @param response
	 * @param request
	 */
	@RequestMapping("exportCaseInfoOther")
	public void exportCaseInfoOther(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String case_code = request.getParameter("case_code");
			params.put("case_code", case_code);
			String areaname = request.getParameter("areaname");
			params.put("areaname", areaname);
			String type_state = request.getParameter("type_state");
			params.put("type_state", type_state);
			params.put("case_type", request.getParameter("case_type"));
			params.put("fee_state", request.getParameter("fee_state"));
			params.put("receiver", request.getParameter("receiver"));
			params.put("client",request.getParameter("client"));
			params.put("starttime", request.getParameter("starttime"));
			params.put("endtime",request.getParameter("endtime"));
			params.put("paragraphtime_start", request.getParameter("paragraphtime_start"));
			params.put("paragraphtime_end",request.getParameter("paragraphtime_end"));
			String account = request.getParameter("account");
			params.put("account", account);
			RdsJudicialCaseFeeService.exportCaseInfoOther(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
