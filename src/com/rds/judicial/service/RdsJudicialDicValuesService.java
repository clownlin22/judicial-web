package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialAreaInfo;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.upc.model.RdsUpcPermitNodeModel;


public interface RdsJudicialDicValuesService  extends RdsJudicialBaseService{

	List<RdsJudicialKeyValueModel> getReportModels(Map<String, Object> params);

	List<RdsUpcPermitNodeModel> getReportType(String code);

	List<RdsJudicialAreaInfo> getAreaInfo(Map<String, Object> params);

	List<RdsJudicialKeyValueModel> getSampleType();

	List<RdsJudicialKeyValueModel> getSampleCall();

	List<RdsJudicialAreaInfo> getUpcUsers(Map<String, Object> params);

	List<RdsJudicialKeyValueModel> getAllUsers();

	List<RdsJudicialKeyValueModel> getUsersId(Map<String, Object> params);
	
	List<RdsJudicialKeyValueModel> getMailModels();

	List<Map<String, Object>> getFeeType();

	List<RdsJudicialKeyValueModel> getManager(Map<String, Object> params);

	List<RdsJudicialKeyValueModel> getCustodyCall();

	List<RdsJudicialKeyValueModel> getUnitTypes();

	List<RdsJudicialKeyValueModel> getCaseTypes();

	List<RdsJudicialKeyValueModel> getCaseFeeTypes();
	
	List<RdsJudicialKeyValueModel> getReportModelByPartner(Map<String, Object> params);

}
