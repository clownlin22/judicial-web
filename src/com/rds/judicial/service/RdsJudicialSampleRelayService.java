package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialRelaySampleInfo;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleCaseCodeModel;
import com.rds.judicial.model.RdsJudicialSampleRelayModel;
import com.rds.upc.model.RdsUpcUserModel;

public interface RdsJudicialSampleRelayService {

	RdsJudicialResponse getSampleReceiveInfos(Map<String, Object> params);

	List<RdsJudicialRelaySampleInfo> getReceiveSampleInfo(Map<String, Object> params);

	boolean exsitReceiveSampleCode(Map<String, Object> params);

	Object saveReceiveSampleInfo(Map<String, Object> params,RdsUpcUserModel user);
	
	Object saveReceiveSampleInfoAuto(Map<String, Object> params,RdsUpcUserModel user);

	boolean deleteReceiveSampleInfo(Map<String, Object> params);

	boolean updateReceiveSampleInfo(Map<String, Object> params);

	RdsJudicialResponse getSampleRelayInfos(Map<String, Object> params);

	List<RdsJudicialRelaySampleInfo> getRelaySampleInfo(
			Map<String, Object> params);

	Object saveRelaySampleInfo(Map<String, Object> params,RdsUpcUserModel user);

	boolean deleteRelaySampleInfo(Map<String, Object> params);

	boolean updateRelaySampleInfo(Map<String, Object> params);

	Object confirmRelaySampleInfo(Map<String, Object> params,RdsUpcUserModel user);

	RdsJudicialSampleRelayModel getRelaySample(Map<String, Object> params);

	List<RdsJudicialRelaySampleInfo> getSelectSampleInfo(
			Map<String, Object> params);

	List<RdsJudicialDicAreaModel> getSampleInfo(String relay_id);
	
	List<String> querySampleByCode(Map<String, Object> params);

	List<String> querySampleCaseCode(Map<String, Object> params);
	
	boolean updateCaseVerifyBycode(Map<String, Object> params);
	
	RdsJudicialResponse queryCaseCodeBySampleCode(Map<String, Object> params);
	
	int countCaseCodeBySampleCod(Map<String, Object> params);

	boolean updateCaseReportmodel(Map<String, Object> params);
	
	public void exportCaseCodeBySampleCode(Map<String, Object> params,
			HttpServletResponse response) ;
	
	List<String> querySampleIdByRece(Map<String, Object> params);
	
	Map<String,Object> saveRelaySampleInfoNow(Map<String, Object> params,RdsUpcUserModel user);
	
	void exportSampleCode(Map<String, Object> params,
			HttpServletResponse response) throws Exception;
	
	Map<String,Object> saveRelaySampleInfoNowAuto(Map<String, Object> params,RdsUpcUserModel user);
	
}
