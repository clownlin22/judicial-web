package com.rds.children.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.service.RdsChildrenExceptionService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("children/exception")
public class RdsChildrenExceptionController {

	@Autowired
	private RdsChildrenExceptionService rdsChildrenExceptionService;

	/**
	 * 异常查看列表
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("getCaseException")
	@ResponseBody
	private RdsChildrenResponse getCaseException(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		return rdsChildrenExceptionService.getExceptionInfo(params);
	}

	/**
	 * 新增异常
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("saveException")
	@ResponseBody
	public Map<String, Object> saveException(
			@RequestBody Map<String, Object> params, HttpSession session) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		Map<String, Object> map = new HashMap<>();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
		}
		params.put("exception_per", user.getUserid());
		if (StringUtils.isEmpty(user.getUserid())) {
			map.put("success", false);
			map.put("msg", "操作失败，请稍后重试！");
			return map;
		}
		try {
			if (rdsChildrenExceptionService.saveException(params)) {
				map.put("success", true);
				map.put("msg", "操作成功！");
			} else {
				map.put("success", false);
				map.put("msg", "操作失败，请稍后重试！");
			}
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "操作异常，请联系管理员！");
		}
		return map;
	}

	@RequestMapping("deleteException")
	@ResponseBody
	public Map<String, Object> deleteException(
			@RequestBody Map<String, Object> params, HttpSession session) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		Map<String, Object> map = new HashMap<>();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
		}
		params.put("handle_per", user.getUserid());
		if (StringUtils.isEmpty(user.getUserid())) {
			map.put("success", false);
			map.put("msg", "操作失败，请稍后重试！");
			return map;
		}
		try {
			if (rdsChildrenExceptionService.deleteException(params)) {
				map.put("success", true);
				map.put("msg", "操作成功！");
			} else {
				map.put("success", false);
				map.put("msg", "操作失败，请稍后重试！");
			}
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "操作异常，请联系管理员！");
		}
		return map;
	}

	@RequestMapping("updateException")
	@ResponseBody
	public Map<String, Object> updateException(
			@RequestBody Map<String, Object> params, HttpSession session) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		Map<String, Object> map = new HashMap<>();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
		}
		params.put("handle_per", user.getUserid());
		if (StringUtils.isEmpty(user.getUserid())) {
			map.put("success", false);
			map.put("msg", "操作失败，请稍后重试！");
			return map;
		}
		try {
			if (rdsChildrenExceptionService.updateException(params)) {
				map.put("success", true);
				map.put("msg", "操作成功！");
			} else {
				map.put("success", false);
				map.put("msg", "操作失败，请稍后重试！");
			}
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "操作异常，请联系管理员！");
		}
		return map;
	}
}
