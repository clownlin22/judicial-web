package com.rds.bacera.service;

/**
 * 肿瘤易感基因-service
 */
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface RdsBaceraTumorSusService extends RdsBaceraBaseService {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	void exportTumorSus(Map<String, Object> params,HttpServletResponse response) throws Exception;
}
