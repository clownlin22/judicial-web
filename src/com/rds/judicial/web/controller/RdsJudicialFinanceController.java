package com.rds.judicial.web.controller;

import java.io.File;
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

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialFinanceService;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * @description 财务管理controller
 * @author ThinK 2015年4月20日
 */
@Controller
@RequestMapping("judicial/finance")
public class RdsJudicialFinanceController {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	@Setter
	@Autowired
	private RdsJudicialFinanceService financeService;

	/**
	 * 财务审核页面、查询审核财务
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("queryVerify")
	@ResponseBody
	public Map<String, Object> queryVerify(@RequestBody Map<String, Object> map) {
		return financeService.queryVerify(map);
	}

	/**
	 * 财务审核页面、审核更新状态
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("updatestatus")
	@ResponseBody
	public Map<String, Object> updateStatus(@RequestBody Map<String, Object> map) {
		return financeService.updatestatus(map);
	}

	/**
	 * 账目查询
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("queryallsum")
	@ResponseBody
	public Map<String, Object> queryAllSum(@RequestBody Map<String, Object> map) {
		return financeService.queryAllSum(map);
	}

	/**
	 * 账目查询、获取明细
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("querydetail")
	@ResponseBody
	public Map<String, Object> queryDetail(@RequestBody Map<String, Object> map) {
		return financeService.queryDetail(map);
	}

	/**
	 * 获取所有月报
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getAllMonthly")
	@ResponseBody
	public Map<String, Object> getAllMonthly(
			@RequestBody Map<String, Object> params, HttpSession session) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
		}
		// 判断自己登录看到自己的日报
		if (!FINANCE_PERMIT.contains(user.getUsercode())) {
			params.put("userid", user.getUserid());
		}
		return financeService.getAllMonthly(params);
	}

	/**
	 * 保存月报审核
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("saveMonthlyVerfiy")
	@ResponseBody
	public Map<String, Object> saveMonthlyVerfiy(
			@RequestBody Map<String, Object> params) {
		return financeService.saveMonthlyVerfiy(params);
	}

	/**
	 * 财务收款
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("saveReturnSum")
	@ResponseBody
	public Map<String, Object> saveReturnSum(
			@RequestBody Map<String, Object> params) {
		return financeService.saveReturnSum(params);
	}

	@RequestMapping("getFinanceDaily")
	@ResponseBody
	public RdsJudicialResponse getFinanceDaily(
			@RequestBody Map<String, Object> params, HttpSession session) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
		}
		// 判断自己登录看到自己的日报
		if (!FINANCE_PERMIT.contains(user.getUsercode())) {
			params.put("userid", user.getUserid());
		}
		return financeService.getFinanceDaily(params);
	}

	@RequestMapping("confirmFinanceDaily")
	@ResponseBody
	public boolean confirmFinanceDaily(@RequestBody Map<String, Object> params) {
		return financeService.confirmFinanceDaily(params);
	}

	@RequestMapping("queryDailyDetail")
	@ResponseBody
	public Map<String, Object> queryDailyDetail(
			@RequestBody Map<String, Object> params, HttpSession session) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
		}
		// 判断自己登录看到自己的日报
		if (!FINANCE_PERMIT.contains(user.getUsercode())) {
			params.put("userid", user.getUserid());
		}
		return financeService.queryDailyDetail(params);
	}

	@RequestMapping("createFinanceDaily")
	@ResponseBody
	public Object createFinanceDaily(@RequestBody Map<String, Object> params,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String case_id = params.get("case_id").toString();
		try {
			if (financeService.createFinanceDaily(case_id) > 0) {
				map.put("result", true);
				map.put("message", "操作成功，请前往汇款!");
			} else {
				map.put("result", false);
				map.put("message", "操作失败，请查看改案例是否已生成日报!");
			}
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "操作异常，请联系管理员!");
			e.printStackTrace();
		}
		return map;
	}

	// 无创汇款手动生成
	@RequestMapping("createPreFinanceDaily")
	@ResponseBody
	public Object createPreFinanceDaily(
			@RequestBody Map<String, Object> params, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String case_id = params.get("case_id").toString();
		try {
			if (financeService.createPreFinanceDaily(case_id) > 0) {
				map.put("result", true);
				map.put("message", "操作成功，请前往汇款!");
			} else {
				map.put("result", false);
				map.put("message", "操作失败，请查看改案例是否已生成日报!");
			}
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "操作异常，请联系管理员!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @param params
	 * @param session
	 * @return
	 */
	@RequestMapping("createChildrenFinanceDaily")
	@ResponseBody
	public Object createChildrenFinanceDaily(
			@RequestBody Map<String, Object> params, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String case_id = params.get("case_id").toString();
		try {
			if (financeService.createChildrenFinanceDaily(case_id) > 0) {
				map.put("result", true);
				map.put("message", "操作成功，请前往汇款!");
			} else {
				map.put("result", false);
				map.put("message", "操作失败，请查看改案例是否已生成日报!");
			}
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "操作异常，请联系管理员!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @param params
	 * @param session
	 * @return
	 */
	@RequestMapping("createContractFinanceDaily")
	@ResponseBody
	public Object createContractFinanceDaily(
			@RequestBody Map<String, Object> params, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String contract_id = params.get("contract_id").toString();
		try {
			if (financeService.createContractFinanceDaily(contract_id) > 0) {
				map.put("result", true);
				map.put("message", "操作成功，请前往汇款!");
			} else {
				map.put("result", false);
				map.put("message", "操作失败，请查看该合同是否已生成日报!");
			}
		} catch (Exception e) {
			map.put("result", false);
			map.put("message", "操作异常，请联系管理员!");
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping("exportCaseFinance")
	@ResponseBody
	public void exportCaseFinance(HttpServletRequest request,
			HttpServletResponse response) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (null == user)
			return;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			// 判断自己登录看到自己的日报
			if (!FINANCE_PERMIT.contains(user.getUsercode())) {
				params.put("userid", user.getUserid());
			}
			String case_code = request.getParameter("case_code");
			params.put("case_code", case_code);
			String starttime = request.getParameter("starttime");
			params.put("starttime", starttime);
			String endtime = request.getParameter("endtime");
			params.put("endtime", endtime);
			String starttime_accept = request.getParameter("starttime_accept");
			params.put("starttime_accept", starttime_accept);
			String endtime_accept = request.getParameter("endtime_accept");
			params.put("endtime_accept", endtime_accept);
			String starttime_client = request.getParameter("starttime_client");
			params.put("starttime_client", starttime_client);
			String endtime_client = request.getParameter("endtime_client");
			params.put("endtime_client", endtime_client);
			String confirm_state = request.getParameter("confirm_state");
			params.put("confirm_state", confirm_state);
			String remittance_type = request.getParameter("remittance_type");
			params.put("remittance_type", remittance_type);
			String settlement_type = request.getParameter("settlement_type");
			params.put("settlement_type", settlement_type);
			String remittance_num = request.getParameter("remittance_num");
			params.put("remittance_num", remittance_num);
			String client = request.getParameter("client");
			params.put("client", client);
			String daily_type = request.getParameter("daily_type");
			params.put("daily_type", daily_type);
			String case_state = request.getParameter("case_state");
			params.put("case_state", case_state);
			String case_receiver = request.getParameter("case_receiver");
			params.put("case_receiver", case_receiver);
			String receiver_area = request.getParameter("receiver_area");
			params.put("receiver_area", receiver_area);
			String confirm_date_start = request.getParameter("confirm_date_start");
			params.put("confirm_date_start", confirm_date_start);
			String confirm_date_end = request.getParameter("confirm_date_end");
			params.put("confirm_date_end", confirm_date_end);
			financeService.exportCaseFinance(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
