package com.rds.alcohol.mapper;

import com.rds.alcohol.model.*;
import com.rds.judicial.model.RdsJudicialCaseFeeModel;

import java.util.List;
import java.util.Map;


public interface RdsAlcoholRegisterMapper {

	List<RdsAlcoholCaseInfoModel> getCaseInfo(Map<String, Object> params);

	int countCaseInfo(Map<String, Object> params);

	int deleteCaseInfo(Map<String, Object> params);

	int exsitCaseCode(String case_code);

	int addCaseInfo(RdsAlcoholCaseInfoModel caseInfoModel);

	int updateCaseCode(RdsAlcoholCaseInfoModel caseInfoModel);

	List<RdsAlcoholCaseInfoModel> getAllCaseInfo(
			RdsAlcoholQueryParam param);

	int addSampleInfo(RdsAlcoholSampleInfoModel sampleInfomodel);

	RdsAlcoholSampleInfoModel getSampleInfo(Map<String, Object> params);

	int updateSampleCode(RdsAlcoholSampleInfoModel sampleInfoModel);

	int addCaseFee(RdsJudicialCaseFeeModel casefee);
	
	List<RdsAlcoholDicValueModel> getClient();
	
	List<RdsAlcoholCaseExportModel> exportCaseInfo(Map<String, Object> params);

	List<RdsAlcoholDicValueModel> getClient2();

	void updateCasefee(RdsAlcoholCaseInfoModel caseInfoModel);

	int exsitcase_code(String case_code);

    List<RdsAlcoholDicValueModel> getIdentificationPer();
}
