package com.rds.mail.mapper;

import java.util.List;
import java.util.Map;

import com.rds.mail.model.RdsMailCaseInfo;
import com.rds.mail.model.RdsMailInfo;

public interface RdsMailCaseMapper {

	public List<RdsMailCaseInfo> getCaseInfos(Map<String, Object> params);

	public int countCaseInfos(Map<String, Object> params);

	public RdsMailCaseInfo getMailCase(Map<String, Object> params);

	public boolean insertMailInfo(Map<String, Object> params);

	public boolean insertCaseMailLink(Map<String, Object> params);

	public boolean deleteMailLinkByMap(Map<String, Object> params);

	public int exsitMailCode(Map<String, Object> params);

	public RdsMailInfo getMailInfo(Map<String, Object> params);

	public List<RdsMailCaseInfo> getMailCaseInfo(Map<String, Object> params);

	public boolean updateMailInfo(Map<String, Object> params);

	public boolean deleteMailLinkByModel(RdsMailInfo mailInfo);

	public boolean deleteMailInfo(RdsMailInfo mailInfo);

	public List<RdsMailInfo> checkMails(Map<String, Object> params);
}
