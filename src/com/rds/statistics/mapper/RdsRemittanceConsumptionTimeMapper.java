package com.rds.statistics.mapper;

import java.util.List;
import java.util.Map;

import com.rds.statistics.model.RdsRemittanceConsumptionTimeModel;

public interface RdsRemittanceConsumptionTimeMapper {
	List<RdsRemittanceConsumptionTimeModel>queryConsumptionTimeAll(Map<String, Object> params);
	int queryConsumptionTimeAllCount(Map<String, Object> params);
	public List<Object> queryConsumptionTime(Object params) throws Exception;
}
