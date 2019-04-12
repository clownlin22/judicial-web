package com.rds.alcohol.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rds.alcohol.mapper.RdsAlcoholVerifyMapper;
import com.rds.alcohol.model.RdsAlcoholCaseInfoModel;
import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.model.RdsAlcoholVerifyInfoModel;
import com.rds.alcohol.service.RdsAlcoholVerifyService;

@Service
@Transactional
public class RdsAlcoholVerifyServiceImple implements RdsAlcoholVerifyService {

	@Autowired
	private RdsAlcoholVerifyMapper rdsAlcoholVerifyMapper;

	@Override
	public RdsAlcoholResponse getVerifyCaseInfo(Map<String, Object> params) {
		RdsAlcoholResponse response = new RdsAlcoholResponse();
		List<RdsAlcoholCaseInfoModel> caseInfoModels = rdsAlcoholVerifyMapper
				.getVerifyCaseInfo(params);
		int count = rdsAlcoholVerifyMapper.countVerifyCaseInfo(params);
		response.setCount(count);
		response.setItems(caseInfoModels);
		return response;
	}

	@Override
	public boolean verifyCaseInfo(Map<String, Object> params) {
		int count = rdsAlcoholVerifyMapper.verifyCaseInfo(params);
		if (count > 0) {
			if ((Integer) params.get("state") == 3) {
				params.put("state", 2);
			}
			rdsAlcoholVerifyMapper.insertVerifyInfo(params);
			return true;
		}
		return false;
	}

	@Override
	public List<RdsAlcoholVerifyInfoModel> getVerifyInfo(
			Map<String, Object> params) {
		return rdsAlcoholVerifyMapper.getVerifyInfo(params);
	}

}
