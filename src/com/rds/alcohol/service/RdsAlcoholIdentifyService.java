package com.rds.alcohol.service;

import java.util.Map;

import com.rds.alcohol.model.RdsAlcoholIdentifyCaseinfo;
import com.rds.alcohol.model.RdsAlcoholResponse;

public interface RdsAlcoholIdentifyService {

	RdsAlcoholResponse getIdentifyInfo(Map<String, Object> params);

	Integer insert(Map<String, Object> params);

	int delete(Map<String, Object> params);

	int update(Map<String, Object> params);


	boolean exsitper_code(Object object);

	boolean exsitper_name(Object per_name);

	String selectper_id(String case_checkper);

	boolean add(RdsAlcoholIdentifyCaseinfo ps);


	void deleteCaseIdetity(String case_id);




}
