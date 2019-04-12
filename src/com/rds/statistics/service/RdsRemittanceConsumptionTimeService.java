package com.rds.statistics.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface RdsRemittanceConsumptionTimeService {

Map<String, Object> queryConsumptionTimeAll(Map<String, Object> params);
void export(Map<String, Object> params,
		HttpServletResponse response) throws Exception;
}
