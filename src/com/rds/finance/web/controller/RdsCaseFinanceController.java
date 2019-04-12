package com.rds.finance.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.bacera.mapper.RdsBaceraInvasivePreMapper;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.model.RdsCaseFinanceAttachmentModel;
import com.rds.finance.model.RdsFinanceContractAttachmentModel;
import com.rds.finance.model.RdsFinancePromptInfo;
import com.rds.finance.model.RdsRemittanceLogInfoModel;
import com.rds.finance.model.RdsRemittancePlanInfoModel;
import com.rds.finance.service.RdsCaseFinanceService;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("finance/casefinance")
public class RdsCaseFinanceController {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String USER_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "user_permit");

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsCaseFinanceService rdsCaseFinanceService;

	@Setter
	@Autowired
	private RdsBaceraInvasivePreMapper rdsInvasivePreMapper;

	@RequestMapping("getCaseFeeInfo")
	@ResponseBody
	public RdsJudicialResponse getCaseFeeInfo(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("parnter_name", user.getParnter_name());
		if (!FINANCE_PERMIT.contains(user.getUsercode())) {
			params.put("userid", user.getUserid());
		}
		return rdsCaseFinanceService.getCaseFeeInfo(params);
	}

	@RequestMapping("updateCaseFee")
	@ResponseBody
	public boolean updateCaseFee(@RequestBody Map<String, Object> params,
			HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		params.put("update_user", user.getUserid());
		params.put("updateByPerson", "1");
		return rdsCaseFinanceService.updateCaseFee(params);
	}

	@RequestMapping("caseFeeConfirm")
	@ResponseBody
	public boolean caseFeeConfirm(@RequestBody Map<String, Object> params,
			HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		params.put("update_user", user.getUserid());
		return rdsCaseFinanceService.confirmCaseFee(params);
	}

	@RequestMapping("queryCasefeePrompt")
	@ResponseBody
	public List<RdsFinancePromptInfo> queryCasefeePrompt(String query,
			HttpServletRequest request) {
		return rdsCaseFinanceService.queryCasefeePrompt();
	}

	@RequestMapping("queryPageRemittanceInfo")
	@ResponseBody
	public RdsJudicialResponse queryPageRemittanceInfo(
			@RequestBody Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (!USER_PERMIT.contains(user.getUsercode()))
			params.put("userid", user.getUserid());
		return rdsCaseFinanceService.queryPageRemittanceInfo(params);
	}

	@RequestMapping("insertRemittanceRecord")
	@ResponseBody
	public boolean insertRemittanceRecord(@RequestParam MultipartFile[] files,
			@RequestParam String fee_id, @RequestParam String daily_type,
			@RequestParam String urgent_state,
			@RequestParam Map<String, Object> params, HttpSession session,
			HttpServletResponse response) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		params.put("files", files);
		params.put("create_per", user.getUserid());
		params.put("fee_id", fee_id);
		params.put("daily_type", daily_type);
		params.put("urgent_state", urgent_state);
		params.put("remittance_id", UUIDUtil.getUUID());
		return rdsCaseFinanceService.insertRemittanceRecord(params);
	}

	@RequestMapping("updateRemittanceRecord")
	@ResponseBody
	public boolean updateRemittanceRecord(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		params.put("confirm_per", user.getUserid());
		return rdsCaseFinanceService.updateRemittanceRecord(params);
	}

	@RequestMapping("updateRemittance")
	@ResponseBody
	public boolean updateRemittance(@RequestBody Map<String, Object> params,
			HttpSession session) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		params.put("confirm_per", user.getUserid());
		return rdsCaseFinanceService.updateRemittance(params);
	}

	@RequestMapping("insertContractPlan")
	@ResponseBody
	public boolean insertContractPlan(@RequestBody Map<String, Object> params,
			HttpSession session) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		params.put("create_per", user.getUserid());
		params.put("contract_id", UUIDUtil.getUUID());
		return rdsCaseFinanceService.insertContractPlan(params);
	}

	@RequestMapping("deleteRemittancePlan")
	@ResponseBody
	public boolean deleteRemittancePlan(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		return rdsCaseFinanceService.deleteRemittancePlan(params);
	}

	@RequestMapping("insertContractRemittance")
	@ResponseBody
	public boolean insertContractRemittance(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		params.put("create_per", user.getUserid());
		if (null == params.get("contract_remittance_planid")
				|| "".equals(params.get("contract_remittance_planid"))) {
			params.put("status", 1);
			params.put("contract_remittance_planid", UUIDUtil.getUUID());
			return rdsCaseFinanceService.insertContractRemittance(params);
		} else {
			return rdsCaseFinanceService.updateRemittancePlan(params);
		}
	}

	@RequestMapping("updateContractPlan")
	@ResponseBody
	public boolean updateContractPlan(@RequestBody Map<String, Object> params,
			HttpSession session) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		params.put("create_per", user.getUserid());
		return rdsCaseFinanceService.updateContractPlan(params);
	}

	@RequestMapping("queryPageContractPlan")
	@ResponseBody
	public RdsJudicialResponse queryPageContractPlan(
			@RequestBody Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (!USER_PERMIT.contains(user.getUsercode()))
			params.put("userid", user.getUserid());
		return rdsCaseFinanceService.queryPageContractPlan(params);
	}

	@RequestMapping("queryAllContractPlan")
	@ResponseBody
	public List<RdsRemittancePlanInfoModel> queryAllContractPlan(
			@RequestBody Map<String, Object> params) {
		try {
			return rdsCaseFinanceService.queryAllContractPlan(params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping("queryRemittanceLogs")
	@ResponseBody
	public List<RdsRemittanceLogInfoModel> queryRemittanceLogs(
			@RequestBody Map<String, Object> params) {
		try {
			return rdsCaseFinanceService.queryRemittanceLogs(params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除汇款单，更新财务信息
	 * 
	 * @param params
	 * @param session
	 * @return
	 */
	@RequestMapping("deleteCaseFeeRemittance")
	@ResponseBody
	public boolean deleteCaseFeeRemittance(
			@RequestBody Map<String, Object> params, HttpSession session) {
		try {
			return rdsCaseFinanceService.deleteCaseFeeRemittance(params);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@RequestMapping("queryConfirmCase")
	@ResponseBody
	public boolean queryConfirmCase(@RequestBody Map<String, Object> params,
			HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if (user.getUserid() == null)
			return false;
		try {
			return rdsCaseFinanceService.queryConfirmCase(params) > 0 ? true
					: false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 案例优惠码插入
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("confirmCodeAdd")
	@ResponseBody
	public Object confirmCodeAdd(@RequestBody Map<String, Object> params,
			HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		String finance_type = (String) params.get("finance_type");
		if ("1".equals(finance_type)) {
			params.put("finance_type", "NIPT(博奥)");
		} else if ("2".equals(finance_type)) {
			params.put("finance_type", "NIPT(贝瑞)");
		} else if ("3".equals(finance_type)) {
			params.put("finance_type", "NIPT-plus(博奥)");
		} else if ("4".equals(finance_type)) {
			params.put("finance_type", "NIPT-plus(贝瑞)");
		} else if ("5".equals(finance_type)) {
			params.put("finance_type", "NIPT(成都博奥)");
		}
		if (user.getUserid() == null)
			return false;
		params.put("update_user", user.getUserid());
		try {
			result = rdsCaseFinanceService.confirmCodeAdd(params);
			if (null != result && "inversive".equals(params.get("case_type"))) {// 无创产前案例，修改confirm值
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("num", params.get("num"));
				map.put("id", params.get("case_id"));
				map.put("confirm_code", params.get("confirm_code"));
				rdsInvasivePreMapper.update(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 案例退款
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("confirmCodeBack")
	@ResponseBody
	public Object confirmCodeBack(@RequestBody Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = rdsCaseFinanceService.queryCaseConfirm(params);
		} catch (Exception e) {
			result.put("success", true);
			result.put("result", false);
			result.put("message", "操作异常，请查看！");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("backCasefee")
	@ResponseBody
	public Object backCasefee(@RequestBody Map<String, Object> params,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("id", UUIDUtil.getUUID());
			params.put("update_user", user.getUserid());
			try {
				map = rdsCaseFinanceService.addCaseFeeOther(params);
			} catch (Exception e) {
				map.put("result", false);
				map.put("success", false);
				map.put("message", "操作异常，请联系管理员！");
				e.printStackTrace();
			}
			return map;
		} else {
			map.put("result", false);
			map.put("message", "登录失效，请联系管理员!");
			return map;
		}
	}

	/**
	 * 汇款人员手动修改价格
	 * 
	 * @param params
	 * @param session
	 * @return
	 */
	@RequestMapping("updateCaseFeeByRegister")
	@ResponseBody
	public Object updateCaseFeeByRegister(
			@RequestBody Map<String, Object> params, HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		params.put("update_user", user.getUserid());
		return rdsCaseFinanceService.updateCaseFeeByRegister(params);
	}

	@RequestMapping("upload")
	@ResponseBody
	public void upload(@RequestParam String contract_id,
			@RequestParam String contract_num, @RequestParam int[] filetype,
			@RequestParam MultipartFile[] files, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contract_id", contract_id);
		params.put("contract_num", contract_num);
		params.put("userid", user.getUserid());

		try {
			// 上传案例文件
			Map<String, Object> map = rdsCaseFinanceService.upload(contract_id,
					contract_num, files, filetype, user.getUserid());
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"result\":true,\"msg\":\""
							+ (String) map.get("message") + "\"}");
		} catch (IOException e) {
			e.printStackTrace();
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"result\":false,\"msg\":\""
							+ "出错了，请重新上传！" + "\"}");
		}

	}

	/**
	 * 获取附件列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping("queryAllContractAttachment")
	@ResponseBody
	public List<RdsFinanceContractAttachmentModel> queryAllContractAttachment(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		return rdsCaseFinanceService.queryContractAttachment(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public void delete(@RequestBody Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		boolean result = rdsCaseFinanceService.deleteFile(params);
		try {
			if (result) {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().print(
						"{\"result\":true,\"msg\":\"" + "删除成功！" + "\"}");
			} else {

				response.setContentType("text/html; charset=utf-8");
				response.getWriter()
						.print("{\"result\":false,\"msg\":\"" + "删除失败，请联系管理员！"
								+ "\"}");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载图片
	 */
	@RequestMapping("downloadAttachment")
	public void downloadAttachment(HttpServletResponse response,
			@RequestParam String id) {
		rdsCaseFinanceService.downloadAttachment(response, id);
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
			if (!USER_PERMIT.contains(user.getUsercode()))
				params.put("userid", user.getUserid());
			String remittance_num = request.getParameter("remittance_num");
			params.put("remittance_num", remittance_num);
			String remittance_per_name = request
					.getParameter("remittance_per_name");
			params.put("remittance_per_name", remittance_per_name);
			String remittance_account = request
					.getParameter("remittance_account");
			params.put("remittance_account", remittance_account);
			String arrival_account = request.getParameter("arrival_account");
			params.put("arrival_account", arrival_account);
			String create_per_name = request.getParameter("create_per_name");
			params.put("create_per_name", create_per_name);
			String starttime = request.getParameter("starttime");
			params.put("starttime", starttime);
			String endtime = request.getParameter("endtime");
			params.put("endtime", endtime);
			String daily_type = request.getParameter("daily_type");
			params.put("daily_type", daily_type);
			String confirm_state = request.getParameter("confirm_state");
			params.put("confirm_state", confirm_state);
			String urgent_state = request.getParameter("urgent_state");
			params.put("urgent_state", urgent_state);
			String confirm_per_name = request.getParameter("confirm_per_name");
			params.put("confirm_per_name", confirm_per_name);
			rdsCaseFinanceService.exportCaseFinance(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 抵扣汇款单处理
	 * 
	 * @param params
	 * @param session
	 * @return
	 */
	@RequestMapping("deductibleCaseInfo")
	@ResponseBody
	public boolean deductibleCaseInfo(@RequestBody Map<String, Object> params,
			HttpSession session) {
		try {
			return rdsCaseFinanceService.deductibleCaseInfo(params);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 导出财务信息
	 * 
	 * @param response
	 * @param request
	 */
	@RequestMapping("exportCaseInfoOther")
	public void exportCaseInfoOther(HttpServletResponse response,
			HttpServletRequest request, HttpSession session) {
		try {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("parnter_name", user.getParnter_name());
			String case_code = request.getParameter("case_code");
			params.put("case_code", case_code);
			String areaname = request.getParameter("areaname");
			params.put("areaname", areaname);
			String type_state = request.getParameter("type_state");
			params.put("type_state", type_state);
			params.put("case_type", request.getParameter("case_type"));
			params.put("fee_state", request.getParameter("fee_state"));
			params.put("receiver", request.getParameter("receiver"));
			params.put("client", request.getParameter("client"));
			params.put("starttime", request.getParameter("starttime"));
			params.put("endtime", request.getParameter("endtime"));
			params.put("paragraphtime_start",
					request.getParameter("paragraphtime_start"));
			params.put("paragraphtime_end",
					request.getParameter("paragraphtime_end"));
			String account = request.getParameter("account");
			params.put("account", account);
			rdsCaseFinanceService.exportCaseInfoOther(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取图片文件
	 */
	@RequestMapping("getImg")
	public void getImg(HttpServletResponse response, String filename) {
		rdsCaseFinanceService.getImg(response, filename);
	}

	@RequestMapping("turnImg")
	@ResponseBody
	public Map<String, Object> turnImg(@RequestBody Map<String, Object> params) {
		return rdsCaseFinanceService.turnImg(params);
	}

	@RequestMapping("updateRemittancePhoto")
	@ResponseBody
	public boolean updateRemittancePhoto(@RequestParam MultipartFile[] files,
			@RequestParam String remittance_id,@RequestParam String remittance_num,
			@RequestParam Map<String, Object> params, HttpSession session,
			HttpServletResponse response) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		params.put("confirm_per", user.getUserid());
		params.put("remittance_id", remittance_id);
		params.put("remittance_num", remittance_num);
		params.put("confirm_state", -1);
		
		for (MultipartFile multipartFile : files) {
			params.put("create_per", user.getUserid());
			params.put("id", UUIDUtil.getUUID());
			params.put("file", multipartFile);
			if(!rdsCaseFinanceService.updateRemittancePhoto(params))
				return false;
		
		}
		return true;
	}
	
	/**
	 * 获取图片信息
	 */
	@RequestMapping("getFinanceAttachMent")
	@ResponseBody
	public List<RdsCaseFinanceAttachmentModel> getFinanceAttachMent(
			@RequestBody Map<String, Object> params) {
		try {
			return rdsCaseFinanceService.queryFinanceAttachment(params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 删除照片
	 * 
	 * @param params
	 * @param session
	 * @return
	 */
	@RequestMapping("deleteFinanceAttachment")
	@ResponseBody
	public boolean deleteFinanceAttachment(@RequestBody Map<String, Object> params,
			HttpSession session) {
		try {
			RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("confirm_per", user.getUserid());
			params.put("confirm_state", -1);
			params.put("confirm_remark", "删除照片:"+params.get("attachment_path"));
			return rdsCaseFinanceService.updateFinanceAttachment(params);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
