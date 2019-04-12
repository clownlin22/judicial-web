package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * service
 * @author yxb
 *
 */
public interface RdsBaceraCTDnaService extends RdsBaceraBaseService {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	void exportCTDna(Map<String, Object> params,HttpServletResponse response) throws Exception;
}
