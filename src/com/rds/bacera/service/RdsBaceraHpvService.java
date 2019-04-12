package com.rds.bacera.service;

/**
 * Hpv-service
 */
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface RdsBaceraHpvService extends RdsBaceraBaseService {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	void exportHpv(Map<String, Object> params,HttpServletResponse response) throws Exception;
}
