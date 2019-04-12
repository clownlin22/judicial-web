package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialParamsModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleExpressModel;
import com.rds.judicial.model.RdsJudicialSecondModel;
import com.rds.upc.model.RdsUpcUserModel;

public interface RdsJudicialRegisterService {

	RdsJudicialResponse getCaseInfo(Map<String, Object> params);

	boolean deleteCaseInfo(Map<String, Object> params);

	int deleteSampleInfo(Map<String, Object> params);

	RdsJudicialResponse getSampleInfo(Map<String, Object> params);

	Object saveCaseInfo(Map<String, Object> params, RdsUpcUserModel user);
	
	Object saveZYCaseInfo(Map<String, Object> params, RdsUpcUserModel user) ;
	// void exportCaseInfo(RdsJudicialParamsModel params, HttpServletResponse
	// response);

	Object updateCaseInfo(Map<String, Object> params, RdsUpcUserModel user);
	
	Object updateChangeCaseInfo(Map<String, Object> params, RdsUpcUserModel user);

	boolean exsitCaseCode(Map<String, Object> params);

	boolean exsitSampleCode(Map<String, Object> params);

	boolean verifyId_Number(Map<String, Object> params);

	Map<String, String> getClient(Map<String, Object> params);

	void exportSampleInfo(RdsJudicialParamsModel params,
			HttpServletResponse response);

	boolean returnCaseInfoState(Map<String, Object> params);

	List<RdsJudicialSampleExpressModel> querySampleExpress(
			Map<String, Object> params);

	int insertSampleExpress(Map<String, Object> params);

	int updateSampleExpress(Map<String, Object> params);

	int delSampleExpress(Map<String, Object> params);

	int updateCaseVerifyState(Map<String, Object> params);

	int insertCaseCodeSecond(Map<String, Object> params);

	List<RdsJudicialSecondModel> queryCaseCodeSecond(Map<String, Object> params);
	
	int countCaseInfo(Map<String, Object> params);
	
	/**
	 * 批量插入样本条形码
	 * @param params
	 * @return
	 */
	int insertSampleCode(Map<String, Object> params);
	
	/**
	 * 分页查询样本条形码
	 * @param params
	 * @return
	 */
	RdsJudicialResponse queryApplySampleCode(Map<String, Object> params);
	
	int countApplySampleCode(Map<String, Object> params);
	
	int queryMaxApplyBefore();
	
	int queryMaxApplyAfter();
	
	boolean addCaseFeeOther(Map<String, Object> params);
	
	RdsJudicialResponse getChangeCaseInfo(Map<String, Object> params);

}