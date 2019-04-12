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

import com.rds.judicial.model.RdsJudicialExperimentModel;
import com.rds.judicial.model.RdsJudicialParamsModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialAllCaseInfoService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("judicial/allcaseinfo")
public class RdsJudicialAllCaseInfoController {

	@Autowired
	private RdsJudicialAllCaseInfoService aciService;

	@RequestMapping("getallcaseinfo")
	@ResponseBody
	public Map<String, Object> getAllCaseInfo(
			@RequestBody Map<String, Object> params) {
		params = aciService.getAllCaseInfo(params);
		return params;
	}

	@RequestMapping("exportSampleInfo")
	public void exportSampleInfo(RdsJudicialParamsModel params,
			HttpServletResponse response, HttpSession session) {
		String sql_str = "";
		String roleType = "";
		String userCode = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			sql_str = user.getSql_str();
			roleType = user.getRoletype();
			userCode = user.getUsercode();
		}
		if (!"".equals(userCode))
			// params.setSql_str(sql_str);
			// if (FINANCE_PERMIT.contains(roleType)
			// || FINANCE_PERMIT.contains(userCode))
			aciService.exportSampleInfo(params, response);
		// else
		// aciService.exportCaseInfoNoFinance(params, response);
	}

	@RequestMapping("queryPlaceBySamplecode")
	@ResponseBody
	public List<RdsJudicialExperimentModel> queryPlaceBySamplecode(
			@RequestBody Map<String, Object> params) {
		return aciService.queryPlaceBySamplecode(params);
	}

	@RequestMapping("exportCaseInfo")
	public void exportCaseInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if (null != user) {
			Map<String, Object> params = new HashMap<String, Object>();
			String case_code = request.getParameter("case_code");
			params.put("case_code", case_code);
			String case_name = request.getParameter("case_name");
			String serial_number = request.getParameter("serial_number");
			params.put("serial_number", serial_number);
			params.put("case_name", case_name);
			String starttime = request.getParameter("starttime");
			params.put("starttime", starttime);
			String endtime = request.getParameter("endtime");
			params.put("endtime", endtime);
			String update_date_start = request
					.getParameter("update_date_start");
			params.put("update_date_start", update_date_start);
			String update_date_end = request.getParameter("update_date_end");
			params.put("update_date_end", update_date_end);
			String confirm_date_start = request
					.getParameter("confirm_date_start");
			params.put("confirm_date_start", confirm_date_start);
			String confirm_date_end = request.getParameter("confirm_date_end");
			params.put("confirm_date_end", confirm_date_end);
			String fee_type = request.getParameter("fee_type");
			params.put("fee_type", fee_type);
			String fee_status = request.getParameter("fee_status");
			params.put("fee_status", fee_status);
			String mail_code = request.getParameter("mail_code");
			params.put("mail_code", mail_code);
			String account = request.getParameter("account");
			params.put("account", account);
			String receiver_area = request.getParameter("receiver_area");
			params.put("receiver_area", receiver_area);
			String report_model = request.getParameter("report_model");
			params.put("report_model", report_model);
			String parnter_name = request.getParameter("parnter_name");
			params.put("parnter_name", parnter_name);
			if (user.getParnter_name() != null) {
				params.put("parnter_name", user.getParnter_name());
			}
			String source_type = request.getParameter("source_type");
			params.put("source_type", source_type);
			String sample_in_per = request.getParameter("sample_in_per");
			params.put("sample_in_per", sample_in_per);
			String userCode = "";
			if (session.getAttribute("user") != null) {
				userCode = user.getUsercode();
			}
			if (user.getReceiver_area() != null)
				params.put("receiver_area_provice", user.getReceiver_area());
			if (!"".equals(userCode)) {
				params.put("userCode", userCode);
				aciService.exportCaseInfo(params, response);
			}
		}
	}

	@RequestMapping("queryExportCaseInfo")
	@ResponseBody
	public Map<String, Object> queryExportCaseInfo(
			@RequestBody Map<String, Object> params,
			HttpServletResponse response, HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if (null != user) {
			if (user.getParnter_name() != null) {
				params.put("parnter_name", user.getParnter_name());
			}
			if (user.getReceiver_area() != null)
				params.put("receiver_area_provice", user.getReceiver_area());
			params = aciService.queryExportCaseInfo(params);
			return params;
		} else {
			return null;
		}
	}

	@RequestMapping("exportPartnerAllCaseInfo")
	@ResponseBody
	public void exportPartnerAllCaseInfo(HttpServletResponse response,
			HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		Map<String, Object> params = new HashMap<>();
		if (null != user) {
			if (user.getParnter_name() != null) {
				params.put("parnter_name", user.getParnter_name());
			}
			if (user.getReceiver_area() != null)
				params.put("receiver_area_provice", user.getReceiver_area());
			aciService.exportPartnerAllCaseInfo(params, response);
		}
	}

	/**
	 * 获取案例的样本信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getExceptionSampleInfo")
	@ResponseBody
	public RdsJudicialResponse getExceptionSampleInfo(
			@RequestBody Map<String, Object> params) {
		return aciService.getSampleInfo(params);
	}

	@RequestMapping("exportMessageCaseInfo")
	public void exportMessageCaseInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if (null != user) {
			Map<String, Object> params = new HashMap<String, Object>();
			String starttime = request.getParameter("starttime");
			params.put("starttime", starttime);
			String endtime = request.getParameter("endtime");
			params.put("endtime", endtime);
			String parnter_name = request.getParameter("parnter_name");
			params.put("parnter_name", parnter_name);
			if (user.getParnter_name() != null) {
				params.put("parnter_name", user.getParnter_name());
			}
			aciService.exportMessageCaseInfo(params, response);
		}
	}

	@RequestMapping("queryFMChild")
	@ResponseBody
	public Map<String, Object> queryFMChild(
			@RequestBody Map<String, Object> params) {
		params = aciService.queryFMChild(params);
		return params;
	}

	@RequestMapping("exportFMChild")
	public void exportFMChild(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> params = new HashMap<String, Object>();
		String accept_time_start = request.getParameter("accept_time_start");
		params.put("accept_time_start", accept_time_start);
		String accept_time_end = request.getParameter("accept_time_end");
		params.put("accept_time_end", accept_time_end);
		String consignment_time_start = request
				.getParameter("consignment_time_start");
		params.put("consignment_time_start", consignment_time_start);
		String consignment_time_end = request
				.getParameter("consignment_time_end");
		params.put("consignment_time_end", consignment_time_end);
		String case_code = request.getParameter("case_code");
		params.put("case_code", case_code);
		String fm = request.getParameter("fm");
		params.put("fm", fm);
		String child = request.getParameter("child");
		params.put("child", child);
		aciService.exportFMChild(params, response);
	}

	@RequestMapping("queryExperimentInfo")
	@ResponseBody
	public Map<String, Object> queryExperimentInfo(
			@RequestBody Map<String, Object> params) {
		params = aciService.queryExperimentInfo(params);
		return params;
	}

	@RequestMapping("exportExperimentInfo")
	public void exportExperimentInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> params = new HashMap<String, Object>();
		String accept_time_start = request.getParameter("accept_time_start");
		params.put("accept_time_start", accept_time_start);
		String accept_time_end = request.getParameter("accept_time_end");
		params.put("accept_time_end", accept_time_end);
		String case_code = request.getParameter("case_code");
		params.put("case_code", case_code);
		String parnter_name = request.getParameter("parnter_name");
		params.put("parnter_name", parnter_name);
		aciService.exportExperimentInfo(params, response);
	}

	@RequestMapping("queryConfirmTimePage")
	@ResponseBody
	public Map<String, Object> queryConfirmTimePage(
			@RequestBody Map<String, Object> params) {
		params = aciService.queryConfirmTimePage(params);
		return params;
	}

	@RequestMapping("exportConfirmTimeInfo")
	public void exportConfirmTimeInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> params = new HashMap<String, Object>();
		String starttime = request.getParameter("starttime");
		params.put("starttime", starttime);
		String endtime = request.getParameter("endtime");
		params.put("endtime", endtime);
		String case_code = request.getParameter("case_code");
		params.put("case_code", case_code);
		String username = request.getParameter("username");
		params.put("username", username);
		String report_model = request.getParameter("report_model");
		params.put("report_model", report_model);
		String verify_state = request.getParameter("verify_state");
		params.put("verify_state", verify_state);
		aciService.exportConfirmTimeInfo(params, response);
	}

}
