package com.rds.statistics.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.statistics.model.RdsStatisticsBaseModel;
import com.rds.statistics.model.RdsStatisticsProgramModel;
import com.rds.statistics.model.RdsStatsticsJudicialTimeModel;

public interface RdsStatisticsMapper {


	List<RdsStatisticsBaseModel> getTotalModelByMonth(String date);

	List<RdsStatisticsBaseModel> getBaseModel(String date);

	List<String> getOwnPerson(String date);

	List<RdsStatisticsBaseModel> getPerBaseModel(String date);

	List<RdsStatsticsJudicialTimeModel> getTimeModel(Map<String, Object> params);

	int countTimeModel(Map<String, Object> params);
	
	List<RdsStatisticsProgramModel> queryProgramProvice(Map<String, Object> params);
	
	int queryProgramProviceCount(Map<String, Object> params);
	
	List<RdsJudicialKeyValueModel> getUsers(Map<String, Object> params);

}
