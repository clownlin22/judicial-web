package com.rds.bacera.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.bacera.service.RdsBaceraInvasivePreAttachmentService;
import com.rds.judicial.model.RdsJudicialMessageModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
@Controller
@RequestMapping("Bacera/InvasivePreAttachment")
public class RdsBaceraInvasivePreAttachmentController extends RdsBaceraAbstractController{


	@Autowired
	@Setter
	RdsBaceraInvasivePreAttachmentService rdsBaceraInvasivePreAttachmentService;

	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;

	@RequestMapping("queryAttachMent")
	@ResponseBody
	public Object queryAttachMent(@RequestBody Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", rdsBaceraInvasivePreAttachmentService.queryAttachment(params));
		result.put("total",rdsBaceraInvasivePreAttachmentService.queryAllCount(params));
		return result;
	}

	@RequestMapping("updateAttachMent")
	@ResponseBody
	public Object updateAttachMent(@RequestBody Map<String, Object> params) {
		int result = rdsBaceraInvasivePreAttachmentService.updateAttachment(params);
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
			rdsBaceraInvasivePreAttachmentService.updateAttachment(map);
			rdsBaceraInvasivePreAttachmentService.download(response, map);
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
	String id=	(String) request.getParameter("id");
	String num=(String) request.getParameter("num");
	String task_def_key=request.getParameter("task_def_key").toString();
	String attachment_path=(String) request.getParameter("attachment_path");
		Map<String, Object> map = rdsBaceraInvasivePreAttachmentService.upload(
				user.getUserid(),id,num,attachment_path,task_def_key,files);
		try {
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"msg\":\""
							+ (String) map.get("message") + "\"}");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params,HttpServletRequest request ) throws Exception {
		
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			if (null==user  ) {
				return new RdsJudicialMessageModel(false, "请重新登录", false);
			}
			
			int result = rdsBaceraInvasivePreAttachmentService.deleteAttachment(params);
			
			return (result > 0 ? 
					this.setModel(true,
					RdsUpcConstantUtil.DELETE_SUCCESS) : this.setModel(false,RdsUpcConstantUtil.DELETE_FAILED));	}    
		
	@RequestMapping("Mail")
	@ResponseBody
	public Object mail(@RequestBody Map<String, Object> params,HttpServletRequest request ) throws Exception {
		
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			if (null==user  ) {
				return new RdsJudicialMessageModel(false, "请重新登录", false);
			}
			
			int result = rdsBaceraInvasivePreAttachmentService.mailAttachment(params);
			if(result>0){
				String taskId=params.get("task_id").toString();
				Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
				if (task == null) {
					return new RdsJudicialMessageModel(false, "该流程步不存在", false);
				}
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("ispass", 1);
				if (null==task.getAssignee()  || task.getAssignee().isEmpty()) {
					taskService.claim(taskId, user.getUsername());// todo:需要改成userid
					taskService.complete(taskId, variables);
				} else {
					taskService.complete(taskId, variables);
				}
			}
			return (result > 0 ? 
					this.setModel(true,
					RdsUpcConstantUtil.SURE_SUCCESS) : this.setModel(false,RdsUpcConstantUtil.SURE_FAILED));	}    
		
	@RequestMapping("onMailOver")
	@ResponseBody
	public Object onMailOver(@RequestBody Map<String, Object> params,HttpServletRequest request ) throws Exception {
		
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			if (null==user  ) {
				return new RdsJudicialMessageModel(false, "请重新登录", false);
			}
			
			int result = rdsBaceraInvasivePreAttachmentService.onMailOver(params);
			if(result>0){
				String taskId=params.get("task_id").toString();
				Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
				if (task == null) {
					return new RdsJudicialMessageModel(false, "该流程步不存在", false);
				}
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("ispass", 1);
				if (null==task.getAssignee()  || task.getAssignee().isEmpty()) {
					taskService.claim(taskId, user.getUsername());// todo:需要改成userid
					taskService.complete(taskId, variables);
				} else {
					taskService.complete(taskId, variables);
				}
			}
			return (result > 0 ? 
					this.setModel(true,
					RdsUpcConstantUtil.SURE_SUCCESS) : this.setModel(false,RdsUpcConstantUtil.SURE_FAILED));	}    
		
	@RequestMapping("onFile")
	@ResponseBody
	public Object onFile(@RequestBody Map<String, Object> params,HttpServletRequest request ) throws Exception {
		
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			if (null==user  ) {
				return new RdsJudicialMessageModel(false, "请重新登录", false);
			}
			
			int result = rdsBaceraInvasivePreAttachmentService.onFile(params);
			if(result>0){
				String taskId=params.get("task_id").toString();
				Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
				if (task == null) {
					return new RdsJudicialMessageModel(false, "该流程步不存在", false);
				}
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("ispass", 1);
				if (null==task.getAssignee()  || task.getAssignee().isEmpty()) {
					taskService.claim(taskId, user.getUsername());// todo:需要改成userid
					taskService.complete(taskId, variables);
				} else {
					taskService.complete(taskId, variables);
				}
			}
			return (result > 0 ? 
					this.setModel(true,
					RdsUpcConstantUtil.SURE_SUCCESS) : this.setModel(false,RdsUpcConstantUtil.SURE_FAILED));	}    
		

	//分页查询数据日志
	@RequestMapping("queryExperimentLog")
	@ResponseBody
	public Object queryExperimentLog(@RequestBody Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data",
				rdsBaceraInvasivePreAttachmentService.queryExperimentLog(params));
		result.put("total",
				rdsBaceraInvasivePreAttachmentService.countExperimentLog(params));
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
			rdsBaceraInvasivePreAttachmentService.exportExperimentLog(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("exportInvasivePreAttachment")
	public void exportInvasivePreAttachment(HttpServletResponse response,
			HttpServletRequest request) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("start_time", request.getParameter("start_time"));
			params.put("end_time", request.getParameter("end_time"));
			params.put("invasive_starttime",
					request.getParameter("invasive_starttime"));
			params.put("invasive_endtime", request.getParameter("invasive_endtime"));
			params.put("upload_username",
					request.getParameter("upload_username"));
			params.put("download_username",
					request.getParameter("download_username"));
			params.put("verify_state", request.getParameter("verify_state"));
			params.put("emailflag", request.getParameter("emailflag"));
			rdsBaceraInvasivePreAttachmentService.exportPreAttachment(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
