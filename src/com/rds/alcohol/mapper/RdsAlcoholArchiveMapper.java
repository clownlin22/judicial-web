package com.rds.alcohol.mapper;

import java.util.List;
import java.util.Map;

import com.rds.alcohol.model.RdsAlcoholArchiveModel;
import com.rds.alcohol.model.RdsAlcoholArchiveReadModel;
import com.rds.alcohol.model.RdsAlcoholCaseInfoModel;

public interface RdsAlcoholArchiveMapper {

	List<RdsAlcoholCaseInfoModel> getCaseInfo(Map<String, Object> params);

	int countCaseInfo(Map<String, Object> params);

	int addArchiveInfo(Map<String, Object> params);

	int updateCaseState(Map<String, Object> params);

	List<RdsAlcoholArchiveModel> getArchiveInfo(Map<String, Object> params);

	int countArchiveInfo(Map<String, Object> params);

	int countReadInfo(Map<String, Object> params);

	List<RdsAlcoholArchiveReadModel> getReadInfo(Map<String, Object> params);

	int addReadInfo(Map<String, Object> params);

}
