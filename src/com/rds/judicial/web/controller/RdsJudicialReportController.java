package com.rds.judicial.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.judicial.model.RdsJudicialCaseReportModel;
import com.rds.judicial.model.RdsJudicialHeadReportModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialReportService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("judicial/report")
public class RdsJudicialReportController {

	@Autowired
	private RdsJudicialReportService RdsJudicialReportService;

	@RequestMapping("getCaseInfo")
	@ResponseBody
	public RdsJudicialResponse getCaseInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String sql_str = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			sql_str = user.getSql_str();
		}
		params.put("sql_str", sql_str);
		return RdsJudicialReportService.getCaseInfo(params);
	}

	@RequestMapping("getCaseReport")
	@ResponseBody
	public List<RdsJudicialCaseReportModel> getCaseReport(
			@RequestBody Map<String, Object> params) {
		return RdsJudicialReportService.getCaseReport(params);
	}

	@RequestMapping("updateReport")
	public void updateReport(@RequestParam MultipartFile[] files,
			RdsJudicialHeadReportModel headReportModel, HttpSession session,
			HttpServletResponse response) {
		String case_in_per = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			case_in_per = user.getUserid();
		}
		boolean is_result= false;
		if(StringUtils.isEmpty(case_in_per)){
			is_result= false;
		}else{
			headReportModel.setUpload_per(case_in_per);
			is_result = RdsJudicialReportService.updateReport(
					headReportModel, files);
		}
		try {
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(is_result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("downloadReport")
	public void downloadReport(String filepath, HttpServletResponse response) {
		RdsJudicialReportService.downloadReport(filepath, response);
	}
}
