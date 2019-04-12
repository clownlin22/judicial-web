package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 基因检测
 * @author wind
 *
 */
public interface BaceraGeneTestingProjectService extends RdsBaceraBaseService  {
	
void exportGeneInfo(Map<String, Object> params,
			HttpServletResponse response) throws Exception;
}
