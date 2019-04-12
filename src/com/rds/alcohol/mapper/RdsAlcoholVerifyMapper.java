package com.rds.alcohol.mapper;

import java.util.List;
import java.util.Map;

import com.rds.alcohol.model.RdsAlcoholCaseInfoModel;
import com.rds.alcohol.model.RdsAlcoholVerifyInfoModel;
import com.rds.judicial.mapper.RdsJudicialBaseMapper;

public interface RdsAlcoholVerifyMapper extends RdsJudicialBaseMapper{
	List<RdsAlcoholCaseInfoModel> getVerifyCaseInfo(Map<String, Object> params);

	int countVerifyCaseInfo(Map<String, Object> params);

	int verifyCaseInfo(Map<String, Object> params);

	int insertVerifyInfo(Map<String, Object> params);

	List<RdsAlcoholVerifyInfoModel> getVerifyInfo(Map<String, Object> params);
}
