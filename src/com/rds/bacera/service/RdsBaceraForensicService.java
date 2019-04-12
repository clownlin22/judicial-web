package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 法医病理-service
 * @author yxb
 *
 */
public interface RdsBaceraForensicService extends RdsBaceraBaseService {
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map);
	void exportForensicInfo(Map<String, Object> params,HttpServletResponse response) throws Exception;
}
