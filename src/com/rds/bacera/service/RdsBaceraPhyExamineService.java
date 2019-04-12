package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * service
 * @author yxb
 *
 */
public interface RdsBaceraPhyExamineService extends RdsBaceraBaseService {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	void exportPhyExamine(Map<String, Object> params,HttpServletResponse response) throws Exception;
}
