package com.rds.statistics.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.bacera.web.controller.RdsBaceraAbstractController;
import com.rds.statistics.model.RdsExperimentalReportModel;
import com.rds.statistics.service.RdsExperimentalReportService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("statistics/report")
public class RdsExperimentalReportController extends RdsBaceraAbstractController {
	
	@Autowired
	private RdsExperimentalReportService  RdsExperimentalReportService;
	
	
	@RequestMapping("queryAllReport")
	@ResponseBody
	public Object queryAllReport(@RequestBody Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", RdsExperimentalReportService.queryAllReport(params));
		result.put("total",RdsExperimentalReportService.queryAllCount(params));
		return result;
	}
	@RequestMapping("queryAllSubject")
	@ResponseBody
	public Object queryAllSubject(@RequestBody Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", RdsExperimentalReportService.queryAllSubject(params));
		result.put("total",RdsExperimentalReportService.queryAllCount(params));
		return result;
	}
	//添加工作
	@RequestMapping("insert")
	@ResponseBody
	private Object insert(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {
			String subject=params.get("subject").toString();
			int rm=RdsExperimentalReportService.querySubject(subject);
			if(rm>0){
				return this.setModel(false, "添加失败，已存在同名工作任务");
			}
			String id = RdsUpcUUIDUtil.getUUID();
			params.put("id", id);
			int result =RdsExperimentalReportService.insert(params);
			if(result>0){
				return this.setModel(true, "添加成功");
				}else{
					return this.setModel(false, "添加失败");
					}
		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}
		
	}
	//上传报告
	@RequestMapping("deleteSubject")
	@ResponseBody
	private Object insertReport(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {
	
			RdsExperimentalReportService.deleteSubject(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}


	}
	//删除报告
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestParam String uuid,HttpServletRequest request) {
		try {
			Map<String, Object>params=new HashMap<String, Object>();
			params.put("uuid", uuid);
			String uploadName=RdsExperimentalReportService.queryReport(uuid).getUpload_username().toString();
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
		String userName=user.getUsername();
		//判断登录用户和上传人是否一致或为超级管理员
		if(userName.equals(uploadName)||userName.equals("超级管理员")){
			RdsExperimentalReportService.deleteReport(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
			}else{
				return this.setModel(true, RdsUpcConstantUtil.REPORT_DELETE_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}


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
			
			RdsExperimentalReportService.download(response, map);
			RdsExperimentalReportService.updateReport(map);//下载一次，次数+1
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
		String report=request.getParameter("subject").toString();
		String id=request.getParameter("id").toString();
		Map<String, Object> map = RdsExperimentalReportService.upload(
				user.getUsername(),report,id,files);
		
		try {
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"msg\":\""
							+ (String) map.get("message") + "\"}");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
