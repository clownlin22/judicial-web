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
import com.rds.bacera.service.RdsBaceraAbilityService;
import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;

@Controller
@RequestMapping("bacera/ability")
public class RdsBaceraAbilityController extends RdsBaceraAbstractController {

	@Setter
	@Autowired
	private RdsBaceraAbilityService rdsBaceraAbilityService;

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
		String case_in_per = user.getUserid();
		// 判断session过期
		if (StringUtils.isEmpty(case_in_per)) {
			Map<String, Object> resultTemp = new HashMap<String, Object>();
			resultTemp.put("success", true);
			resultTemp.put("result", false);
			resultTemp.put("message", "登录状态失效，请重新登录！");
			return resultTemp;
		}
		if (!(USER_PERMIT.contains(user.getUsercode()) || null != params.get("finance")))
			params.put("case_in_per", case_in_per);
		return rdsBaceraAbilityService.queryAllPage(this.pageSet(params));
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsBaceraAbilityService.delete(params);
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
			rdsBaceraAbilityService.delAttachment(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.DELETE_FAILED);
		}

	}

	/**
	 * 保存案例信息
	 */
	@RequestMapping("saveAbilityInfo")
	@ResponseBody
	public Object saveCaseInfo(@RequestParam int[] filetype,
			@RequestParam MultipartFile[] files,
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> resultTemp = new HashMap<String, Object>();
		//判断项目编号是否存在
//		if(rdsBaceraAbilityService.queryNumExit(params)>0)
//		{
//			resultTemp.put("success", true);
//			resultTemp.put("result", false);
//			resultTemp.put("message", "项目编号已存在，请重新输入！");
//			return resultTemp;
//		}
		// 随机案例id
		String ability_id = UUIDUtil.getUUID();

		// 案例登记人
		String case_in_per = "";
		// 获取该案例登记人信息
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		case_in_per = user.getUserid();

		// 判断session过期
		if (StringUtils.isEmpty(case_in_per)) {
			resultTemp.put("success", true);
			resultTemp.put("result", false);
			resultTemp.put("message", "登录状态失效，请重新登录！");
			return resultTemp;
		}
		params.put("case_in_per", case_in_per);
		params.put("ability_id", ability_id);
		// 上传图片信息
		params.put("filetype", filetype);
		params.put("files", files);

		// 案例上传
		Map<String, Object> result = (Map<String, Object>) rdsBaceraAbilityService
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
		String case_in_per = "";
		// 获取该案例登记人信息
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		case_in_per = user.getUserid();
		// 判断session过期
		if (StringUtils.isEmpty(case_in_per)) {
			return false;
		}
		params.put("case_in_per", case_in_per);
		Map<String, Object> resultMap = new HashMap<>();
		//判断更新案例时 项目编号是否修改为重复项目编号
		if(rdsBaceraAbilityService.queryNumExit(params)==0)
		{
			Map<String, Object> map= new HashMap<>();
			map.put("ability_num", params.get("ability_num"));
			if(rdsBaceraAbilityService.queryNumExit(map)>0)
			{
				resultMap.put("success", true);
				resultMap.put("result", false);
				resultMap.put("message", "项目编号已存在，请重新输入！");
				return resultMap;
			}
		}
		resultMap = rdsBaceraAbilityService.updateCaseInfo(params, user);
		return resultMap;
	}

	/**
	 * 
	 * @param response
	 * @param request
	 */
	@RequestMapping("exportAbility")
	public void exportAbility(HttpServletResponse response,
			HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String num = request.getParameter("num");
			params.put("num", num);
			String name = request.getParameter("name");
			params.put("name", name);
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			params.put("identify_starttime",
					request.getParameter("identify_starttime"));
			params.put("identify_endtime", request.getParameter("identify_endtime"));
			params.put("reportif", request.getParameter("reportif"));
			params.put(
					"cancelif",
					"0".equals(request.getParameter("cancelif")) ? "" : request
							.getParameter("cancelif"));
			String agent = request.getParameter("agent");
			params.put("agent", agent);
			params.put("areacode",
					"null".equals(request.getParameter("areacode")) ? ""
							: request.getParameter("areacode"));
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			rdsBaceraAbilityService.exportAbility(params, response);
			;
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
		rdsBaceraAbilityService.download(response, attachment_id);
	}

	/**
	 * 下载文件
	 */
	@RequestMapping("queryAttacmByAbility")
	@ResponseBody
	public List<RdsBaceraCaseAttachmentModel> queryAttacmByAbility(
			@RequestBody Map<String, Object> params) {
		return rdsBaceraAbilityService.queryAttacmByAbility(params);
	}

	@RequestMapping("uploadAbilityAttachment")
	@ResponseBody
	public Object uploadAbilityAttachment(@RequestParam int[] filetype,
			@RequestParam MultipartFile[] files,
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		try {
			rdsBaceraAbilityService.uploadAbilityAttachment(
					params.get("ability_id").toString(),
					params.get("ability_num").toString(), files, filetype,
					user.getUserid());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
