package com.rds.trace.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.trace.model.RdsTraceCaseInfoModelExt;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/20
 */
public interface RdsTraceRegisterService {
    List<RdsTraceCaseInfoModelExt> queryCaseInfo(Map<String,Object> params);

    int queryCountCaseInfo(Map<String,Object> params);

    void insertCaseInfo(Map<String,Object> params) throws Exception;

    boolean updateCaseInfo(Map<String,Object> params)throws Exception;

    int deleteCaseInfo(String case_id);

    int updateStatus(Map<String,Object> params);

    RdsTraceCaseInfoModelExt queryByCaseId(String case_id);

    void exportInfo(String case_no, String start_time, String end_time,
                           String status, Integer is_delete, String invoice_number,HttpServletResponse response);
    boolean traceMailDely(Map<String,Object> params);
}