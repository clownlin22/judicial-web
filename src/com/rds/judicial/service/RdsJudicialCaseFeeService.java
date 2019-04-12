package com.rds.judicial.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialResponse;

public interface RdsJudicialCaseFeeService {

	RdsJudicialResponse getCaseFeeInfo(Map<String, Object> params);

	boolean saveCaseFee(Map<String, Object> params);

	boolean insertOAnum(Map<String, Object> params);
	
	boolean caseFeeConfirm(Map<String, Object> params);

	void exportCaseInfoOther(Map<String, Object> params,HttpServletResponse response) throws Exception;
}
