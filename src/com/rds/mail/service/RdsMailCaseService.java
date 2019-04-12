package com.rds.mail.service;

import java.util.List;
import java.util.Map;

import com.rds.mail.model.RdsMailCaseInfo;
import com.rds.mail.model.RdsMailInfo;

public interface RdsMailCaseService {

	Map<String, Object> getCaseInfo(Map<String, Object> params);

	RdsMailCaseInfo getMailCase(Map<String, Object> params);

	boolean saveMailInfo(Map<String, Object> params);

	boolean exsitMailCode(Map<String, Object> params);

	RdsMailInfo getMailInfo(Map<String, Object> params);

	boolean updateMailInfo(Map<String, Object> params);

	boolean deleteMailInfo(Map<String, Object> params);

	List<RdsMailInfo> checkMails(Map<String, Object> params);

}
