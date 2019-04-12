package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface RdsBaceraTumorMarkerService extends RdsBaceraBaseService{
	void exportTumor(Map<String, Object> params,HttpServletResponse response) throws Exception;
	public boolean exsitCaseCode(Map<String, Object> params);

}
