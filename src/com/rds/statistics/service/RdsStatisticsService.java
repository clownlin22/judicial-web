package com.rds.statistics.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.statistics.model.RdsStatisticsResponse;

public interface RdsStatisticsService {

	RdsStatisticsResponse getTotalStatistics(String date);

	RdsStatisticsResponse getPerStatistics(String date);

	RdsJudicialResponse statTime(Map<String, Object> params);

	void exportTotalStatistics(String date, HttpServletResponse response);

	void exportPerStatistics(String date, HttpServletResponse response);

	void exportPerBanlance(String date, HttpServletResponse response);
	
	RdsJudicialResponse queryProgramProvice(Map<String, Object> params);

}
