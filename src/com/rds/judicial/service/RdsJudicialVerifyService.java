package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialVerifyCaseInfoModel;

public interface RdsJudicialVerifyService {
	public Map<String, Object> yesVerify(Map<String, Object> params);

	public boolean noVerify(Map<String, Object> params);

    public boolean yesSampleVerify(Map<String, Object> params);

    public boolean noSampleVerify(Map<String, Object> params);

	//public RdsJudicialResponse getCaseInfo(Map<String, Object> params);

    public List<RdsJudicialVerifyCaseInfoModel> queryAll(Map<String,Object> params);

    public int queryCount(Map<String,Object> params);

    public List<Map<String, Object>> queryVerifyHistory(String case_id);

    public List<String> getSampleCodes(String[] caseCodes);
    
    public Map<String, Object> updateSampleCaseInfo(Map<String, Object> params);
    
    int updateCaseRemark(Map<String,Object> params);
    
    int updateCaseConsignment(Map<String,Object> params);
    
	int updateCaseVerifyToText(Map<String, Object> params);

}
