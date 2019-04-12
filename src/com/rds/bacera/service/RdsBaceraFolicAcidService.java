package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 百会叶酸代谢能力检测-service
 * @author yxb
 *
 */
public interface RdsBaceraFolicAcidService extends RdsBaceraBaseService {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	void exportFolicAcid(Map<String, Object> params,HttpServletResponse response) throws Exception;

}
