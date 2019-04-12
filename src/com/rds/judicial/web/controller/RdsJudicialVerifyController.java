package com.rds.judicial.web.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.model.RdsJudicialMessageModel;
import com.rds.judicial.service.RdsJudicialPrintService;
import com.rds.judicial.service.RdsJudicialVerifyService;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * @description 案例审核control
 * @author ThinK 2015年4月15日
 */
@Controller
@RequestMapping("judicial/verify")
public class RdsJudicialVerifyController {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "judicial_path");

	@Setter
	@Autowired
	private RdsJudicialVerifyService verifyService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private RdsJudicialPrintService rdsJudicialPrintService;

	@RequestMapping("yes")
	@ResponseBody
	public RdsJudicialMessageModel yesVerify(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (user == null) {
			return new RdsJudicialMessageModel(false, "请重新登录", false);
		}
		params.put("verify_baseinfo_person", user.getUserid());
		String report_model = (params.get("report_model") == null) ? ""
				: params.get("report_model").toString();
		
		Map<String, Object> map = verifyService.yesVerify(params);
		if ((boolean) map.get("success")) {
			//审核通过签收流程
			String taskId = params.get("taskId").toString();
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			if (task == null) {
				return new RdsJudicialMessageModel(false, "该流程步不存在", false);
			}
			Map<String, Object> variables = new HashMap<String, Object>();
			if ("".equals(report_model))
				variables.put("ispass", 2);
			else
				variables.put("ispass", 1);
			if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
				taskService.claim(taskId, user.getUsername());// todo:需要改成userid
				taskService.complete(taskId, variables);
			} else {
				taskService.complete(taskId, variables);
			}
			if (null != map.get("sample_code"))
				return new RdsJudicialMessageModel(true, "审核通过成功！样本条码为：<br>"
						+ map.get("sample_code"), true);
			else
				return new RdsJudicialMessageModel(true, "审核通过成功！", true);
		} else
			return new RdsJudicialMessageModel(false, "审核通过失败,请联系管理员", false);
	}

	@RequestMapping("no")
	@ResponseBody
	public RdsJudicialMessageModel noVerify(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (user == null) {
			return new RdsJudicialMessageModel(false, "请重新登录", false);
		}

		if (verifyService.noVerify(params)){
			
			params.put("verify_baseinfo_person", user.getUserid());
			String taskId = params.get("taskId").toString();
			String processInstanceId = params.get("processInstanceId").toString();
			String comment = params.get("verify_baseinfo_remark").toString();
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			if (task == null) {
				return new RdsJudicialMessageModel(false, "该流程步不存在", false);
			}

			identityService.setAuthenticatedUserId(user.getUsername());// todo:需要改成userid
			taskService.addComment(taskId, processInstanceId, comment);

			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("ispass", 0);
			if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
				taskService.claim(taskId, user.getUsername());// todo:需要改成userid
				taskService.complete(taskId, variables);
			} else {
				taskService.complete(taskId, variables);
			}

				return new RdsJudicialMessageModel(true, "审核不通过成功", true);
		}else
		{
			return new RdsJudicialMessageModel(false, "审核不通过失败,请联系管理员", false);
		}
		
	}

	@RequestMapping("sampleYes")
	@ResponseBody
	public RdsJudicialMessageModel yesSampleVerify(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (user == null) {
			return new RdsJudicialMessageModel(false, "请重新登录", false);
		}
		params.put("verify_sampleinfo_person", user.getUserid());
		if (verifyService.yesSampleVerify(params))
			return new RdsJudicialMessageModel(true, "审核通过成功", true);
		return new RdsJudicialMessageModel(false, "审核通过失败,请联系管理员", false);
	}

	@RequestMapping("sampleNo")
	@ResponseBody
	public RdsJudicialMessageModel noSampleVerify(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (user == null) {
			return new RdsJudicialMessageModel(false, "请重新登录", false);
		}
		params.put("verify_sampleinfo_person", user.getUserid());
		if (verifyService.noSampleVerify(params))
			return new RdsJudicialMessageModel(true, "审核不通过成功", true);
		return new RdsJudicialMessageModel(false, "审核不通过失败,请联系管理员", false);
	}

	/**
	 * 根据条件获取案例的基本信息
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getCaseInfo")
	@ResponseBody
	public Map<String, Object> getCaseInfo(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			// sql_str = user.getSql_str();
		}
		// if (!USER_PERMIT.contains(user.getUsercode()))
		// {
		// if(null != user.getAreacode() && !"".equals(user.getAreacode()))
		// {
		// if(null != user.getParnter_name() &&
		// !"".equals(user.getParnter_name()))
		// {
		// params.put("areacode", user.getAreacode());
		// params.put("parnter_name", user.getParnter_name());
		// }else
		// params.put("areacode", user.getAreacode());
		// }
		// else
		// {
		// params.put("areacode", user.getAreacode());
		// params.put("parnter_name", user.getParnter_name());
		// params.put("userid", user.getUserid());
		// }
		// }
		if (null != user.getParnter_name()
				&& !"".equals(user.getParnter_name())) {
			params.put("parnter_name", user.getParnter_name());
		}
		map.put("data", verifyService.queryAll(params));
		map.put("total", verifyService.queryCount(params));
		return map;
	}

	@RequestMapping("queryVerifyHistory")
	@ResponseBody
	public List<Map<String, Object>> queryVerifyHistory(
			@RequestBody Map<String, Object> params) {
		return verifyService.queryVerifyHistory((String) params.get("case_id"));
	}

	@RequestMapping("verifyWord")
	public Object queryVerify(HttpServletRequest request, String case_code,
			String report_model) {
		try {
			String file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + ".doc";
			Map<String, Object> params = new HashMap<>();
			params.put("case_code", case_code);
			params.put("file_name", file_name);
			params.put("report_model", report_model);
			File directory = new File(ATTACHMENTPATH + File.separatorChar
					+ case_code);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			if (report_model.equals("sbyxmodel")
					|| report_model.equals("syjdmodel")
					|| report_model.equals("dcjdmodel")) {
				rdsJudicialPrintService.createJudicialDocBySubCaseCode(params);
			} else {
				rdsJudicialPrintService.createJudicialDoc(params);
			}
			request.setAttribute("file_name", "file://" + file_name);
			return "pageoffice/readOnly_word";

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// request.setAttribute("case_code",case_code);
		// request.setAttribute("report_model",report_model);
		// return "judicial/getPDF";
	}

	@RequestMapping("getPrintBarcode")
	public String getPrintBarcode(String caseCodes, HttpServletRequest request) {
		String[] caseCodeArray = caseCodes.split(",");
		List<String> sampleCodes = verifyService.getSampleCodes(caseCodeArray);
		request.setAttribute("msgs", sampleCodes);
		return "judicial/getPrintBarcode";
	}

	@RequestMapping("expBack")
	@ResponseBody
	public RdsJudicialMessageModel expBack(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (user == null) {
			return new RdsJudicialMessageModel(false, "请重新登录", false);
		}
		params.put("verify_baseinfo_person", user.getUserid());

		String taskId = params.get("task_id").toString();
		String processInstanceId = params.get("process_instance_id").toString();
		String comment = params.get("back_remark").toString();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			return new RdsJudicialMessageModel(false, "该流程步不存在", false);
		}

		identityService.setAuthenticatedUserId(user.getUsername());// todo:需要改成userid
		taskService.addComment(taskId, processInstanceId, comment);

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("isback", 1);
		variables.put("isyesresult", -1);
		if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
			taskService.claim(taskId, user.getUsername());// todo:需要改成userid
			taskService.complete(taskId, variables);
		} else {
			taskService.complete(taskId, variables);
		}

		if (verifyService.noVerify(params))
			return new RdsJudicialMessageModel(true, "实验室退回成功", true);
		return new RdsJudicialMessageModel(false, "实验室退回失败,请联系管理员", false);
	}

	@RequestMapping("updateSampleCaseInfo")
	@ResponseBody
	public Object updateSampleCaseInfo(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		return verifyService.updateSampleCaseInfo(params);
	}

	@RequestMapping("updateCaseRemark")
	@ResponseBody
	public RdsJudicialMessageModel updateCaseRemark(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		if (null == user) {
			return new RdsJudicialMessageModel(false, "登录状态失效，请重新登录!", false);
		}
		String remark_bak = params.get("remark_bak") == null ? "" : params.get(
				"remark_bak").toString();
		String remark = params.get("remark").toString();
		StringBuilder string = new StringBuilder();
		string.append(remark_bak);
		string.append(";");
		string.append(dateString);
		string.append(user.getUsername());
		string.append(" :");
		string.append(remark);
		params.put("remark", string.toString());
		if (verifyService.updateCaseRemark(params) > 0)
			return new RdsJudicialMessageModel(true, "备注添加成功!", true);
		else
			return new RdsJudicialMessageModel(false, "备注添加失败,请联系管理员!", false);
	}
	
	@RequestMapping("updateCaseConsignment")
	@ResponseBody
	public RdsJudicialMessageModel updateCaseConsignment(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (null == user) {
			return new RdsJudicialMessageModel(false, "登录状态失效，请重新登录!", false);
		}
		if (verifyService.updateCaseConsignment(params) > 0)
			return new RdsJudicialMessageModel(true, "修改委托时间成功!", true);
		else
			return new RdsJudicialMessageModel(false, "修改委托时间失败,请联系管理员!", false);
	}
	
	@RequestMapping("onSubmitVerifyExperiment")
	@ResponseBody
	public boolean onSubmitVerifyExperiment(@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("user", user);
		return verifyService.updateCaseVerifyToText(params) > 0 ? true
				: false;
	}
}
