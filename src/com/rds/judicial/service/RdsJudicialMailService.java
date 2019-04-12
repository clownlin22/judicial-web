package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import com.rds.code.utils.model.MailInfo;
import com.rds.judicial.model.RdsJudicialMailInfoModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleExpressModel;

public interface RdsJudicialMailService {

	List<MailInfo> getMailInfo(Map<String, Object> params);

	List<RdsJudicialMailInfoModel> getAllMails(Map<String, Object> params);

	Object delMailInfo(Map<String, Object> params);

	boolean saveMailInfo(Map<String, Object> params);

	boolean updateMailInfo(Map<String, Object> params);

	RdsJudicialResponse getMailCaseInfo(Map<String, Object> params);

	Map<String, Object> insertException(Map<String, Object> params);

	List<RdsJudicialSampleExpressModel> querySampleRecive(Map<String, Object> params);

}
