package com.rds.judicial.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.children.mapper.RdsChildrenSampleReceiveMapper;
import com.rds.code.utils.model.MailInfo;
import com.rds.judicial.model.RdsJudicialMailInfoModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleExpressModel;
import com.rds.judicial.service.RdsJudicialMailService;
import com.rds.judicial.service.RdsJudicialSampleRelayService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("judicial/mail")
public class RdsJudicialMailController {

	@Setter
	@Autowired
	private RdsJudicialMailService RdsJudicialMailService;

	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	@Autowired
	private RdsJudicialSampleRelayService rdsJudicialSampleRelayService;

	@Autowired
	private RdsChildrenSampleReceiveMapper rdsChildrenSampleReceiveMapper;

	/**
	 * 根据条件获取案例的基本信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getMailCaseInfo")
	@ResponseBody
	public RdsJudicialResponse getMailCaseInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		// String sql_str = "";
		String partner_name = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			// sql_str = user.getSql_str();
			partner_name = user.getParnter_name();
		}
		params.put("partner_name", partner_name);
		return RdsJudicialMailService.getMailCaseInfo(params);
	}

	/**
	 * 获取快递信息
	 */
	@RequestMapping("getMailInfo")
	@ResponseBody
	public List<MailInfo> getMailInfo(@RequestBody Map<String, Object> params) {
		return RdsJudicialMailService.getMailInfo(params);
	}

	/**
	 * 获取所有邮件
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getAllMails")
	@ResponseBody
	public List<RdsJudicialMailInfoModel> getAllMails(
			@RequestBody Map<String, Object> params) {
		return RdsJudicialMailService.getAllMails(params);
	}

	/**
	 * 删除邮件信息
	 */
	@RequestMapping("delMailInfo")
	@ResponseBody
	public Object delMailInfo(@RequestBody Map<String, Object> params) {
		return RdsJudicialMailService.delMailInfo(params);
	}

	/**
	 * 保存邮件信息
	 */

	@RequestMapping("saveMailInfo")
	@ResponseBody
	public boolean saveMailInfo(@RequestBody Map<String, Object> params,
			HttpSession session) {
		String userid = "";
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			userid = user.getUserid();
			if (null == userid)
				return false;
		}
		params.put("user_id", userid);
		System.out.println(params.get("case_type"));
		if (null == params.get("case_type")) {
			params.put("case_type", "dna");
			if (RdsJudicialMailService.saveMailInfo(params)) {
				String caseCode = params.get("case_code").toString();
				rdsActivitiJudicialService.runByCaseCode(caseCode, "taskMail",
						null, user);
				Map<String, Object> mapTemp = new HashMap<String, Object>();
				mapTemp.put("case_code", caseCode);
				mapTemp.put("verify_state", 9);
				rdsJudicialSampleRelayService.updateCaseVerifyBycode(mapTemp);
				return true;
			} else {
				return false;
			}
		} else {
			System.out.println("11111111111111111");
			// 保存邮寄信息
			if (RdsJudicialMailService.saveMailInfo(params)) {
				// 儿童基因库流程
				Map<String, Object> variables = new HashMap<>();
				String caseCode = params.get("case_code").toString();
				rdsActivitiJudicialService.runByChildCaseCode(caseCode,"taskMail",
						variables, user);
				Map<String, Object> mapTemp = new HashMap<String, Object>();
				mapTemp.put("case_code", caseCode);
				mapTemp.put("verify_state", 8);
				rdsChildrenSampleReceiveMapper.updateCaseState(mapTemp);
				return true;
			} else {
				return false;
			}
		}

	}

	/**
	 * 修改邮件信息
	 */
	@RequestMapping("updateMailInfo")
	@ResponseBody
	public boolean updateMailInfo(@RequestBody Map<String, Object> params,
			HttpSession session) {
		String userid = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			userid = user.getUserid();
		}
		params.put("user_id", userid);
		return RdsJudicialMailService.updateMailInfo(params);
	}

	@RequestMapping("insertException")
	@ResponseBody
	public Map<String, Object> insertException(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String userid = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			userid = user.getUserid();
		}
		params.put("userid", userid);
		return RdsJudicialMailService.insertException(params);
	}

	@RequestMapping("querySampleRecive")
	@ResponseBody
	public List<RdsJudicialSampleExpressModel> querySampleRecive(
			@RequestBody Map<String, Object> params) {
		return RdsJudicialMailService.querySampleRecive(params);
	}

}
