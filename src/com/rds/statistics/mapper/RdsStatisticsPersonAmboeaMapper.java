package com.rds.statistics.mapper;

import java.util.List;
import java.util.Map;

import com.rds.statistics.model.RdsAmboeaPersonModel;
import com.rds.statistics.model.RdsFinanceCaseDetailStatisticsModel;

/**
 * @author yxb
 * @className
 * @description
 * @date 2018/5/14
 */
public interface RdsStatisticsPersonAmboeaMapper {
	List<Object> queryAllPage(Map<String, Object> params);

	int queryAllCount(Map<String, Object> params);

	List<RdsFinanceCaseDetailStatisticsModel> queryCaseAll(
			Map<String, Object> params);

	int queryCaseAllCount(Map<String, Object> params);

	List<RdsAmboeaPersonModel> queryAmboeaPerson(Map<String, Object> params);

	int queryAmboeaPersonCount(Map<String, Object> params);
}
