package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialVerifyCaseInfoModel;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/29
 */
public interface RdsJudicialVerifyMapper {
    List<RdsJudicialVerifyCaseInfoModel> queryAll(Map<String,Object> params);

    int queryCount(Map<String,Object> params);

    int verifySampleInfo(Map<String,Object> params);

    int verifyBaseInfo(Map<String,Object> params);

    int querySampleInfoCount(String case_code);

    int updateCaseInfoBaseState(Map<String,Object> params);

    int updateCaseInfoSampleState(Map<String,Object> params);

    int deleteCase(String case_code);

    List<Map<String, Object>> queryVerifyHistory(String case_code);

	int verifyCaseInfo(Map<String, Object> params);

    List<String> getSampleCodes(String[] caseCodes);
    
    int updateCaseInfoCaseCode(Map<String,Object> params);
    
    int updateCaseRemark(Map<String,Object> params);
    
    int updateCaseConsignment(Map<String,Object> params);
    
    int updateCaseVerifyTotest(Map<String,Object> params);
}
