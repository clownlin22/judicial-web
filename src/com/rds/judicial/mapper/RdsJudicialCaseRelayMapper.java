package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialCaseConfirmModel;
import com.rds.judicial.model.RdsJudicialCaseRelayModel;
import com.rds.judicial.model.RdsJudicialConfirmCaseInfo;
import com.rds.judicial.model.RdsJudicialSampleReciveModel;

public interface RdsJudicialCaseRelayMapper {

	int countCaseRelays(Map<String, Object> params);

	List<RdsJudicialCaseConfirmModel> getCaseRelays(Map<String, Object> params);

	List<RdsJudicialConfirmCaseInfo> getPrintCaseCode(Map<String, Object> params);

	boolean saveCaseRelayInfo(RdsJudicialCaseRelayModel caseRelayModel);

	void addCaseInfo(RdsJudicialConfirmCaseInfo caseInfo);

	List<RdsJudicialConfirmCaseInfo> getRelayCaseInfo(Map<String, Object> params);

	void deleteCaseInfo(RdsJudicialCaseRelayModel caseRelayModel);

	boolean updateCaseRelayInfo(RdsJudicialCaseRelayModel caseRelayModel);

	boolean deleteRelayCaseInfo(Map<String, Object> params);

	List<RdsJudicialSampleReciveModel> getCaseInfo(String relay_id);

	boolean confirmCaseRelayInfo(RdsJudicialCaseConfirmModel caseConfirmModel);

	void updateCaseInfo(RdsJudicialConfirmCaseInfo caseInfo);

	void updateCaseInfoToFalse(Map<String, Object> params);

	int countPrintCaseCode(Map<String, Object> params);

	List<RdsJudicialConfirmCaseInfo> getRelayCaseCode(Map<String, Object> params);
	
	List<String> getCaseConfirmCode(String relay_id);
	
	List<RdsJudicialConfirmCaseInfo> getPrintCaseCodeOnline(String relay_id);

}
