package com.rds.alcohol.service;

import java.util.List;
import java.util.Map;

import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.model.RdsAlcoholVerifyInfoModel;

public interface RdsAlcoholVerifyService {

	RdsAlcoholResponse getVerifyCaseInfo(Map<String, Object> params);

	boolean verifyCaseInfo(Map<String, Object> params);

	List<RdsAlcoholVerifyInfoModel> getVerifyInfo(Map<String, Object> params);

}
