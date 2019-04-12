package com.rds.judicial.web.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.code.date.DateUtils;
import com.rds.code.utils.ExportUtils;
import com.rds.judicial.mapper.RdsJudicialOtherMapper;
import com.rds.judicial.model.RdsJudicialCaseProcessModel;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("judicial/other")
public class RdsJudicialOtherController {

	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;
	
	@Autowired
	private RdsJudicialOtherMapper rdsJudicialOtherMapper;

	@RequestMapping("caseCodeRun")
	@ResponseBody
	public Object caseCodeRun(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		String case_id = params.get("case_id").toString();
		String state = params.get("state").toString();
		Map<String, Object> resultTemp = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		Map<String, Object> variables = new HashMap<String, Object>();
		// 跳过样本交接
		if ("1".equals(state)) {
			try {
				rdsActivitiJudicialService.runByCaseCode(case_id, variables,
						user);
				params.put("verify_state", 4);
				rdsJudicialOtherMapper.updateCaseInfoVerifyState(params);
			} catch (Exception e) {
				e.printStackTrace();
				resultTemp.put("success", true);
				resultTemp.put("result", false);
				resultTemp.put("message", "流程操作失败，请查看当前案例流程状态！");
				return resultTemp;
			}
		}
		// 跳过样本接收
		else if ("2".equals(state)) {
			try {
				rdsActivitiJudicialService.runByCaseCode(case_id, variables,
						user);
				params.put("verify_state", 4);
				rdsJudicialOtherMapper.updateCaseInfoVerifyState(params);
			} catch (Exception e) {
				e.printStackTrace();
				resultTemp.put("success", true);
				resultTemp.put("result", false);
				resultTemp.put("message", "流程操作失败，请查看当前案例流程状态！");
				return resultTemp;
			}
		}
		// 跳过样本确认
		else if ("3".equals(state)) {
			variables.put("issamplepass", 1);
			try {
				rdsActivitiJudicialService.runByCaseCode(case_id, variables,
						user);
				params.put("verify_state", 5);
				rdsJudicialOtherMapper.updateCaseInfoVerifyState(params);
			} catch (Exception e) {
				e.printStackTrace();
				resultTemp.put("success", true);
				resultTemp.put("result", false);
				resultTemp.put("message", "流程操作失败，请查看当前案例流程状态！");
				return resultTemp;
			}
		}
		// 跳过实验
		else if ("4".equals(state)) {
			variables.put("isyesresult", 1);
			variables.put("isback", -1);
			try {
				rdsActivitiJudicialService.runByCaseCode(case_id, variables,
						user);
				params.put("verify_state", 6);
				rdsJudicialOtherMapper.updateCaseInfoVerifyState(params);
			} catch (Exception e) {
				e.printStackTrace();
				resultTemp.put("success", true);
				resultTemp.put("result", false);
				resultTemp.put("message", "流程操作失败，请查看当前案例流程状态！");
				return resultTemp;
			}
		}
		// 跳过报告打印
		else if ("5".equals(state)) {
			try {
				rdsActivitiJudicialService.runByCaseCode(case_id, variables,
						user);
				params.put("verify_state", 7);
				rdsJudicialOtherMapper.updateCaseInfoVerifyState(params);
			} catch (Exception e) {
				e.printStackTrace();
				resultTemp.put("success", true);
				resultTemp.put("result", false);
				resultTemp.put("message", "流程操作失败，请查看当前案例流程状态！");
				return resultTemp;
			}
		}
		// 跳过报告交接
		else if ("6".equals(state)) {
			try {
				rdsActivitiJudicialService.runByCaseCode(case_id, variables,
						user);
				params.put("verify_state", 7);
				rdsJudicialOtherMapper.updateCaseInfoVerifyState(params);
			} catch (Exception e) {
				e.printStackTrace();
				resultTemp.put("success", true);
				resultTemp.put("result", false);
				resultTemp.put("message", "流程操作失败，请查看当前案例流程状态！");
				return resultTemp;
			}
		}
		// 跳过报告确认
		else if ("7".equals(state)) {
			variables.put("isreportpass", 1);
			try {
				rdsActivitiJudicialService.runByCaseCode(case_id, variables,
						user);
				params.put("verify_state", 8);
				rdsJudicialOtherMapper.updateCaseInfoVerifyState(params);
			} catch (Exception e) {
				e.printStackTrace();
				resultTemp.put("success", true);
				resultTemp.put("result", false);
				resultTemp.put("message", "流程操作失败，请查看当前案例流程状态！");
				return resultTemp;
			}
		}
		resultTemp.put("success", true);
		resultTemp.put("result", true);
		return resultTemp;
	}
	/**
	 * 导出样本信息
	 * 
	 * @param params
	 * @param response
	 */
	@RequestMapping("exportCaseInfo")
	public void exportCaseInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> params = new HashMap<String, Object>();
		String case_code = request.getParameter("case_code");
		params.put("case_code", case_code);
		String report_model=request.getParameter("report_model");
		params.put("report_model", report_model);
		String starttime = request.getParameter("starttime");
		params.put("starttime", starttime);
		String endtime = request.getParameter("endtime");
		params.put("endtime", endtime);
		String verify_state = request.getParameter("verify_state");
		params.put("verify_state", verify_state);
		String client = request.getParameter("client");
		params.put("client", client);
		String phone = request.getParameter("phone");
		params.put("phone", phone);
		
		String filename = "案例列表-流程";
		// 根据查询条件查询案例信息
		List<RdsJudicialCaseProcessModel> caseInfoModels = rdsJudicialOtherMapper.queryExport(params);
		
		Object[] titles = { "案例编号","受理日期", "登记日期","委托人","手机号码", "操作人", "操作步骤", "开始时间",
				"结束时间", "批注" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < caseInfoModels.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			// 案例编号
			objects.add(caseInfoModels.get(i).getCase_code());
			//受理日期
			objects.add(caseInfoModels.get(i).getAccept_time());
			// 登记时间
			objects.add(caseInfoModels.get(i).getSample_in_time());
			// 委托人
			objects.add(caseInfoModels.get(i).getClient());
			// 联系方式
			objects.add(caseInfoModels.get(i).getPhone());
			// 操作人
			objects.add(caseInfoModels.get(i).getASSIGNEE_());
			// 操作步骤
			objects.add(caseInfoModels.get(i).getNAME_());
			// 开始时间
			objects.add(caseInfoModels.get(i).getSTART_TIME_());
			// 结束时间
			objects.add(caseInfoModels.get(i).getEND_TIME_());
			// 批注
			objects.add(caseInfoModels.get(i).getMESSAGE_());
			
			bodys.add(objects);
		}
		
		ExportUtils.export(response, filename, titles, bodys, "亲子鉴定-流程"
				+ DateUtils.Date2String(new Date()));
	
	}

}
