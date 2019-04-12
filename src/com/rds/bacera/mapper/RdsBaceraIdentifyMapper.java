package com.rds.bacera.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rds.bacera.model.RdsBaceraIdentifyInfoModel;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;

@Component("RdsBaceraIdentifyMapper")
public interface RdsBaceraIdentifyMapper extends RdsBaceraBaseMapper{

	int countCaseInfo(Map<String, Object> params);

	int deleteCaseInfo(Map<String, Object> params);

	int deleteSampleInfo(Map<String, Object> params);

	List<RdsJudicialSampleInfoModel> getSampleInfo(Object case_id);

	int insertCaseInfo(RdsBaceraIdentifyInfoModel caseInfoModel);

	int insertSampleInfo(RdsJudicialSampleInfoModel model);

	List<RdsBaceraIdentifyInfoModel> queryCaseInfo(Map<String, Object> params);

	int updateCaseInfo(RdsBaceraIdentifyInfoModel caseInfoModel);
	
	int updateIsBill(Map<String, Object> params);
	
	int updateIsArchived(Map<String, Object> params);

    int updateSampleVerifyinfo(Map<String, Object> params);

	int exsitCaseCode(Map<String, Object> params);
	
	int exsitSampleCode(Map<String, Object> params);

	int exsitBlackNumber(String id_number);

}

