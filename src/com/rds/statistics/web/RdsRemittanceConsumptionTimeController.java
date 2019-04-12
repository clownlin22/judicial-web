package com.rds.statistics.web;

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

import com.rds.statistics.service.RdsRemittanceConsumptionTimeService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("statistics/Consumption")
public class RdsRemittanceConsumptionTimeController {
	
	@Setter
	@Autowired
	private RdsRemittanceConsumptionTimeService RdsRemittanceConsumptionTimeService;
	
	@RequestMapping("queryConsumptionTimeAll")
	@ResponseBody
	public Map<String, Object> queryConsumptionTimeAll(@RequestBody Map<String, Object> params,HttpSession session){

		return RdsRemittanceConsumptionTimeService.queryConsumptionTimeAll(params);
	}
	
	@RequestMapping("consumptionTimeExport")
	@ResponseBody
	public void exportInvasiveInfo(HttpServletResponse response,
			HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String case_code = request.getParameter("case_code");
			params.put("case_code", case_code);
			params.put("starttime_accept",
					request.getParameter("starttime_accept"));
			params.put("endtime_accept",
					request.getParameter("endtime_accept"));
			String confirm_state = request.getParameter("confirm_state");
			params.put("confirm_state", confirm_state);
			String remittance_num = request.getParameter("remittance_num");
			params.put("remittance_num", remittance_num);
			params.put("case_state",
					request.getParameter("case_state"));
			params.put("case_receiver",
					request.getParameter("case_receiver"));
			params.put("time_spent",
					request.getParameter("time_spent"));
//			RdsUpcUserModel user = (RdsUpcUserModel) session
//					.getAttribute("user");
//			params.put("roleType", user.getRoletype());
//			params.put("userCode", user.getUsercode());
			RdsRemittanceConsumptionTimeService.export(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
