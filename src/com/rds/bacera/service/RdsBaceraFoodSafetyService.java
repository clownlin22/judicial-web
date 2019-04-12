package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 食品安全-service
 * @author Administrator
 *
 */
public interface RdsBaceraFoodSafetyService extends RdsBaceraBaseService {
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map);
	void exportFoodSafe(Map<String, Object> params,HttpServletResponse response) throws Exception;

}
