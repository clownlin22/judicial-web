package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialAllCaseInfoModel;
import com.rds.judicial.model.RdsJudicialCaseConfirmTime;
import com.rds.judicial.model.RdsJudicialExperimentModel;
import com.rds.judicial.model.RdsJudicialExportCaseInfoModel;
import com.rds.judicial.model.RdsJudicialParamsModel;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;

public interface RdsJudicialAllCaseInfoMapper {

	List<RdsJudicialAllCaseInfoModel> getAllCaseInfo(Map<String, Object> params);

	int getAllCaseInfoCount(Map<String, Object> params);

	List<RdsJudicialExportCaseInfoModel> queryAllCaseInfo(
			RdsJudicialParamsModel params);

	List<RdsJudicialSampleInfoModel> queryParentSampleInfoList(
			List<RdsJudicialExportCaseInfoModel	> caseInfoModels);

	List<RdsJudicialSampleInfoModel> queryChildSampleInfoList(
			List<RdsJudicialExportCaseInfoModel> caseInfoModels);
	
	List<RdsJudicialExperimentModel> queryPlaceBySamplecode(Map<String, Object> params);
	
	List<RdsJudicialExportCaseInfoModel> queryExportCaseInfo(Map<String, Object> params);
	
	int queryCountExportCaseInfo(Map<String, Object> params);
	
	List<RdsJudicialExportCaseInfoModel> queryPartnerAllCaseInfo(Map<String, Object> params);
	
	List<RdsJudicialSampleInfoModel> getExceptionSampleInfo(Map<String, Object> params);
	
	List<RdsJudicialExportCaseInfoModel> queryMessageCase(Map<String, Object> params);
	
	List<Map<String,String>> queryFMChild(Map<String, Object> params);
	
	int queryFMChildCount(Map<String, Object> params);
	
	List<Map<String,String>> queryExperimentInfo(Map<String, Object> params);
	
	int queryExperimentCount(Map<String, Object> params);
	
	List<RdsJudicialCaseConfirmTime> queryCaseConfirmTime(Map<String, Object> params);
	
	int queryCaseConfirmTimeCount(Map<String, Object> params);
}
