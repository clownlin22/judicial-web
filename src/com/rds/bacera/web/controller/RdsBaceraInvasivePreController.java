package com.rds.bacera.web.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.bacera.model.RdsBaceraInvasivePhotoModel;
import com.rds.bacera.service.BaceraInvasiveOwnpersonToEmailsService;
import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.bacera.service.RdsBaceraHospitalAreaNameService;
import com.rds.bacera.service.RdsBaceraInvasivePreService;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.judicial.model.RdsJudicialMessageModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/invasivePre")
public class RdsBaceraInvasivePreController extends RdsBaceraAbstractController {
	@Autowired
	private TaskService taskService;

	@Autowired
	private IdentityService identityService;

	@Setter
	@Autowired
	private RdsBaceraInvasivePreService rdsInvasivePreService;
	
	@Setter
	@Autowired
	private RdsBaceraHospitalAreaNameService rdsBaceraHospitalAreaNameService;

	@Setter
	@Autowired
	private RdsBaceraBoneAgeService rdsBoneAgeService;

	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;
	
	@Setter
	@Autowired
	private BaceraInvasiveOwnpersonToEmailsService raceraInvasiveOwnpersonToEmailsService;
	
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";
	
	// 附件存放地址
	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
					FILE_PATH, "invasive_head_photo");

	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params)
			throws Exception {
		return 0;
	}

	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (user == null) {
			return new RdsJudicialMessageModel(false, "请重新登录", false);
		}
		if (1 == (Integer) params.get("flag")) {
			params.put("invasive_starttime", params.get("verify_starttime"));
			params.put("invasive_endtime", params.get("verify_endtime"));
			params.put("invasive_sample_starttime",
					params.get("verify_sample_starttime"));
			params.put("invasive_sample_endtime",
					params.get("verify_sample_endtime"));
			return rdsInvasivePreService.queryAllPage(params);
		} else {
			params.put("inputperson", user.getUserid());
			return rdsInvasivePreService.queryAllPage(params);
		}

	}

	@RequestMapping("invasivePreYes")
	@ResponseBody
	public RdsJudicialMessageModel invasivePreYes(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (user == null) {
			return new RdsJudicialMessageModel(false, "请重新登录", false);
		}
		// params.put("verify_baseinfo_person", user.getUserid());

		int map = rdsInvasivePreService.yesInvasivePreVerify(params);

		if (map > 0) {
			// 审核通过签收流程

			String taskId = params.get("task_id").toString();
			params.put("verify_baseinfo_person", user.getUserid());
			String processInstanceId = params.get("processInstanceId")
					.toString();
			String comment = params.get("verifyRemark").toString();
			Task task = taskService.createTaskQuery().taskId(taskId)
					.singleResult();
			if (task == null) {
				return new RdsJudicialMessageModel(false, "该流程步不存在", false);
			}
			identityService.setAuthenticatedUserId(user.getUsername());// todo:需要改成userid
			taskService.addComment(taskId, processInstanceId, comment);
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("ispass", 1);
			if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
				taskService.claim(taskId, user.getUsername());// todo:需要改成userid
				taskService.complete(taskId, variables);
			} else {
				taskService.complete(taskId, variables);
			}
			return new RdsJudicialMessageModel(true, "审核通过!", true);
		} else
			return new RdsJudicialMessageModel(false, "审核通过失败,请联系管理员", false);
	}

	@RequestMapping("invasivePreNo")
	@ResponseBody
	public RdsJudicialMessageModel invasivePreNo(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (user == null) {
			return new RdsJudicialMessageModel(false, "请重新登录", false);
		}
		params.put("verify_baseinfo_person", user.getUserid());

		String taskId = params.get("task_id").toString();
		String processInstanceId = params.get("processInstanceId").toString();
		String comment = params.get("verifyRemark").toString();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
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

		if (rdsInvasivePreService.noInvasivePreVerify(params) > 0)
			return new RdsJudicialMessageModel(true, "审核不通过成功", true);
		return new RdsJudicialMessageModel(false, "审核不通过失败,请联系管理员", false);
	}

	@RequestMapping("queryAllExpress")
	@ResponseBody
	public Object queryAllExpress(@RequestBody Map<String, Object> params)
			throws Exception {

		return rdsInvasivePreService.queryAllPage(params);

	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			Map<String, Object> map = new HashMap<>();
			rdsInvasivePreService.delete(params);
			map.put("case_id", params.get("id").toString());
			List<RdsBaceraInvasivePhotoModel> a=rdsInvasivePreService.getAttachMent(map);
			String photo_path=a.get(0).getPhoto_path();
			map.put("photo_path", photo_path);
			rdsInvasivePreService.deleteFile(map);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}

	}

	/**
	 * 查重案例编号
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exsitCaseCode")
	@ResponseBody
	public boolean exsitCaseCode(@RequestBody Map<String, Object> params) {
		return rdsInvasivePreService.exsitCaseCode(params);
	}
/**
 * 案例作废
 * @param params
 * @param request
 * @return
 */
	@RequestMapping("onCancel")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		
			int result;
			try {
				result = rdsInvasivePreService.update(params);
				if(result>0){
					return this.setModel(true, "操作成功");
					}else{
						return this.setModel(false, "操作失败");
						}
			} catch (Exception e) {
				e.printStackTrace();
				return this.setModel(true, false, e.getMessage());
			}	
			}

	/**
	 * 保存并提交审核
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@Transactional
	@RequestMapping("saveVerify")
	@ResponseBody
	public Object saveVerify(@RequestParam Map<String, Object> params,
			@RequestParam MultipartFile files,
			HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();

		try {
			String id = RdsUpcUUIDUtil.getUUID();
			params.put("id", id);
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			params.put("inputperson", user.getUserid());
			params.put("case_type", "无创产前");
			params.put("files", files);
			result = rdsInvasivePreService.saveCaseInfo(params, user);
			if ((boolean) result.get("result")) {
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("ispass", 1);
				// 更新提交审核流程
				rdsActivitiJudicialService.runByCaseNum(id, variables, user);

				rdsInvasivePreService.updateVerify(params);
				return result;
			} else {
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("result", false);
			result.put("message", "出错啦，请联系管理员！");
			return result;
		}
	}

	@RequestMapping("update")
	@ResponseBody
	private Object update(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {

			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			params.put("inputperson", user.getUserid());
			int result = rdsInvasivePreService.update(params);
			if (result > 0) {

				String taskdefkey = (String) params.get("task_def_key");
				if ("taskRegister".equals(taskdefkey)) {
					String taskId = (String) params.get("task_id");
					Task task = taskService.createTaskQuery().taskId(taskId)
							.singleResult();
					if (task == null) {
						return new RdsJudicialMessageModel(false, "该流程步不存在",
								false);
					}
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("ispass", 1);
					if (task.getAssignee() == null
							|| task.getAssignee().isEmpty()) {
						taskService.claim(taskId, user.getUsername());// todo:需要改成userid
						taskService.complete(taskId, variables);
					} else {
						taskService.complete(taskId, variables);
					}
				}
				return this.setModel(true, "操作成功");
			} else {
				return this.setModel(false, "操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	@RequestMapping("updateVerify")
	@ResponseBody
	private Object updateVerify(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {
			// 案例流程控制
			Map<String, Object> variables = new HashMap<String, Object>();

			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");

			int result = rdsInvasivePreService.updateVerify(params);
			if (result > 0) {
				// 更新提交审核流程
				rdsActivitiJudicialService.runByCaseNum(user.getUserid(),
						variables, user);
				return this.setModel(true, "操作成功");
			} else {
				return this.setModel(false, "操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	/**
	 * 导出无创信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exportInvasiveInfo")
	public void exportInvasiveInfo(HttpServletResponse response,
			HttpServletRequest request, HttpSession session) {
		try {
			if ("1".equals(request.getParameter("check").toString())){
			Map<String, Object> params = new HashMap<String, Object>();
			String num = request.getParameter("num");
			params.put("num", num);
			String name = request.getParameter("name");
			params.put("name", name);
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			params.put("invasive_starttime",
					request.getParameter("invasive_starttime"));
			params.put("invasive_endtime",
					request.getParameter("invasive_endtime"));
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
			String code = request.getParameter("code");
			params.put("code", code);
			params.put("verify_state",
					request.getParameter("verify_state"));
			String hospital = request.getParameter("hospital");
			params.put("hospital", hospital);
			String inspectionunit = request.getParameter("inspectionunit");
			params.put("inspectionunit", inspectionunit);
			params.put("invasive_sample_starttime",
					request.getParameter("invasive_sample_starttime"));
			params.put("invasive_sample_endtime",
					request.getParameter("invasive_sample_endtime"));
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			rdsInvasivePreService.exportInvasiveInfo(params, response);}
			else{
			Map<String, Object> params = new HashMap<String, Object>();
			String num = request.getParameter("num");
			params.put("num", num);
			String name = request.getParameter("name");
			params.put("name", name);
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			params.put("invasive_starttime",
					request.getParameter("invasive_starttime"));
			params.put("invasive_endtime",
					request.getParameter("invasive_endtime"));
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
			String code = request.getParameter("code");
			params.put("code", code);
			params.put("verify_state",
					request.getParameter("verify_state"));
			String hospital = request.getParameter("hospital");
			params.put("hospital", hospital);
			String inspectionunit = request.getParameter("inspectionunit");
			params.put("inspectionunit", inspectionunit);
			params.put("invasive_sample_starttime",
					request.getParameter("invasive_sample_starttime"));
			params.put("invasive_sample_endtime",
					request.getParameter("invasive_sample_endtime"));
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("inputperson", user.getUserid());
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			rdsInvasivePreService.exportInvasiveInfo(params, response);
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("queryInvasivePreFee")
	@ResponseBody
	public Object queryInvasivePreFee(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsInvasivePreService.queryInvasivePreFee(this.pageSet(params));
	}

	/**
	 * 保存骨龄人员对应财务应收金额
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("saveInvasivePreFee")
	@ResponseBody
	public Object saveInvasivePreFee(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Object id = params.get("id");
		if (id == null || "".equals(id)) {
			try {
				// 判断该人员是否配置金额
				if (rdsInvasivePreService.queryInvasivePreFeeCount(params) > 0) {
					return this.setModel(false, "该人员已配置过金额");
				} else {
					params.put("id", RdsUpcUUIDUtil.getUUID());
					RdsUpcUserModel user = (RdsUpcUserModel) request
							.getSession().getAttribute("user");
					params.put("inputperson", user.getUserid());
					int result = rdsInvasivePreService
							.saveInvasivePreFee(params);
					if (result > 0) {
						return this.setModel(true,
								RdsUpcConstantUtil.INSERT_SUCCESS);
					} else {
						return this.setModel(false,
								RdsUpcConstantUtil.INSERT_FAILED);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
						+ "\n" + e.getMessage());
			}
		} else {
			try {
				// 判断是否为当前用户
				if (rdsInvasivePreService.queryInvasivePreFeeCount(params) > 0) {
					RdsUpcUserModel user = (RdsUpcUserModel) request
							.getSession().getAttribute("user");
					params.put("inputperson", user.getUserid());
					int result = rdsInvasivePreService
							.updateInvasivePreFee(params);
					if (result > 0) {
						return this.setModel(true, "操作成功");
					} else {
						return this.setModel(false, "操作成功");
					}
				} else {
					// 判断修改用户后该用户是否已配置
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userid", params.get("userid"));
					if (rdsInvasivePreService.queryInvasivePreFeeCount(map) > 0) {
						return this.setModel(false, "该人员已配置过金额");
					} else {
						int result = rdsInvasivePreService
								.updateInvasivePreFee(params);
						if (result > 0) {
							return this.setModel(true, "操作成功");
						} else {
							return this.setModel(false, "操作成功");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return this.setModel(true, false, e.getMessage());
			}
		}
	}

	@RequestMapping("deleteInvasivePreFee")
	@ResponseBody
	public Object deleteInvasivePreFee(@RequestBody Map<String, Object> params) {
		try {
			rdsInvasivePreService.deleteInvasivePreFee(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION);
		}

	}

	/**
	 * 保存案例信息
	 */

	@RequestMapping("saveCaseInfo")
	@ResponseBody
	public Object saveCaseInfo(@RequestParam Map<String, Object> params,
			@RequestParam MultipartFile files,
			HttpServletRequest request) throws Exception {
		// 随机案例id
		String id = UUIDUtil.getUUID();
		params.put("id", id);
		// 案例登记人
		String case_in_per = "";
		// 获取该案例登记人信息
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("inputperson", user.getUserid());
		params.put("case_type", "无创产前");
		params.put("files", files);
		case_in_per = user.getUserid();
		// 判断session过期
		if (StringUtils.isEmpty(case_in_per)) {
			Map<String, Object> resultTemp = new HashMap<String, Object>();
			resultTemp.put("success", true);
			resultTemp.put("result", false);
			resultTemp.put("message", "登录状态失效，请重新登录！");
			return resultTemp;
		}
		Map<String, Object> result = rdsInvasivePreService.saveCaseInfo(params,
				user);
		if ((boolean) result.get("result")) {
			// 案例流程控制map
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("ispass", 1);
			// 更新提交审核流程
			boolean result1 = rdsActivitiJudicialService.runByCaseNum(id,
					variables, user);
			if (result1) {
				Map<String, Object> resultTemp = new HashMap<String, Object>();
				resultTemp.put("id", id);
				// 更新登记表的案例状态
				// rdsInvasivePreService
				// .updateVerify(resultTemp);
				resultTemp.put("result", true);
				resultTemp.put("success", true);
				resultTemp.put("message", "新增成功！");
				return resultTemp;
			} else {
				Map<String, Object> resultTemp = new HashMap<String, Object>();
				resultTemp.put("result", false);
				resultTemp.put("success", true);
				resultTemp.put("message", "流程提交失败，请联系管理员");
				return resultTemp;
			}
		} else {
			return result;
		}
	}

	/**
	 * 修改案例审核状态
	 */
	@RequestMapping("updateVerifyAll")
	@ResponseBody
	public Object updateVerifyAll(@RequestBody Map<String, Object> params)
			throws Exception {
		if (rdsInvasivePreService.updateVerifyAll(params) > 0) {
			return this.setModel(true, "操作成功");
		}
		return this.setModel(true, "操作成功");
	}
	
	
	/**
	 * 查询配置归属地医院
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryallpageHospitalAreaName")
	@ResponseBody
	public Object queryallpageHospitalAreaName(@RequestBody Map<String, Object> params)
			throws Exception {
		String areacode=params.get("areacode")==null?"":params.get("areacode").toString();
		if(null !=areacode&!"".equals(areacode)){
		areacode=areacode.substring(0,4);
		params.put("areacode", areacode);}
			return rdsBaceraHospitalAreaNameService.queryAllPage(params);

		}
	@RequestMapping("getHospital")
	@ResponseBody
	public Object getHospital(
			HttpServletRequest request) {
		String areaname = request.getParameter("areaname");

		try {
			String hospital= rdsBaceraHospitalAreaNameService.queryHospital(areaname);

			return this.setModel(true, hospital);
		
		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}

	}

	@RequestMapping("saveHospitalAreaName")
	@ResponseBody
	public Object saveHospitalAreaName(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		Object id = params.get("id");
		if (id == null || id.equals("")) {
			try {
			int a=	rdsBaceraHospitalAreaNameService.queryOne(params);
			if(a>0)return  this.setModel(false, "已有重复配置，请查看！！");
			else return insertHospitalAreaName(params,request);
			} catch (Exception e) {
				e.printStackTrace();
				return this.setModel(true, false, e.getMessage());
			}
		} else {
			return updateHospitalAreaName(params,request);
		}
	}

    /**
     * 新增归属地医院配置
     * @param params
     * @param request
     * @return
     */
	@RequestMapping("insertHospitalAreaName")
	@ResponseBody
	private Object insertHospitalAreaName(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {
			String id = RdsUpcUUIDUtil.getUUID();
			params.put("id", id);
			int result =rdsBaceraHospitalAreaNameService.insert(params);
			if(result>0){
				return this.setModel(true, "添加成功");
				}else{
					return this.setModel(false, "添加失败");
					}
		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}
		
	}
	/**
	 * 删除医院配置信息
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("deleteHospitalAreaName")
	@ResponseBody
	private Object deleteHospitalAreaName(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {
			int result =rdsBaceraHospitalAreaNameService.delete(params);
			if(result>0){
				return this.setModel(true, "删除成功");
				}else{
					return this.setModel(false, "删除失败");
					}
		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}
	}
	/**
	 * 修改医院归属地配置信息
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("updateHospitalAreaName")
	@ResponseBody
	private Object updateHospitalAreaName(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {
			int result =rdsBaceraHospitalAreaNameService.update(params);
			if(result>0){
				return this.setModel(true, "修改成功");
				}else{
					return this.setModel(false, "修改失败");
					}
		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}
	}
	/**
	 * 查询配置归属人邮箱
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryallpageOwnpersonToEmails")
	@ResponseBody
	public Object queryallpageOwnpersonToEmails(@RequestBody Map<String, Object> params)
			throws Exception {
//		String ownperson=params.get("ownperson")==null?"":params.get("ownperson").toString();
//		if(null !=ownperson&!"".equals(ownperson)){
//			ownperson=ownperson.substring(0,4);
//		params.put("ownperson", ownperson);}
			return raceraInvasiveOwnpersonToEmailsService.queryAllPage(params);

		}
	@RequestMapping("getOwnperson")
	@ResponseBody
	public Object getOwnperson(
			HttpServletRequest request) {
		String ownpersonname = request.getParameter("ownpersonname");

		try {
			String hospital= raceraInvasiveOwnpersonToEmailsService.queryOwnperson(ownpersonname);

			return this.setModel(true, hospital);
		
		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}

	}
/**
 * 新增或修改市场归属人邮箱配置
 * @param params
 * @param request
 * @return
 */
	@RequestMapping("saveOwnpersonToEmails")
	@ResponseBody
	public Object saveOwnpersonToEmails(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		Object id = params.get("id");
		if (id == null || id.equals("")) {
			int result;
			try {
				 id = RdsUpcUUIDUtil.getUUID();
				params.put("id", id);
				int a=raceraInvasiveOwnpersonToEmailsService.queryExsit(params);
				if ( a>0) return this.setModel(false, "此归属人已存在配置记录，请查看或修改！");
				else {result = raceraInvasiveOwnpersonToEmailsService.insert(params);
				if(result>0){
					return this.setModel(true, "添加成功");
					}else{
						return this.setModel(false, "添加失败");
					}	
			}} catch (Exception e) {
				e.printStackTrace();
				return this.setModel(true, false, e.getMessage());
			}
			}
		 else {
			int result;
			try {
				result = raceraInvasiveOwnpersonToEmailsService.update(params);
				if(result>0){
					return this.setModel(true, "修改成功");
					}else{
						return this.setModel(false, "修改失败");
						}
			} catch (Exception e) {
				e.printStackTrace();
				return this.setModel(true, false, e.getMessage());
			}
		
		}
		
	}

	/**
	 * 删除医院配置信息
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("deleteOwnpersonToEmails")
	@ResponseBody
	private Object deleteOwnpersonToEmails(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {
			int result =raceraInvasiveOwnpersonToEmailsService.delete(params);
			if(result>0){
				return this.setModel(true, "删除成功");
				}else{
					return this.setModel(false, "删除失败");
					}
		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}
	}
	
	@RequestMapping("getImgWidth")
	@ResponseBody
	public Map<String, Object> getImgWidth(@RequestBody Map<String, Object> params) {
		Map<String, Object> map = new HashMap<>();
		File file = new File(ATTACHMENTPATH+params.get("photo_path").toString());
		if (file.exists()) {
			BufferedImage bi = null;
			try {
				bi = javax.imageio.ImageIO.read(file);
				map.put("height", bi.getHeight());
				map.put("width", bi.getWidth());
				System.out.println(bi.getHeight()+"------"+bi.getWidth());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	@RequestMapping("getAttachMent")
	@ResponseBody
	public List<RdsBaceraInvasivePhotoModel> getAttachMent(
			@RequestBody Map<String, Object> params) {
		return rdsInvasivePreService.getAttachMent(params);
	}
	
	/**
	 * 获取图片文件
	 */
	@RequestMapping("getImg")
	public void getImg(HttpServletResponse response, String photo_path) {
		rdsInvasivePreService.getImg(response, photo_path);
	}
	/**
	 * 图片旋转
	 * @param params
	 * @return
	 */
	@RequestMapping("turnImg")
	@ResponseBody
	public Map<String, Object> turnImg(@RequestBody Map<String, Object> params) {
		return rdsInvasivePreService.turnImg(params);
	}
	
	
	@RequestMapping("upload")
	@ResponseBody
	public void upload(@RequestParam String case_id,@RequestParam int[] filetype, @RequestParam MultipartFile[] files,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		try {
			//上传案例图片
			Boolean result = rdsInvasivePreService.upload(case_id,
					files[0], user.getUserid());
			if (result){
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"result\":true,\"msg\":\""
							+ "上传添加成功"+ "\"}");
			}else{
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().print(
						"{\"success\":false,\"result\":false,\"msg\":\""
								+ "出错了，请重新上传！" + "\"}");
			}
		} catch (IOException e) {
			e.printStackTrace();
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"result\":false,\"msg\":\""
							+ "出错了，请重新上传！" + "\"}");
		}

	}

	@RequestMapping("deleteFile")
	@ResponseBody
	public void deleteFile(@RequestBody Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		boolean result = rdsInvasivePreService.deleteFile(params);
		try {
			if (result) {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().print(
						"{\"result\":true,\"msg\":\"" + "删除成功！" + "\"}");
			} else {

				response.setContentType("text/html; charset=utf-8");
				response.getWriter()
						.print("{\"result\":false,\"msg\":\"" + "删除失败，请联系管理员！"
								+ "\"}");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取照片列表
	 * 
	 * @throws Exception
	 */
	@RequestMapping("queryAllPhoto")
	@ResponseBody
	public List<RdsBaceraInvasivePhotoModel> queryAllPhoto(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		return rdsInvasivePreService.queryAllFile(params);
	}
}
