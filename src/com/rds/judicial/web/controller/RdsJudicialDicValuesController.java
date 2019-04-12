package com.rds.judicial.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialAreaInfo;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.service.RdsJudicialDicValuesService;
import com.rds.upc.model.RdsUpcPermitNodeModel;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("judicial/dicvalues")
public class RdsJudicialDicValuesController extends
		RdsJudicialAbstractController {

	@Getter
	@Setter
	private String KEY = "id";

	@Setter
	@Autowired
	private RdsJudicialDicValuesService RdsJudicialDicValuesService;

	/**
	 * 获取案例模板
	 * 
	 * @return
	 */
	@RequestMapping("getReportModels")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getReportModels(
			@RequestBody Map<String, Object> params, HttpSession session) {
		return RdsJudicialDicValuesService.getReportModels(params);
	}

	/**
	 * 获取案例模板
	 * 
	 * @return
	 */
	@RequestMapping("getReportModelByPartner")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getReportModelByPartner(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String userid = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			userid = user.getUserid();
		}
		params.put("userid", userid);
		return RdsJudicialDicValuesService.getReportModelByPartner(params);
	}

	/**
	 * 获取快递信息
	 * 
	 */
	@RequestMapping("getMailModels")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getMailModels() {
		return RdsJudicialDicValuesService.getMailModels();
	}

	/**
	 * 获取案例类型
	 * 
	 * @return
	 */
	@RequestMapping("getreport_type")
	@ResponseBody
	public List<RdsUpcPermitNodeModel> getReportType(HttpSession session) {
		String code = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			code = user.getUsercode();
		}
		List<RdsUpcPermitNodeModel> reportTypeModels = RdsJudicialDicValuesService
				.getReportType(code);
		return reportTypeModels;
	}

	/**
	 * 获取地区信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getAreaInfo")
	@ResponseBody
	public List<RdsJudicialAreaInfo> getAreaInfo(String query, String area_code) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("query", query);
		if ("".equals(query) || null == query)
			params.put("area_code", area_code);
		return RdsJudicialDicValuesService.getAreaInfo(params);
	}

	/**
	 * 获取样本采样类型
	 */
	@RequestMapping("getSampleType")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getSampleType() {
		return RdsJudicialDicValuesService.getSampleType();
	}

	/**
	 * 获取称谓信息
	 */
	@RequestMapping("getSampleCall")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getSampleCall() {
		return RdsJudicialDicValuesService.getSampleCall();
	}

	/**
	 * 获取称谓信息
	 */
	@RequestMapping("getCustodyCall")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getCustodyCall() {
		return RdsJudicialDicValuesService.getCustodyCall();
	}

	/**
	 * 获取接收员工
	 */
	@RequestMapping("getUpcUsers")
	@ResponseBody
	public List<RdsJudicialAreaInfo> getUpcUsers(String query,
			HttpServletRequest request) {
		String companyid = "";
		String area_id = request.getParameter("area_id");
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (user != null) {
			if (!"admin".equals(user.getUsercode()))
				companyid = user.getCompanyid();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyid", companyid);
		map.put("query", query);
		if ("".equals(query) || null == query)
			map.put("area_id", area_id);
		return RdsJudicialDicValuesService.getUpcUsers(map);
	}

	/**
	 * 获取单位类型
	 */
	@RequestMapping("getUnitTypes")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getUnitTypes() {
		return RdsJudicialDicValuesService.getUnitTypes();
	}

	/**
	 * 获取案例类型
	 */
	@RequestMapping("getCaseTypes")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getCaseTypes() {
		return RdsJudicialDicValuesService.getCaseTypes();
	}

	/**
	 * 获取财务类型
	 */
	@RequestMapping("getCaseFeeTypes")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getCaseFeeTypes() {
		return RdsJudicialDicValuesService.getCaseFeeTypes();
	}

	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		params = this.pageSet(params);
		return RdsJudicialDicValuesService.queryAllPage(this.pageSet(params));
	}

	@Override
	public Integer insert(Map<String, Object> params) throws Exception {
		params.put(KEY, UUIDUtil.getUUID());
		return RdsJudicialDicValuesService.insert(params);
	}

	@Override
	public Integer update(Map<String, Object> params) throws Exception {
		return RdsJudicialDicValuesService.update(params);
	}

	@Override
	public Integer delete(Map<String, Object> params) throws Exception {
		return RdsJudicialDicValuesService.delete(params);
	}

	@Override
	public Object queryAll(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryModel(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取所有员工
	 */
	@RequestMapping("getAllUsers")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getAllUsers() {
		return RdsJudicialDicValuesService.getAllUsers();
	}

	/**
	 * 获取所有员工名和id
	 */
	@RequestMapping("getUsersId")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getUsersId(String query,
			HttpServletRequest request) {
		String userid = request.getParameter("userid");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("query", query==null?"":query.toLowerCase());
		if (StringUtils.isNullOrEmpty(query)) {
			map.put("userid", userid);
		}
		return RdsJudicialDicValuesService.getUsersId(map);
	}

	/**
	 * 获取收费类型
	 * 
	 * @return
	 */
	@RequestMapping("getFeeType")
	@ResponseBody
	public List<Map<String, Object>> getFeeType() {
		return RdsJudicialDicValuesService.getFeeType();
	}

	@RequestMapping("getAllManager")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getManager(String query,
			HttpServletRequest request) {
		String area_id = request.getParameter("area_id");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("query", query);
		map.put("area_id", area_id);
		return RdsJudicialDicValuesService.getManager(map);
	}
}
