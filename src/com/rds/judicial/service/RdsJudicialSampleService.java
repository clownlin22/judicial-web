package com.rds.judicial.service;

import com.rds.judicial.model.RdsJudicialPhoneHistoryModel;
import com.rds.judicial.model.RdsJudicialSampleResultModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/9/2
 */
public interface RdsJudicialSampleService {
    List<Object> queryAll(Map<String,Object> params);

    int queryAllCount(Map<String,Object> params);

    List<RdsJudicialSampleResultModel> queryByExperimentNo(Map<String, Object> params);

    RdsJudicialSampleResultModel queryOneRecord(Map<String, Object> params);

    Map<String,Object> queryOneRecordData(Map<String,Object> params);

    List<RdsJudicialSampleResultModel> queryRecordsByCaseCode(Map<String,Object> params);

    List<RdsJudicialPhoneHistoryModel> queryHistory(Map<String,Object> params);

    int queryHistoryCount(Map<String,Object> params);
}
