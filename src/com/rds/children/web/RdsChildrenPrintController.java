package com.rds.children.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.children.mapper.RdsChildrenRegisterMapper;
import com.rds.children.model.RdsChildrenPrintCaseModel;
import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.service.RdsChildrenPrintService;
import com.rds.upc.model.RdsUpcUserModel;

@RequestMapping("children/print")
@Controller
public class RdsChildrenPrintController {

	@Autowired
	private RdsChildrenPrintService RdsChildrenPrintService;
	
	@Autowired
	private RdsChildrenRegisterMapper rdsChildrenRegisterMapper;

	@RequestMapping("getCaseInfo")
	@ResponseBody
	private RdsChildrenResponse getCaseInfo(
			@RequestBody Map<String, Object> params) {
		return RdsChildrenPrintService.getCaseInfo(params);
	}

	@RequestMapping("printCaseInfo")
	private String printCaseInfo(String case_id, HttpServletRequest request) {
		RdsChildrenPrintCaseModel caseinfo = RdsChildrenPrintService
				.printCaseInfo(case_id);
		String photo = RdsChildrenPrintService.getCasePhoto(case_id);
		List<Map<String, String>> results = RdsChildrenPrintService
				.printCaseResult(case_id);
		request.setAttribute("photo", photo);
		request.setAttribute("case_info", caseinfo);
		request.setAttribute("results", results);
		return "children/caseinfo";
	}

	@RequestMapping("printPersonInfo")
	private String printPersonInfo(String case_id, HttpServletRequest request) {
		RdsChildrenPrintCaseModel caseinfo = RdsChildrenPrintService
				.printCaseInfo(case_id);
		request.setAttribute("case_info", caseinfo);
		return "children/personinfo";
	}

	@RequestMapping("printTableInfo")
	private String printTableInfo(String case_id, HttpServletRequest request) {
		RdsChildrenPrintCaseModel caseinfo = RdsChildrenPrintService
				.printCaseInfo(case_id);
		List<Map<String, String>> results = RdsChildrenPrintService
				.printCaseResult(case_id);
		request.setAttribute("case_result", results);
		request.setAttribute("case_info", caseinfo);
		return "children/tableinfo";
	}

	@RequestMapping("getImg")
	public void getImg(HttpServletResponse response, String filename) {
		RdsChildrenPrintService.getImg(response, filename);
	}

	@RequestMapping("changePrintState")
	@ResponseBody
	public boolean changePrintState(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		String userid = "";
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			userid = user.getUserid();
		}
		if (StringUtils.isEmpty(userid)) {
			return false;
		}
		params.put("user", user);
		return RdsChildrenPrintService.changePrintState(params);
	}

	@RequestMapping("childrenCardCreate")
	@ResponseBody
	private Object childrenCardCreate(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		String userid = "";
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			userid = user.getUserid();
		}
		if (StringUtils.isEmpty(userid)) {
			return false;
		}
		params.put("user", user);
		
		Map<String, Object> map = new HashMap<String, Object>();
		params.put("photo_type", 2);
		if(rdsChildrenRegisterMapper.queryCasePhotoCount(params)<=0){
			map.put("success", false);
			map.put("msg", "该案例儿童照片未处理!");
			return map;
		}
		
		map = RdsChildrenPrintService.childrenCardCreate(params);
//		String case_id = params.get("case_id").toString();
//		RdsChildrenPrintCaseModel caseinfo = RdsChildrenPrintService
//				.printCaseInfo(case_id);
//		String photo = RdsChildrenPrintService.getCasePhoto(case_id);
//		List<Map<String, String>> results = RdsChildrenPrintService
//				.printCaseResult(case_id);
//		request.setAttribute("photo", photo);
//		request.setAttribute("case_info", caseinfo);
//		request.setAttribute("results", results);
		return map;
	}

}
