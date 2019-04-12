package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialResponse;

/**
 * 百会亲子鉴定-service
 * @author yxb
 *
 */
public interface RdsBaceraIdentifyService {

	Object getCaseInfo(Map<String, Object> params);

	boolean deleteCaseInfo(Map<String, Object> params);

	int deleteSampleInfo(Map<String, Object> params);

	RdsJudicialResponse getSampleInfo(Map<String, Object> params);

	boolean saveCaseInfo(Map<String, Object> params);

	//void exportCaseInfo(RdsJudicialParamsModel params, HttpServletResponse response);

	boolean updateCaseInfo(Map<String, Object> params);

	boolean exsitCaseCode(Map<String, Object> params);

	boolean exsitSampleCode(Map<String, Object> params);

	boolean verifyId_Number(Map<String, Object> params);

	void exportIdentifyInfo(Map<String, Object> params,HttpServletResponse response) throws Exception;

	void exportIdentifyInfoQM(Map<String, Object> params,HttpServletResponse response) throws Exception;

	public Map<String, Object> getStandFee(Integer typeid, Integer pernum,String areaid, Integer case_type);

}