package com.rds.alcohol.service;

import java.util.List;
import java.util.Map;

import com.rds.alcohol.model.RdsAlcoholArchiveModel;
import com.rds.alcohol.model.RdsAlcoholArchiveReadModel;
import com.rds.alcohol.model.RdsAlcoholResponse;

public interface RdsAlcoholArchiveService {

	RdsAlcoholResponse getCaseInfo(Map<String, Object> params);

	boolean addArchiveInfo(Map<String, Object> params);

	RdsAlcoholResponse getArchiveInfo(Map<String, Object> params);

	List<RdsAlcoholArchiveReadModel> getReadInfo(Map<String, Object> params);

	boolean addReadInfo(Map<String, Object> params);

}
