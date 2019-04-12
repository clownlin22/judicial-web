package com.rds.bacera.service;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 安全用药-service
 * @author Administrator
 *
 */
public interface RdsBaceraSafeMedicationService extends RdsBaceraBaseService {
	public int queryAudltMaxNum(@SuppressWarnings("rawtypes") Map map);
	public int queryChildMaxNum(@SuppressWarnings("rawtypes") Map map);
	void exportSafeMedInfo(Map<String, Object> params,HttpServletResponse response) throws Exception;
}
