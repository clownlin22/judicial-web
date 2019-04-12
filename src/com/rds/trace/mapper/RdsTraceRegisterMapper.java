package com.rds.trace.mapper;

import java.util.List;
import java.util.Map;

import com.rds.trace.model.RdsTraceCaseInfoModelExt;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/20
 */
public interface RdsTraceRegisterMapper {
    List<RdsTraceCaseInfoModelExt> queryCaseInfo(Map<String,Object> params);

    int queryCountCaseInfo(Map<String,Object> params);

    int queryCountCaseInfoByYear(int year);

    void insertCaseInfo(Map<String,Object> params);

    int updateCaseInfo(Map<String,Object> params);

    int deleteCaseInfo(String case_id);

    int updateStatus(Map<String,Object> params);

    int queryCaseNo(int year);

    String queryCaseId(Map<String,Object> params);

    RdsTraceCaseInfoModelExt queryByCaseId(String case_id);

	int addCaseFee(Map<String, Object> params);
	
	boolean updateCaseFee(Map<String, Object> params);

    List<RdsTraceCaseInfoModelExt> exportInfo(Map<String, Object> params);
    
    void traceMailDely(Map<String,Object> params);
}
