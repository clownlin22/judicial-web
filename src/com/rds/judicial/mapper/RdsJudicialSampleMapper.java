package com.rds.judicial.mapper;


import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialMissingDataModel;
import com.rds.judicial.model.RdsJudicialPhoneHistoryModel;
import com.rds.judicial.model.RdsJudicialPiInfoModel;
import com.rds.judicial.model.RdsJudicialSampleResultDataHis;
import com.rds.judicial.model.RdsJudicialSampleResultModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/13
 */
public interface RdsJudicialSampleMapper extends RdsJudicialExperimentBaseMapper{
    void addRecordStr(Map<String, Object> params);

    List<RdsJudicialSampleResultModel> queryBySampleCode(Map<String, Object> params);

    List<RdsJudicialSampleResultModel> queryByExperimentNo(Map<String, Object> params);

    List<String> queryOtherRecord(Map<String, Object> params);

    List<RdsJudicialSampleResultModel> queryByCaseCode(Map<String,Object> paramsout);

    RdsJudicialSampleResultModel queryOneRecord(Map<String, Object> params);

    int queryCountBySampleCode(String sample_code);

    String querySampleCall(Map<String, Object> params);

    List<String> querySampleCodeByCaseCode(String case_Code);

    List<String> queryExperiment(Map<String, Object> params);

    List<RdsJudicialCaseInfoModel> queryCaseCodeBySampleCode(String sample_code);

    List<RdsJudicialMissingDataModel> queryMissingData(Map<String,Object> params);

    int queryCountMissingData(Map<String,Object> params);

    int queryMD5(String md5);

    Map<String,Object> queryCurrentCountUnmatchedNode(String sub_case_code);

    Map<String,Object> queryNeedExt(String sub_case_code);

    void addRecordData(Map<String,Object> params);
    
    void addRecordDataAll(List<RdsJudicialSampleResultModel>  listOne);

    List<Map<String,Object>> queryRecordData(Map<String,Object> params);

    List<RdsJudicialPhoneHistoryModel> queryHistory(Map<String,Object> params);

    int queryHistoryCount(Map<String,Object> params);
    
    List<RdsJudicialSampleResultDataHis> queryPiForUnmatchNode(Map<String,Object> params);
    
//	List<Map<String, String>>  newqueryOrder(Map<String,Object> params);

}
