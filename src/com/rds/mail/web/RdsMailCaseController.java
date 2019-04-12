package com.rds.mail.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.mail.model.RdsMailCaseInfo;
import com.rds.mail.model.RdsMailInfo;
import com.rds.mail.service.RdsMailCaseService;
import com.rds.upc.model.RdsUpcUserModel;


@Controller
@RequestMapping("mail")
public class RdsMailCaseController {

	@Autowired
	private RdsMailCaseService RdsMailCaseService;
	
	@ResponseBody
	@RequestMapping("getCaseInfo")
	public Map<String, Object> getCaseInfo(@RequestBody Map<String, Object> params){
		return RdsMailCaseService.getCaseInfo(params);
	}  
	
	@ResponseBody
	@RequestMapping("getMailCase")
	public RdsMailCaseInfo getMailCase(@RequestBody Map<String, Object> params){
		return RdsMailCaseService.getMailCase(params);
	}
	
	@ResponseBody
	@RequestMapping("saveMailInfo")
	public boolean saveMailInfo(@RequestBody Map<String, Object> params,HttpSession session){
		String mail_per = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			mail_per = user.getUserid();
        }
		params.put("mail_per", mail_per);
		return RdsMailCaseService.saveMailInfo(params);
	}
	
	@ResponseBody
	@RequestMapping("exsitMailCode")
	public boolean exsitMailCode(@RequestBody Map<String, Object> params){
		return RdsMailCaseService.exsitMailCode(params);
	}
	
	@ResponseBody
	@RequestMapping("getMailInfo")
	public RdsMailInfo getMailInfo(@RequestBody Map<String, Object> params){
		return RdsMailCaseService.getMailInfo(params);
	}
	
	@ResponseBody
	@RequestMapping("updateMailInfo")
	public boolean updateMailInfo(@RequestBody Map<String, Object> params,HttpSession session){
		String mail_per = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			mail_per = user.getUserid();
        }
		params.put("mail_per", mail_per);
		return RdsMailCaseService.updateMailInfo(params);
	}
	
	@ResponseBody
	@RequestMapping("deleteMailInfo")
	public boolean deleteMailInfo(@RequestBody Map<String, Object> params,HttpSession session){
		return RdsMailCaseService.deleteMailInfo(params);
	}
	
	@ResponseBody
	@RequestMapping("checkMails")
	public List<RdsMailInfo> checkMails(@RequestBody Map<String, Object> params){
		return RdsMailCaseService.checkMails(params);
	}
}
