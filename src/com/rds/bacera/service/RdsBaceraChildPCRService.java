package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 儿童基因检测-service
 * @author yxb
 *
 */
public interface RdsBaceraChildPCRService extends RdsBaceraBaseService {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	void exportChildPCR(Map<String, Object> params,HttpServletResponse response) throws Exception;
}
