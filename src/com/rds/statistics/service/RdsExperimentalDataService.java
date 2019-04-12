package com.rds.statistics.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface RdsExperimentalDataService {

	void exportExperimentalData(Map<String, Object> params,HttpServletResponse response) throws Exception;

	Object queryAllPage(Object params) throws Exception;

	


}
