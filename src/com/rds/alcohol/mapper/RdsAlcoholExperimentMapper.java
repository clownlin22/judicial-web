package com.rds.alcohol.mapper;

import java.util.List;
import java.util.Map;

import com.rds.alcohol.model.RdsAlcoholExperimentDataModel;
import com.rds.alcohol.model.RdsAlcoholExperimentModel;
import com.rds.alcohol.model.RdsAlcoholRegressionModel;
import com.rds.alcohol.model.RdsAlcoholRegressionPointModel;

public interface RdsAlcoholExperimentMapper {
	public int setRegressionData(List<RdsAlcoholRegressionPointModel> list);

	public int setRegression(RdsAlcoholRegressionModel regModel);

	public RdsAlcoholRegressionModel getRegModel(Map<String, Object> params);

	public int getExperCount(Map<String, Object> params);

	public int addExperiment(RdsAlcoholExperimentModel experModel);

	public int addExperimentData(List<RdsAlcoholExperimentDataModel> list);

	public int updateCaseState(Map<String, Object> params);

	List<Map<String, Object>> queryCaseForExper(Map<String, Object> params);

	int queryCaseForExperCount(Map<String, Object> params);

	public List<Map<String, Object>> getExperDetail(Map<String, Object> params);

	public int deleteExper(Map<String, Object> params);
}
