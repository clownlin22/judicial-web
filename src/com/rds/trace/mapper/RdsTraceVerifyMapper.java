package com.rds.trace.mapper;

import com.rds.trace.model.RdsTraceCaseInfoModel;
import com.rds.trace.model.RdsTraceCaseInfoModelExt;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/23
 */
public interface RdsTraceVerifyMapper {
    void insertVerify(Map<String,Object> params);

    List<RdsTraceCaseInfoModelExt> queryVerify(Map<String,Object> params);

    int queryCountVerify(Map<String,Object> params);

    List<RdsTraceCaseInfoModel> queryBaseinfoVerifyByCaseid(String caseid);
}
