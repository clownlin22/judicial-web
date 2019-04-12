package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialConfirmCaseInfo;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleReciveModel;

public interface RdsJudicialCaseRelayService {

	RdsJudicialResponse getCaseRelayInfo(Map<String, Object> params);

	List<RdsJudicialConfirmCaseInfo> getRelayCaseInfo(Map<String, Object> params);

	RdsJudicialResponse getPrintCaseCode(Map<String, Object> params);

	boolean saveCaseRelayInfo(Map<String, Object> params);

	boolean updateCaseRelayInfo(Map<String, Object> params);

	boolean deleteRelayCaseInfo(Map<String, Object> params);

	List<RdsJudicialSampleReciveModel> getCaseInfo(String relay_id);

	boolean confirmCaseRelayInfo(Map<String, Object> params);

	List<RdsJudicialConfirmCaseInfo> getRelayCaseCode(Map<String, Object> params);
	
	List<RdsJudicialConfirmCaseInfo> getPrintCaseCodeOnline(String relay_id);
	
	void exportCaseCode(Map<String, Object> params,
			HttpServletResponse response) throws Exception;

}
