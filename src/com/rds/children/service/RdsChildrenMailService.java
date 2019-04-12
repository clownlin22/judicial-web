package com.rds.children.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.children.model.RdsChildrenResponse;

public interface RdsChildrenMailService {

	RdsChildrenResponse getMailCaseInfo(Map<String, Object> params);

	void exportChildrenMail(String endtime, String starttime, String child_sex,
			String mail_starttime, String mail_endtime, String child_name,
			String case_code, String sample_code, String case_userid,
			String case_areaname, Integer is_mail, Integer is_paid,
			HttpServletResponse response);
}
