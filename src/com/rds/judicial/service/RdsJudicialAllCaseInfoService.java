package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialExperimentModel;
import com.rds.judicial.model.RdsJudicialParamsModel;
import com.rds.judicial.model.RdsJudicialResponse;

public interface RdsJudicialAllCaseInfoService {

	Map<String, Object> getAllCaseInfo(Map<String, Object> params);

	void exportSampleInfo(RdsJudicialParamsModel params,
			HttpServletResponse response);
	
	void exportCaseInfoNoFinance(RdsJudicialParamsModel params,
			HttpServletResponse response);

	List<RdsJudicialExperimentModel> queryPlaceBySamplecode(Map<String, Object> params);
	
	void exportCaseInfo(Map<String, Object> params,
			HttpServletResponse response);
	
	Map<String, Object> queryExportCaseInfo(Map<String, Object> params);
	
	void exportPartnerAllCaseInfo(Map<String, Object> params,
			HttpServletResponse response);
	
	RdsJudicialResponse getSampleInfo(Map<String, Object> params);
	
	void exportMessageCaseInfo(Map<String, Object> params,
			HttpServletResponse response);
	
	Map<String, Object> queryFMChild(Map<String, Object> params);
	
	int queryFMChildCount(Map<String, Object> params);
	
	void exportFMChild(Map<String, Object> params,HttpServletResponse response);
	
	Map<String, Object> queryExperimentInfo(Map<String, Object> params);
	
	int queryExperimentCount(Map<String, Object> params);
	
	void exportExperimentInfo(Map<String, Object> params,HttpServletResponse response);
	
	public Map<String, Object> queryConfirmTimePage(Map<String, Object> params);

	void exportConfirmTimeInfo(Map<String, Object> params,HttpServletResponse response);
	
}
