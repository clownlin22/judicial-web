package com.rds.bacera.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.bacera.service.RdsBaceraIdentifyService;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.service.RdsCaseFinanceService;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("bacera/identify")
public class RdsBaceraIdentifyController extends RdsBaceraAbstractController {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String USER_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "user_permit");

	@Setter
	@Autowired
	private RdsBaceraIdentifyService rdsIdentifyService;

	@Setter
	@Autowired
	private RdsBaceraBoneAgeService rdsBoneAgeService;
	
	@Setter
	@Autowired
	private RdsCaseFinanceService rdsCaseFinanceService;

	@Getter
	@Setter
	private String KEY = "id";

	/**
	 * 根据条件获取案例的基本信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getCaseInfo")
	@ResponseBody
	public Object getCaseInfo(@RequestBody Map<String, Object> params,
			HttpSession session) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) (RdsUpcUserModel) session
				.getAttribute("user");
		if (!USER_PERMIT.contains(user.getUsercode())) {
			params.put("companyid", user.getCompanyid());
		}
		return rdsIdentifyService.getCaseInfo(this.pageSet(params));
	}

	/**
	 * 删除案例的信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("deleteCaseInfo")
	@ResponseBody
	public boolean deleteCaseInfo(@RequestBody Map<String, Object> params) {
		boolean result = rdsIdentifyService.deleteCaseInfo(params);
		return result;
	}

	/**
	 * 获取案例的样本信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getSampleInfo")
	@ResponseBody
	public RdsJudicialResponse getSampleInfo(
			@RequestBody Map<String, Object> params) {
		return rdsIdentifyService.getSampleInfo(params);
	}

	/**
	 * 保存案例信息
	 */
	@RequestMapping("saveCaseInfo")
	@ResponseBody
	public boolean saveCaseInfo(@RequestBody Map<String, Object> params,
			HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		String case_in_per = user.getUserid();
		params.put("case_in_per", case_in_per);
		// 案例id
		String case_id = UUIDUtil.getUUID();
		params.put("case_id", case_id);
		// 样本数量
		int per_num = params.get("sample_username") == null ? 0 : (params
				.get("sample_username").toString().split(",").length);
		params.put("per_num", per_num);
		params.put("finance_type", params.get("type"));
		if(rdsCaseFinanceService.addCaseFee(params))
			return rdsIdentifyService.saveCaseInfo(params);
		else return false;
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 财务id
//		map.put("id", case_id);
//		// 财务编号
//		map.put("num", params.get("case_code"));
		// 案例归属人，根据归属人配置公式算出应收款项
//		String receiver_id = params.get("receiver_id").toString();

		// 样本数量
//		int per_num = params.get("sample_username") == null ? 0 : (params
//				.get("sample_username").toString().split(",").length);
		// 根据公式计算该案例的应收，typeid:单双亲 per_num:样本数量 reveiver_id 归属人id
		// case_type:案例类型（司法/医学）
//		Map<String, Object> result = rdsIdentifyService.getStandFee(
//				Integer.parseInt(params.get("typeid").toString()), per_num,
//				receiver_id,
//				Integer.valueOf(params.get("case_type").toString()));

		// Map<String, Object> result = rdsJudicialPhoneRegisterService
		// .getStandFee(Integer.parseInt(params.get("typeid").toString()),
		// per_num, receiver_id,
		// Integer.valueOf(params.get("case_type").toString()));
		// 案例应收计算结果
//		if (Boolean.valueOf(result.get("success").toString())) {
//			map.put("paid", Double.valueOf(result.get("standFee").toString()));
//			map.put("receivables",
//					Double.valueOf(result.get("standFee").toString()));
//		} else {
//			map.put("paid", 0);
//			map.put("receivables", 0);
//		}
//		try {
//			map.put("case_type", params.get("type"));
//			// 插入财务信息
//			// rdsBoneAgeService.insertFinance(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * 修改案例信息
	 */
	@RequestMapping("updateCaseInfo")
	@ResponseBody
	public boolean updateCaseInfo(@RequestBody Map<String, Object> params,
			HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		String case_in_per = user.getUserid();
		params.put("case_in_per", case_in_per);
		int per_num = params.get("sample_username") == null ? 0 : (params
				.get("sample_username").toString().split(",").length);
		params.put("per_num", per_num);
		params.put("finance_type", params.get("type"));
		if(rdsCaseFinanceService.updateCaseFee(params))
			return rdsIdentifyService.updateCaseInfo(params);
		else return false;
	}

	@RequestMapping("exsitCaseCode")
	@ResponseBody
	public boolean exsitCaseCode(@RequestBody Map<String, Object> params) {
		return rdsIdentifyService.exsitCaseCode(params);
	}

	@RequestMapping("exsitSampleCode")
	@ResponseBody
	public boolean exsitSampleCode(@RequestBody Map<String, Object> params) {
		return rdsIdentifyService.exsitSampleCode(params);
	}

	/**
	 * 验证身份证
	 */
	@RequestMapping("verifyId_Number")
	@ResponseBody
	public boolean verifyId_Number(@RequestBody Map<String, Object> params) {
		return rdsIdentifyService.verifyId_Number(params);
	}

	/**
	 * 导出亲子鉴定
	 * 
	 * @param response
	 * @param request
	 */
	@RequestMapping("exportIdentifyInfo")
	public void exportIdentifyInfo(HttpServletResponse response,
			HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String case_code = request.getParameter("case_code");
			params.put("case_code", case_code);
			String receiver = request.getParameter("receiver");
			params.put("receiver", receiver);
			String samplename = request.getParameter("samplename");
			params.put("samplename", samplename);
			params.put("starttime", request.getParameter("starttime"));
			params.put("endtime", request.getParameter("endtime"));
			params.put("express_starttime",
					request.getParameter("express_starttime"));
			params.put("express_endtime",
					request.getParameter("express_endtime"));
			params.put("expressnum", request.getParameter("expressnum"));
			params.put("reportif", request.getParameter("reportif"));
			params.put("is_delete", request.getParameter("is_delete"));
			String agentid = request.getParameter("agentid");
			params.put("agentid", agentid);
			String case_in_per = request.getParameter("case_in_per");
			params.put("case_in_per", case_in_per);
			String type = request.getParameter("type");
			params.put("type", "null".equals(type) ? "" : type);
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			if ("亲子鉴定-杭州千麦".equals(type)) {
				rdsIdentifyService.exportIdentifyInfoQM(params, response);
			} else {
				rdsIdentifyService.exportIdentifyInfo(params, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
