package com.rds.alcohol.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.alcohol.model.RdsAlcoholAttachmentModel;
import com.rds.alcohol.service.RdsAlcoholAttachmentService;
import com.rds.children.model.RdsChildrenCasePhotoModel;
import com.rds.upc.model.RdsUpcUserModel;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("alcohol/attachment")
public class RdsAlcoholAttachmentController
{

	@Autowired
	private RdsAlcoholAttachmentService aService;

	@RequestMapping("getAttachment")
	@ResponseBody
	public Map<String, Object> getAttachment(@RequestBody Map<String, Object> params)
	{
		return this.aService.getAttachment(params);
	}

	@RequestMapping("getAtt")
	@ResponseBody
	public List<RdsChildrenCasePhotoModel> queryCasePhoto(@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception
	{
		return this.aService.getAtt(params);
	}

	@RequestMapping("upload")
	@ResponseBody
	public void upload(@RequestParam String case_code, @RequestParam MultipartFile[] files, HttpServletResponse response) throws Exception
	{
		Map map = this.aService.upload(case_code, files);
		try {
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(
					"{\"success\":true,\"msg\":\"" + 
							(String)map.get("message") + "\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("uploadt")
	@ResponseBody
	public void upload2(RdsAlcoholAttachmentModel pmodel, HttpServletRequest request, @RequestParam MultipartFile[] headPhoto, HttpServletResponse response) throws Exception
	{
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel)request.getSession().getAttribute("user");
		}
		Map pmap = this.aService.photoUpload(
				pmodel, headPhoto);
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(JSONObject.fromObject(pmap).toString());
		response.flushBuffer();
	}

	@RequestMapping("uploadtt")
	@ResponseBody
	public void upload3(RdsAlcoholAttachmentModel pmodel, HttpServletRequest request, @RequestParam MultipartFile[] headPhoto, HttpServletResponse response) throws Exception
	{
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel)request.getSession().getAttribute("user");
		}
		Map pmap = this.aService.photoUploadt(
				pmodel, headPhoto);
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(JSONObject.fromObject(pmap).toString());
		response.flushBuffer();
	}
	
	@RequestMapping("uploadtd")
	@ResponseBody
	public void upload4(RdsAlcoholAttachmentModel pmodel, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel)request.getSession().getAttribute("user");
		}
		Map pmap = this.aService.photoUploadtd(
				pmodel);
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(JSONObject.fromObject(pmap).toString());
		response.flushBuffer();
	}

	@RequestMapping("getPrintAtt")
	public String getPrintAtt(@RequestParam String att_id, HttpServletRequest request) {
		request.setAttribute("att", this.aService.getPrintAttachment(att_id));
		return "alcohol/print_att";
	}

	@RequestMapping("getPrintAttXBS")
	public String getPrintAttXBS(@RequestParam String att_id, HttpServletRequest request)
	{
		Map map = this.aService.getPrintAttachment(att_id);
		request.setAttribute("att", map);
		request.setAttribute("case_code", map.get("case_code"));
		return "alcohol/print_attXBS";
	}

	@RequestMapping("deletAttInfo")
	@ResponseBody
	public boolean deletAttInfo(@RequestBody Map<String, Object> params)
	{
		return this.aService.deletAttInfo(params);
	}
}