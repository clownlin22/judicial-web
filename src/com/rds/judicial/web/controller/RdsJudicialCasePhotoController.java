package com.rds.judicial.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialCaseAttachmentService;
import com.rds.judicial.service.RdsJudicialCasePhotoService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("judicial/casephoto")
public class RdsJudicialCasePhotoController {

	@Autowired
	private RdsJudicialCasePhotoService cpService;

	@Autowired
	private RdsJudicialCaseAttachmentService attachmentService;

	/**
	 * 根据条件获取案例的基本信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getPrintCaseInfo")
	@ResponseBody
	public RdsJudicialResponse getPrintCaseInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String sql_str = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			sql_str = user.getSql_str();
		}
		params.put("sql_str", sql_str);
		return cpService.getPrintCaseInfo(params);
	}

	@RequestMapping("photoUpload")
	public void upload(@RequestParam MultipartFile[] casephoto,
			@RequestParam String case_code, HttpServletResponse response,
			HttpSession session) throws Exception {
		Map<String, Object> result = cpService
				.uploadTempPhoto(casephoto, case_code, session);
		response.setContentType("text/html; charset=utf-8");
		if ((boolean)result.get("success")) {
			response.getWriter().print(
					"{\"success\":true,\"msg\":\"" + "OK" + "\"}");
		} else
			response.getWriter().print(
					"{\"success\":false,\"msg\":\"" + result.get("msg")+ "\"}");
	}

	@RequestMapping("getCasePhoto")
	public void getCasePhoto(HttpSession session, HttpServletResponse response) {
		cpService.getImg(response, session.getAttribute("case_photo_path").toString());
	}

	@RequestMapping("turnImg")
	@ResponseBody
	public Map<String, Object> turnImg(@RequestBody Map<String, Object> params,
			HttpSession session) {
			params.put("path", session.getAttribute("case_photo_path").toString());
			return cpService.turnImg(params);
	}

	@RequestMapping("getCaseReceiver")
	@ResponseBody
	public Map<String, Object> getCaseReceiver(@RequestBody Map<String, Object> params	,HttpSession session){
		return cpService.getCaseReceiver(params,session);
	}
	
	@RequestMapping("getCasePhotoInServer")
		public void getCasePhotoInServer(@RequestParam String attachment_path,HttpServletResponse response){
		attachmentService.getImg(response, attachment_path);
	}
	
	@RequestMapping("savephoto")
	@ResponseBody
	public Map<String, Object> savephoto(@RequestBody Map<String, Object> params,HttpSession session){
		return cpService.savephoto(params,session);
	}
	@RequestMapping("getAllCasePhoto")
	@ResponseBody
	public Map<String, Object> getAllCasePhoto(@RequestBody Map<String, Object> params){
		return cpService.getAllCasePhoto(params);
	}
}
