package com.rds.judicial.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.judicial.service.RdsJudicialAttachmentService;
import com.rds.trace.service.RdsTraceRegisterService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;

@Controller
@RequestMapping("judicial/experimentAttachment")
public class RdsJudicialAttachementController extends
		RdsJudicialAbstractController {

	@Autowired
	@Setter
	RdsJudicialAttachmentService rdsJudicialAttachmentService;

	@Autowired
	@Setter
	RdsTraceRegisterService rdsTraceRegisterService;

	@RequestMapping("queryAttachMent")
	@ResponseBody
	public Object queryAttachMent(@RequestBody Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", rdsJudicialAttachmentService.queryAttachment(params));
		result.put("total",
				rdsJudicialAttachmentService.queryCountAttachment(params));
		return result;
	}

	@RequestMapping("updateAttachMent")
	@ResponseBody
	public Object updateAttachMent(@RequestBody Map<String, Object> params) {
		int result = rdsJudicialAttachmentService.updateAttachment(params);
		return (result > 0 ? this.setModel(false,
				RdsUpcConstantUtil.UPDATE_SUCCESS) : this.setModel(false,
				RdsUpcConstantUtil.UPDATE_FAILED));
	}

	@RequestMapping("download")
	public void download(HttpServletResponse response,
			HttpServletRequest request, @RequestParam String uuid) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("down_userid", user.getUserid());
		map.put("uuid", uuid);
		try {
			rdsJudicialAttachmentService.updateAttachment(map);
			rdsJudicialAttachmentService.download(response, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("upload")
	@ResponseBody
	public void upload(@RequestParam MultipartFile[] files,
			HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		Map<String, Object> map = rdsJudicialAttachmentService.upload(
				user.getUserid(), files);
		try {
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"msg\":\""
							+ (String) map.get("message") + "\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer insert(Map<String, Object> params) throws Exception {
		return null;
	}

	@Override
	public Integer update(Map<String, Object> params) throws Exception {
		return null;
	}

	@Override
	public Object delete(Map<String, Object> params) throws Exception {
		int result = rdsJudicialAttachmentService.deleteAttachment(params);
		return (result > 0 ? this.setModel(false,
				RdsUpcConstantUtil.UPDATE_SUCCESS) : this.setModel(false,
				RdsUpcConstantUtil.UPDATE_FAILED));
	}

	@Override
	public Object queryAll(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryModel(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	//分页查询数据日志
	@RequestMapping("queryExperimentLog")
	@ResponseBody
	public Object queryExperimentLog(@RequestBody Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data",
				rdsJudicialAttachmentService.queryExperimentLog(params));
		result.put("total",
				rdsJudicialAttachmentService.countExperimentLog(params));
		return result;
	}

	/**
	 * 导出实验室数据上传下载日志
	 * @param response
	 * @param request
	 */
	@RequestMapping("exortExperimentLog")
	public void exortExperimentLog(HttpServletResponse response,
			HttpServletRequest request) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("start_time", request.getParameter("start_time"));
			params.put("end_time", request.getParameter("end_time"));
			params.put("down_start_time",
					request.getParameter("down_start_time"));
			params.put("down_end_time", request.getParameter("down_end_time"));
			params.put("upload_username",
					request.getParameter("upload_username"));
			params.put("download_username",
					request.getParameter("download_username"));
			rdsJudicialAttachmentService.exportExperimentLog(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
