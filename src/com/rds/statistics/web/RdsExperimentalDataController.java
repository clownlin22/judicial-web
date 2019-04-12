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

import com.rds.statistics.mapper.RdsExperimentalDataMapper;
import com.rds.statistics.service.RdsExperimentalDataService;
import com.rds.upc.model.RdsUpcUserModel;



@Controller
@RequestMapping("statistics/experimental")
public class RdsExperimentalDataController {
	@Setter
	@Autowired
	private RdsExperimentalDataService rdsExperimentalDataService;
	
	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		//params.put("hpv_endtime",params.get("hpv_endtime").toString()+" 23:59:59");
		return rdsExperimentalDataService.queryAllPage(params);
	}

	/**
	 * 导出实验数据
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exportexperimentaldata")
	public void export(HttpServletResponse response, HttpServletRequest request , HttpSession session) {
		try {
			
			Map<String, Object> params = new HashMap<String, Object>();
			String caseCode = request.getParameter("case_code");
			params.put("case_code", caseCode);
			String sample_codes = request.getParameter("sample_codes");
			params.put("sample_codes", sample_codes);
			String experiment_no =request.getParameter("experiment_no");
			params.put("experiment_no", experiment_no);
			String mail_phone=request.getParameter("mail_phone");
			params.put("mail_phone", mail_phone);
			params.put("accpet_start_time", request.getParameter("accpet_start_time"));
			params.put("accpet_end_time", request.getParameter("accpet_end_time"));
			params.put("consignment_start_time", request.getParameter("consignment_start_time"));
			params.put("consignment_end_time", request.getParameter("consignment_end_time"));
			
			RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			rdsExperimentalDataService.exportExperimentalData(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
