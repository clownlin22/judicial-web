package com.rds.children.web;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.service.RdsChildrenMailService;

@RequestMapping("children/mail")
@Controller
public class RdsChildrenMailController {

	@Autowired
	private RdsChildrenMailService rdsChildrenMailService;

	@RequestMapping("getMailCaseInfo")
	@ResponseBody
	private RdsChildrenResponse getCaseInfo(
			@RequestBody Map<String, Object> params) {
		return rdsChildrenMailService.getMailCaseInfo(params);
	}

	@RequestMapping("exportChildrenMail")
	public void exportChildrenMail(@RequestParam String endtime,
			@RequestParam String starttime, @RequestParam String child_sex,
			@RequestParam String mail_starttime, @RequestParam String mail_endtime,
			@RequestParam String child_name, @RequestParam String case_code,
			@RequestParam String sample_code, @RequestParam String case_userid,
			@RequestParam String case_areaname, @RequestParam Integer is_mail,@RequestParam Integer is_paid,
			HttpServletResponse response) {
		rdsChildrenMailService.exportChildrenMail(endtime, starttime, child_sex,mail_starttime,mail_endtime,
				child_name, case_code,sample_code,case_userid,case_areaname,is_mail,is_paid, response);
	}
}
