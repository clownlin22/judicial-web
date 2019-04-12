package com.rds.alcohol.service;

import java.util.List;
import java.util.Map;

import com.rds.alcohol.model.RdsAlcoholResponse;

public interface RdsAlcoholExperimentService {
	public RdsAlcoholResponse queryCaseInfo(Map<String, Object> params);

	public Map<String, Object> addRegression(Map<String, Object> params);

	public Map<String, Object> addExperiment(Map<String, Object> params);

	public Map<String, Object> isRegPastDue(Map<String, Object> params);

	public List<Map<String, Object>> queryExperDetail(Map<String, Object> params);

	public Map<String, Object> deleteExper(Map<String, Object> params);
}
