package com.rds.children.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.service.RdsChildrenResultService;
import com.rds.upc.model.RdsUpcUserModel;

@RequestMapping("children/result")
@Controller
public class RdsChildrenResultController {

	@Autowired
	private RdsChildrenResultService rdsChildrenResultService;

	@RequestMapping("getResultInfo")
	@ResponseBody
	public RdsChildrenResponse getResultInfo(
			@RequestBody Map<String, Object> params) {
		return rdsChildrenResultService.getResultInfo(params);
	}

	@RequestMapping("addCaseResult")
	@ResponseBody
	public Object addCaseResult(@RequestParam MultipartFile[] files,
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
		}
		if (StringUtils.isEmpty(user.getUserid())) {
			return false;
		}
		params.put("user", user);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = rdsChildrenResultService.addCaseResult(files, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping("addCaseResultByHand")
	@ResponseBody
	public Object addCaseResultByHand(@RequestBody Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
		}
		if (StringUtils.isEmpty(user.getUserid())) {
			return false;
		}
		params.put("user", user);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = rdsChildrenResultService.addCaseResultByHand(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@RequestMapping("getLocusInfo")
	@ResponseBody
	public Map<String, Object> getLocusInfo(
			@RequestBody Map<String, Object> params) {
		return rdsChildrenResultService.getLoucsInfo(params);
	}

	/**
	 * 从亲子鉴定导入儿童位点信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("uploadSampleByIdentify")
	@ResponseBody
	public Map<String, Object> uploadSampleByIdentify(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
		}
		if (StringUtils.isEmpty(user.getUserid())) {
			map.put("success", false);
			map.put("result", false);
			map.put("message", "登录状态失效，请重新登录！");
			return map;
		}
		params.put("user", user);
		try {
			map = rdsChildrenResultService.uploadSampleByIdentify(params);
		} catch (Exception e) {
			map.put("success", false);
			map.put("result", false);
			map.put("message", "操作失败，请联系管理员！");
			e.printStackTrace();
		}
		return map;
	}
}
