package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialCaseExceptionInfo;
import com.rds.judicial.model.RdsJudicialCaseExceptionModel;
import com.rds.judicial.model.RdsJudicialDicValuesModel;
import com.rds.judicial.model.RdsJudicialResponse;

public interface RdsJudicialCaseExceptionService {

	RdsJudicialResponse getCaseException(Map<String, Object> params);

	List<RdsJudicialCaseExceptionModel> getOtherException(Map<String, Object> params);

	boolean saveExceptionInfo(Map<String, Object> params);

	List<RdsJudicialDicValuesModel> getExceptionTypes();

	boolean deleteExceptionInfo(Map<String, Object> params);

	boolean handleExceptionInfo(Map<String, Object> params);

	boolean updateExceptionInfo(Map<String, Object> params);
	
	RdsJudicialCaseExceptionInfo getCaseInfo(Map<String, Object> params);
	
	RdsJudicialResponse getExportException(Map<String,Object> params);
	
	public void exportCaseException(Map<String, Object> params,HttpServletResponse response);
}
