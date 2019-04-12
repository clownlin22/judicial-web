package com.rds.children.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.children.model.RdsChildrenCasePhotoModel;
import com.rds.children.model.RdsChildrenCustodyInfoModel;
import com.rds.children.model.RdsChildrenGatherInfoModel;
import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.service.RdsChildrenRegisterService;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialMessageModel;
import com.rds.judicial.web.controller.RdsJudicialPrintController;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * 儿童基因库登记
 * 
 * @author
 *
 */
@RequestMapping("children/register")
@Controller
public class RdsChildrenRegisterController {

	@Autowired
	private RdsChildrenRegisterService rdsChildrenRegisterService;

	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private IdentityService identityService;
	
	Logger logger = Logger.getLogger(RdsJudicialPrintController.class);
	
	// 配置文件地址
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	// 附件存放地址
	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "children_path") ;


	/**
	 * 分页查询儿童基因库
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("getCaseInfo")
	@ResponseBody
	private RdsChildrenResponse getCaseInfo(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		String userid = "";
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			userid = user.getUserid();
		}
		if (StringUtils.isEmpty(userid)) {
			return null;
		}
		if (!"admin".equals(user.getUsercode()))
			params.put("userid", userid);
		return rdsChildrenRegisterService.getCaseInfo(params);
	}

	/**
	 * 分页查询审核案例信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("getVerifyCaseInfo")
	@ResponseBody
	private RdsChildrenResponse getVerifyCaseInfo(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		return rdsChildrenRegisterService.getCaseInfo(params);
	}

	@RequestMapping("getCustodyInfo")
	@ResponseBody
	public List<RdsChildrenCustodyInfoModel> getCustodyInfo(
			@RequestBody Map<String, Object> params) {
		return rdsChildrenRegisterService.getCustodyInfo(params);
	}

	@RequestMapping("getGatherInfo")
	@ResponseBody
	public RdsChildrenGatherInfoModel getGatherInfo(
			@RequestBody Map<String, Object> params) {
		return rdsChildrenRegisterService.getGatherInfo(params);
	}

	@RequestMapping("deleteCaseInfo")
	@ResponseBody
	public boolean deleteCaseInfo(@RequestBody Map<String, Object> params) {
		return rdsChildrenRegisterService.deleteCaseInfo(params);
	}

	@RequestMapping("exsitCaseCode")
	@ResponseBody
	public boolean exsitCaseCode(@RequestBody Map<String, Object> params) {
		return rdsChildrenRegisterService.exsitCaseCode(params);
	}

	@RequestMapping("saveCaseInfo")
	@ResponseBody
	public Object saveCaseInfo(@RequestParam String[] filetype,
			@RequestParam MultipartFile[] files,
			@RequestParam String[] custody_name,
			@RequestParam String[] custody_id_number,
			@RequestParam String[] custody_call,
			@RequestParam String[] custody_phone,
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		// 案例id
		String case_id = UUIDUtil.getUUID();
		params.put("case_id", case_id);
		// 登记人id
		String case_in_per = "";
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			case_in_per = user.getUserid();
		}
		params.put("case_in_per", case_in_per);
		params.put("userid", case_in_per);
		if (StringUtils.isEmpty(case_in_per)) {
			return false;
		}
		// 结果集
		Map<String, Object> result = new HashMap<>();
		//新增是判断样本编号是否存在
		if(!"".equals(params.get("sample_code").toString())){
			Map<String,Object> maps = new HashMap<>();
			maps.put("sample_code", params.get("sample_code"));
			if(rdsChildrenRegisterService.exsitSampleCode(maps))
			{
				result.put("result", false);
				result.put("success", true);
				result.put("msg", "案例样本编号已存在！");
				return result;
			}
		}
		// 案例照片信息
		params.put("files", files);
		MultipartFile[] photo = (MultipartFile[]) params.get("files");
		if (photo.length != 2) {
			result.put("result", false);
			result.put("success", true);
			result.put("msg", "案例照片有误！");
			return result;
		} else {
			if (photo[0].getOriginalFilename().equals(
					photo[1].getOriginalFilename())) {
				result.put("result", false);
				result.put("success", true);
				result.put("msg", "案例上传照片相同！");
				return result;
			}
		}
		params.put("filetype", filetype);
		// 监护人信息
		params.put("custody_name", custody_name);
		params.put("custody_id_number", custody_id_number);
		params.put("custody_call", custody_call);
		params.put("custody_phone", custody_phone);
		result = rdsChildrenRegisterService.saveCaseInfo(params);
		// 判断如果新增失败，直接给出提示
		if (!(boolean) result.get("result"))
			return result;
		// 案例流程控制map
		Map<String, Object> variables = new HashMap<String, Object>();
		// 判断是否保存并提交审核
		if (null != params.get("submitFlag")) {
			boolean result1 = rdsActivitiJudicialService.runByChildCaseCode(
					case_id, variables, user);
			if (result1) {
				Map<String, Object> resultTemp = new HashMap<String, Object>();
				resultTemp.put("case_id", case_id);
				resultTemp.put("verify_state", 1);
				// 更新登记表的案例状态
				rdsChildrenRegisterService.updateCaseState(resultTemp);
				resultTemp.put("result", true);
				resultTemp.put("success", true);
				resultTemp.put("msg", "案例新增成功！");
				return resultTemp;
			} else {
				Map<String, Object> resultTemp = new HashMap<String, Object>();
				resultTemp.put("result", false);
				resultTemp.put("success", true);
				resultTemp.put("msg", "流程提交失败，请联系管理员");
				return resultTemp;
			}
		} else {
			return result;
		}
	}

	@RequestMapping("updateCaseInfo")
	@ResponseBody
	public Object updateCaseInfo(@RequestBody Map<String, Object> params,
			HttpSession session) {
		String case_in_per = "";
		String case_id = params.get("case_id").toString();
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			case_in_per = user.getUserid();
		}
		params.put("case_in_per", case_in_per);
		params.put("userid", case_in_per);
		if (StringUtils.isEmpty(case_in_per)) {
			return false;
		}
		Map<String, Object> result = new HashMap<>();
		//修改时判断样本编号是否存在
		if(!rdsChildrenRegisterService.exsitSampleCode(params))
		{
			Map<String,Object> maps = new HashMap<>();
			maps.put("sample_code", params.get("sample_code"));
			if(rdsChildrenRegisterService.exsitSampleCode(maps)){
				result.put("result", false);
				result.put("success", true);
				result.put("msg", "案例样本编号已存在！");
				return result;
			}
		}
		
		result = rdsChildrenRegisterService.updateCaseInfo(params);
		// 判断如果新增失败，直接给出提示
		if (!(boolean) result.get("result"))
			return result;
		// 案例流程控制map
		Map<String, Object> variables = new HashMap<String, Object>();
		// 判断是否保存并提交审核
		if (null != params.get("submitFlag")) {
			boolean result1 = rdsActivitiJudicialService.runByChildCaseCode(
					case_id, variables, user);
			if (result1) {
				Map<String, Object> resultTemp = new HashMap<String, Object>();
				resultTemp.put("case_id", case_id);
				resultTemp.put("verify_state", 1);
				// 更新登记表的案例状态
				rdsChildrenRegisterService.updateCaseState(resultTemp);

				resultTemp.put("result", true);
				resultTemp.put("success", true);
				resultTemp.put("msg", "案例更新成功！");
				return resultTemp;
			} else {
				Map<String, Object> resultTemp = new HashMap<String, Object>();
				resultTemp.put("result", false);
				resultTemp.put("success", true);
				resultTemp.put("msg", "流程提交失败，请联系管理员");
				return resultTemp;
			}
		} else {
			// 更新基表案例状态
			Map<String, Object> resultTemp = new HashMap<String, Object>();
			resultTemp.put("case_id", case_id);
			resultTemp.put("verify_state", 0);
			// 更新登记表的案例状态
			rdsChildrenRegisterService.updateCaseState(resultTemp);
			return result;
		}
	}

	/**
	 * 修改案例信息
	 */
	@RequestMapping("updateCaseState")
	@ResponseBody
	public boolean updateCaseState(@RequestBody Map<String, Object> params) {
		return rdsChildrenRegisterService.updateCaseState(params);
	}

	@RequestMapping("getTariff")
	@ResponseBody
	public Map<String, Object> getTariff() {
		return rdsChildrenRegisterService.getTariff();
	}

	@RequestMapping("photoUpload")
	public void photoUpload(RdsChildrenCasePhotoModel pmodel,
			@RequestParam MultipartFile[] headPhoto,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
		}
		pmodel.setUpload_user(user.getUserid());
		if (null == pmodel.getPhoto_type() || "".equals(pmodel.getPhoto_type()))
			pmodel.setPhoto_type("2");
		Map<String, Object> pmap = rdsChildrenRegisterService.photoUpload(
				pmodel, headPhoto);
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(JSONObject.fromObject(pmap).toString());
		response.flushBuffer();
	}

	@RequestMapping("exportInfo")
	public void exportInfo(@RequestParam String case_code,
			@RequestParam String starttime, @RequestParam String endtime,
			@RequestParam String child_name, @RequestParam Integer is_delete,
			HttpServletResponse response) {
		rdsChildrenRegisterService.exportInfo(case_code, starttime, endtime,
				child_name, is_delete, response);
	}

	/**
	 * 获取附件列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping("queryCasePhoto")
	@ResponseBody
	public List<RdsChildrenCasePhotoModel> queryCasePhoto(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		return rdsChildrenRegisterService.queryCasePhoto(params);
	}

	/**
	 * 分页获取附件列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping("queryPageCasePhoto")
	@ResponseBody
	public RdsChildrenResponse queryPageCasePhoto(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		return rdsChildrenRegisterService.queryPageCasePhoto(params);
	}

	/**
	 * 获取图片文件
	 */
	@RequestMapping("getImg")
	public void getImg(HttpServletResponse response, String filename) {
		rdsChildrenRegisterService.getImg(response, filename);
	}

	@RequestMapping("yes")
	@ResponseBody
	public RdsJudicialMessageModel yesVerify(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (user == null) {
			return new RdsJudicialMessageModel(false, "请重新登录", false);
		}
		if (rdsChildrenRegisterService.updateCaseState(params)) {
			// 审核通过签收流程
			String taskId = params.get("taskId").toString();
			Task task = taskService.createTaskQuery().taskId(taskId)
					.singleResult();
			if (task == null) {
				return new RdsJudicialMessageModel(false, "该流程步不存在", false);
			}
			Map<String, Object> variables = new HashMap<String, Object>();
			// 审核通过
			variables.put("ispass", 1);
			if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
				taskService.claim(taskId, user.getUsername());// todo:需要改成userid
				taskService.complete(taskId, variables);
			} else {
				taskService.complete(taskId, variables);
			}
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
		if (rdsChildrenRegisterService.updateCaseState(params)) {
			params.put("verify_baseinfo_person", user.getUserid());
			String taskId = params.get("taskId").toString();
			String processInstanceId = params.get("processInstanceId")
					.toString();
			String comment = params.get("verify_baseinfo_remark").toString();
			Task task = taskService.createTaskQuery().taskId(taskId)
					.singleResult();
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
		} else {
			return new RdsJudicialMessageModel(false, "审核不通过失败,请联系管理员", false);
		}

	}
	/**
	 * 下载文件word
	 */
	@RequestMapping("downWord")
	public void downWord(HttpServletResponse response,
			@RequestParam String case_id,@RequestParam String case_code  ,HttpServletRequest request)
				throws Exception {
		FileInputStream hFile = null;
		OutputStream toClient = null;
		File directory = new File(FILE_PATH);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		String file_name =ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + ".doc";
	 
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("case_code", case_code);
			params.put("file_name", file_name);
				rdsChildrenRegisterService.downWord(response, case_id,case_code,file_name);
			File file = new File(file_name);
			hFile = new FileInputStream(file);
			String fileName = file.getName();
			// 得到文件大小
			int i = hFile.available();
			byte data[] = new byte[i];
			// 读数据
			hFile.read(data);
			// 设置response的Header
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(
							fileName.replaceAll(" ", "").getBytes("utf-8"),
							"iso8859-1"));
			// 得到向客户端输出二进制数据的对象
			toClient = response.getOutputStream();
			// 输出数据
			toClient.write(data);
			toClient.flush(); 
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			toClient.close();
			hFile.close();
		}
	}

	/**
	 * 下载文件
	 */
	@RequestMapping("download")
	public void download(HttpServletResponse response,
			@RequestParam String case_id,@RequestParam String case_code) {
		rdsChildrenRegisterService.download(response, case_id,case_code);
	}
	
	/**
	 * 下载图片
	 */
	@RequestMapping("downloadPhoto")
	public void downloadPhoto(HttpServletResponse response,
			@RequestParam String photo_id) {
		rdsChildrenRegisterService.downloadPhoto(response, photo_id);
	}

}
