package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 健康美容基因
 * @author yxb
 *
 */
public interface RdsBaceraSlimBeautyPCRService extends RdsBaceraBaseService {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	void exportSlimBeautyPCR(Map<String, Object> params,HttpServletResponse response) throws Exception;
}
