package com.rds.bacera.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.bacera.model.RdsBaceraCaseAttachmentModel;
import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.bacera.service.RdsBaceraDocumentAppCooService;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;

@Controller
@RequestMapping("bacera/documentAppCoo")
public class RdsBaceraDocumentAppCooController extends RdsBaceraAbstractController {

	@Setter
	@Autowired

	private RdsBaceraDocumentAppCooService rdsBaceraDocumentAppCooService;
	@Setter
	@Autowired
	private RdsBaceraBoneAgeService rdsBoneAgeService;

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String USER_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "user_permit");
	
	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params)
			throws Exception {
		return 0;
	}

	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		String create_per = user.getUserid();
		// 判断session过期
		if (StringUtils.isEmpty(create_per)) {
			Map<String, Object> resultTemp = new HashMap<String, Object>();
			resultTemp.put("success", true);
			resultTemp.put("result", false);
			resultTemp.put("message", "登录状态失效，请重新登录！");
			return resultTemp;
		}
		if (!(USER_PERMIT.contains(user.getUsercode()) || null != params.get("finance")))
			params.put("create_per", create_per);
		return rdsBaceraDocumentAppCooService.queryAllPage(this.pageSet(params));
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsBaceraDocumentAppCooService.delete(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}

	}

	@RequestMapping("delAttachment")
	@ResponseBody
	public Object delAttachment(@RequestBody Map<String, Object> params) {
		try {
			rdsBaceraDocumentAppCooService.delAttachment(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.DELETE_FAILED);
		}

	}

	/**
	 * 保存案例信息
	 */
	@RequestMapping("saveDocumentAppCoo")
	@ResponseBody
	public Object saveDocumentAppCoo(@RequestParam int[] filetype,
			@RequestParam MultipartFile[] files,
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultTemp = new HashMap<String, Object>();
		//判断项目编号是否存在
		if(rdsBaceraDocumentAppCooService.queryNumExit(params)>0)
		{
			resultTemp.put("success", true);
			resultTemp.put("result", false);
			resultTemp.put("message", "项目编号已存在，请重新输入！");
			return resultTemp;
		}
		// 随机案例id
		String id = UUIDUtil.getUUID();

		// 案例登记人
		String create_per = "";
		// 获取该案例登记人信息
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		create_per = user.getUserid();

		// 判断session过期
		if (StringUtils.isEmpty(create_per)) {
			resultTemp.put("success", true);
			resultTemp.put("result", false);
			resultTemp.put("message", "登录状态失效，请重新登录！");
			return resultTemp;
		}
		params.put("create_per", create_per);
		params.put("id", id);
		// 上传图片信息
		params.put("filetype", filetype);
		params.put("files", files);

		// 案例上传
		Map<String, Object> result = (Map<String, Object>) rdsBaceraDocumentAppCooService
				.saveCaseInfo(params, user);
		return result;
	}

	/**
	 * 修改案例信息
	 */
	@RequestMapping("updateCaseInfo")
	@ResponseBody
	public Object updateCaseInfo(@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {

		// 案例登记人
		String create_per = "";
		// 获取该案例登记人信息
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		create_per = user.getUserid();
		// 判断session过期
		if (StringUtils.isEmpty(create_per)) {
			return false;
		}
		params.put("create_per", create_per);
		Map<String, Object> resultMap = new HashMap<>();
		//判断更新案例时 项目编号是否修改为重复项目编号
		if(rdsBaceraDocumentAppCooService.queryNumExit(params)==0)
		{
			Map<String, Object> map= new HashMap<>();
			map.put("num", params.get("num"));
			if(rdsBaceraDocumentAppCooService.queryNumExit(map)>0)
			{
				resultMap.put("success", true);
				resultMap.put("result", false);
				resultMap.put("message", "项目编号已存在，请重新输入！");
				return resultMap;
			}
		}
		resultMap = rdsBaceraDocumentAppCooService.updateCaseInfo(params, user);
		return resultMap;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 */
	@RequestMapping("exportDocumentAppCoo")
	public void exportDocumentAppCoo(HttpServletResponse response,
			HttpServletRequest request, HttpSession session) {
		try {
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			String create_per = user.getUserid();
			Map<String, Object> params = new HashMap<String, Object>();
			if (!(USER_PERMIT.contains(user.getUsercode())))
				params.put("create_per", create_per);
			String num = request.getParameter("num");
			params.put("num", num);
			String client = request.getParameter("client");
			params.put("client", client);
			String phone = request.getParameter("phone");
			params.put("phone", phone);
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			String create_pername = request.getParameter("create_pername");
			params.put("create_pername", create_pername);
			String case_close = request.getParameter("case_close");
			params.put("case_close", case_close);
			params.put("client_starttime",
					request.getParameter("client_starttime"));
			params.put("client_endtime", request.getParameter("client_endtime"));
			params.put("accept_starttime", request.getParameter("accept_starttime"));
			params.put("accept_endtime", request.getParameter("accept_endtime"));
			params.put("appraisal_end_starttime", request.getParameter("appraisal_end_starttime"));
			params.put("appraisal_end_endtime", request.getParameter("appraisal_end_endtime"));
			params.put("paragraphtime_endtime", request.getParameter("paragraphtime_endtime"));
			params.put("paragraphtime_starttime", request.getParameter("paragraphtime_starttime"));
			params.put("express_starttime", request.getParameter("express_starttime"));
			params.put("express_endtime", request.getParameter("express_endtime"));
			params.put("reportif", request.getParameter("reportif"));
			params.put("expressnum", request.getParameter("expressnum"));
			params.put(
					"cancelif",
					"0".equals(request.getParameter("cancelif")) ? "" : request
							.getParameter("cancelif"));
			String agent = request.getParameter("agent");
			params.put("agent", agent);
			params.put("areacode",
					"null".equals(request.getParameter("areacode")) ? ""
							: request.getParameter("areacode"));
			params.put("userCode", user.getUsercode());
			rdsBaceraDocumentAppCooService.exportDocumentAppCoo(params, response);;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载文件
	 */
	@RequestMapping("download")
	public void download(HttpServletResponse response,
			@RequestParam String attachment_id) {
		rdsBaceraDocumentAppCooService.download(response, attachment_id);
	}

	/**
	 * 下载文件
	 */
	@RequestMapping("queryAttacmByDocumentAppCoo")
	@ResponseBody
	public List<RdsBaceraCaseAttachmentModel> queryAttacmByDocumentAppCoo(
			@RequestBody Map<String, Object> params) {
		return rdsBaceraDocumentAppCooService.queryAttacmByDocumentAppCoo(params);
	}

	@RequestMapping("uploadDocumentAppCooAttachment")
	@ResponseBody
	public Object uploadDocumentAppCooAttachment(@RequestParam int[] filetype,
			@RequestParam MultipartFile[] files,
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		try {
			rdsBaceraDocumentAppCooService.uploadDocumentAppCooAttachment(
					params.get("id").toString(),
					params.get("num").toString(), files, filetype,
					user.getUserid());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
