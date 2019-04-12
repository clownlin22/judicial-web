package com.rds.statistics.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.statistics.model.RdsFinanceOAModel;
import com.rds.statistics.model.RdsFinanceOATypeModel;
import com.rds.statistics.model.RdsStatisticsTypeModel;
import com.rds.statistics.model.RdsStatisticsTypeModel2;

public interface RdsFinanceOAService {
	List<RdsFinanceOAModel> queryAllPage(Map<String, Object> params);

	int queryAllCount(Map<String, Object> params);

	void exportFinanceOAAll(Map<String, Object> params,
			HttpServletResponse response) throws Exception;

	boolean updateOAdept(Map<String, Object> params);

	List<RdsFinanceOATypeModel> queryOAtypePage(Map<String, Object> params);

	int queryOAtypeCount(Map<String, Object> params);

	boolean insertOAtype(Map<String, Object> params);

	boolean updateOAtype(Map<String, Object> params);

	boolean deleteOAtype(Map<String, Object> params);

	List<RdsStatisticsTypeModel2> queryUserDeptToModel();

	boolean updateOAInfo(Map<String, Object> params);

	List<RdsStatisticsTypeModel2> queryUserDeptToModel2(Map<String, Object> params);

	String queryBuMen(String deptid);
}
