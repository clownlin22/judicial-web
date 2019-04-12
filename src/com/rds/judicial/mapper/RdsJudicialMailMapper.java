package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialMailInfoModel;
import com.rds.judicial.model.RdsJudicialSampleExpressModel;


public interface RdsJudicialMailMapper extends RdsJudicialBaseMapper{

	int updateMailInfo(Map<String, Object> params);

	int countMailCaseInfo(Map<String, Object> params);

	List<RdsJudicialCaseInfoModel> queryMailCaseInfo(Map<String, Object> params);

	int saveMailInfo(Map<String, Object> params);

	int delMailInfo(Map<String, Object> params);

	List<RdsJudicialMailInfoModel> getAllMails(Map<String, Object> params);

	boolean insertException(Map<String, Object> params);

	int getException(Map<String, Object> params);

	List<RdsJudicialCaseInfoModel> queryCanMailCaseInfo(
			Map<String, Object> params);
	
	List<RdsJudicialSampleExpressModel> querySampleRecive(Map<String, Object> params);

}

