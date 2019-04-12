package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialRelaySampleInfo;
import com.rds.judicial.model.RdsJudicialSampleCaseCodeModel;
import com.rds.judicial.model.RdsJudicialSampleConfirmModel;
import com.rds.judicial.model.RdsJudicialSampleReceiveModel;
import com.rds.judicial.model.RdsJudicialSampleRelayModel;

public interface RdsJudicialSampleRelayMapper {

	int countSampleReceiveInfos(Map<String, Object> params);

	List<RdsJudicialSampleReceiveModel> getSampleReceiveInfos(
			Map<String, Object> params);

	List<RdsJudicialRelaySampleInfo> getReceiveSampleInfo(Map<String, Object> params);

	int exsitReceiveSampleCode(Map<String, Object> params);

	boolean addReceiveSampleInfo(RdsJudicialSampleReceiveModel receiveModel);

	int addReceiveSample(RdsJudicialRelaySampleInfo sampleInfo);

	boolean deleteReceiveSampleInfo(Map<String, Object> params);

	int deleteReceiveSample(RdsJudicialSampleReceiveModel receiveModel);

	boolean updateReceiveSampleInfo(RdsJudicialSampleReceiveModel receiveModel);
	
	List<RdsJudicialSampleRelayModel> getSampleRelayInfos(
			Map<String, Object> params);

	int countSampleRelayInfos(Map<String, Object> params);

	List<RdsJudicialRelaySampleInfo> getRelaySampleInfo(
			Map<String, Object> params);

	boolean addRelaySampleInfo(RdsJudicialSampleRelayModel sampleRelayModel);

	int addRelaySample(RdsJudicialRelaySampleInfo sampleInfo);

	boolean deleteRelaySampleInfo(Map<String, Object> params);

	boolean updateRelaySampleInfo(RdsJudicialSampleRelayModel relayModel);

	int deleteRelaySample(RdsJudicialSampleRelayModel relayModel);

	boolean confirmRelaySampleInfo(RdsJudicialSampleConfirmModel confirmModel);

	RdsJudicialSampleRelayModel getRelaySample(Map<String, Object> params);

	List<RdsJudicialRelaySampleInfo> getSelectSampleInfo(
			Map<String, Object> params);

	List<RdsJudicialDicAreaModel> getSampleInfo(String relay_id);

	void updateSampleInfoToFalse(Map<String, Object> params);

	void updateSampleInfo(RdsJudicialRelaySampleInfo relayInfo);
	
	List<String> querySampleByCode(Map<String, Object> params);
	
	int queryCaseCodeVerify(String params);
	
	List<String> querySampleCaseCode(Map<String, Object> params);
	
	List<String> queryCaseCodes(Map<String, Object> params);
	
	boolean updateCaseVerifyBycode(Map<String, Object> params);
	
	List<String> querySampleCodeByid(Map<String, Object> params);
	
	List<String> querySampleCodeByCaseid(Map<String, Object> params);
	
	
	List<String> queryCaseVerifyState(Map<String, Object> params);
	
	List<String> queryFalseSampleCodeByid(Map<String, Object> params);
	
	List<Map<String,Object>> querySampleCodeConfirm(Map<String, Object> params);
	
	List<Map<String,Object>> queryCaseVerifyBySample(Map<String, Object> params);
	
	List<RdsJudicialSampleCaseCodeModel> queryCaseCodeBySampleCode(Map<String, Object> params);
	
	int countCaseCodeBySampleCode(Map<String, Object> params);
	
	boolean updateCaseReportmodel(Map<String, Object> params);
	
	//导出案例对应样本编号
	List<RdsJudicialSampleCaseCodeModel> exportCaseCodeBySampleCode(Map<String, Object> params);
	
	List<String> querySampleIdByRece(Map<String, Object> params);
	
	List<String> exportSampleCodeByCaseId (Map<String, Object> params);
	
	List<String> getReceiveSamplecode  (Map<String, Object> params);
	List<String> getCaseCodeBycaseId(Map<String, Object> params);
	List<String> getSampleCodesBycaseId(Map<String, Object> params);
	
	List<String> queryCaseCodeByCaseids(Map<String, Object> params);
	  
	void updateReceiveSamplecode(Map<String, Object> params);
	
	List<String> querySampleCaseCode2(Map<String, Object> params);
}
