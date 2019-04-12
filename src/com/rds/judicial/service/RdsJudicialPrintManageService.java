package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.model.RdsJudicialResponse;

public interface RdsJudicialPrintManageService {

	RdsJudicialResponse getPrintCaseInfo(Map<String, Object> params);
	
	RdsJudicialResponse getPrintInfoFoModel(Map<String, Object> params);

	List<RdsJudicialKeyValueModel> getCompany();

	List<RdsJudicialDicAreaModel> getArea(Map<String, Object> params);

	boolean saveArea(Map<String, Object> params);
	
	boolean saveDicPrintModel(Map<String, Object> params);
	

}
