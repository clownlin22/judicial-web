package com.rds.judicial.service;

import com.rds.judicial.model.RdsJudicialExceptionModel;
import com.rds.judicial.model.RdsJudicialResponse;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/16
 */
public interface RdsJudicialExceptionService {
    List<Object> queryAll(Map<String, Object> params);

    int queryAllCount(Map<String, Object> params);

    int update(Map<String, Object> params);

    void insert(String exception_id,String case_code,
                             String sample_code1,String sample_code2,String laboratory_no);

    List<RdsJudicialExceptionModel> queryCrossCompare(Map<String, Object> params);

    int queryCountCrossCompare(Map<String, Object> params);

}
