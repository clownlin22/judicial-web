package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialCaseExceptionInfo;
import com.rds.judicial.model.RdsJudicialCaseExceptionInfoModel;
import com.rds.judicial.model.RdsJudicialCaseExceptionModel;
import com.rds.judicial.model.RdsJudicialCaseExportExceptionModel;
import com.rds.judicial.model.RdsJudicialDicValuesModel;

public interface RdsJudicialCaseExceptionMapper {

	List<RdsJudicialCaseExceptionInfoModel> getCaseException(
			Map<String, Object> params);

	int countCaseException(Map<String, Object> params);

	List<RdsJudicialCaseExceptionModel> getOtherException(Map<String, Object> params);

	boolean saveExceptionInfo(Map<String, Object> params);

	List<RdsJudicialDicValuesModel> getExceptionTypes();

	boolean deleteExceptionInfo(Map<String, Object> params);

	boolean handleExceptionInfo(Map<String, Object> params);

	boolean updateExceptionInfo(Map<String, Object> params);

	List<RdsJudicialCaseExceptionInfo> getCaseInfo(Map<String,Object> params);
	
	List<RdsJudicialCaseExportExceptionModel> getExportException(Map<String,Object> params);
	
	List<RdsJudicialCaseExportExceptionModel> getExportExceptionByPage(Map<String,Object> params);
	
	int countExportException(Map<String, Object> params);
}
