package com.rds.statistics.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.statistics.model.RdsAmboeaPersonModel;
import com.rds.statistics.model.RdsFinanceCaseDetailStatisticsModel;

/**
 * @author yxb
 * @className
 * @description
 * @date 2018/5/14
 */
public interface RdsStatisticsPersonAmboeaService {
	List<Object> queryAllPage(Map<String, Object> params);

	int queryAllCount(Map<String, Object> params);

	void exportPersonAmboeaInfo(Map<String, Object> params,
			HttpServletResponse response) throws Exception;

	List<RdsFinanceCaseDetailStatisticsModel> queryCaseAll(
			Map<String, Object> params);

	int queryCaseAllCount(Map<String, Object> params);

	void exportCaseAll(Map<String, Object> params, HttpServletResponse response)
			throws Exception;

	List<RdsAmboeaPersonModel> queryAmboeaPerson(Map<String, Object> params);

	int queryAmboeaPersonCount(Map<String, Object> params);
	
	void exportAmboeaPerson(Map<String, Object> params, HttpServletResponse response)
			throws Exception;
}
