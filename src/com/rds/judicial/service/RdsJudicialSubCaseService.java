package com.rds.judicial.service;

import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSubCaseInfoModel;
import com.rds.judicial.model.RdsJudicialSubCaseResultModel;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/23
 */

public interface RdsJudicialSubCaseService {
    void insert(RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel);

    int update(Map<String,Object> params);

    List<Object> queryAll(Map<String,Object> params);

    int queryAllCount(Map<String,Object> params);

    int queryCountForGen(String case_code);

    int verifySampleCode(Map<String,Object> params);

    void insertCheckNegReport(Map<String,Object> params);

    int updateCheckNegReport(String case_code);

    int deleteData(String case_code);
    
    int allDelete(Map<String, String>[] data);

    RdsJudicialCaseInfoModel queryCaseInfoByCaseCode(String case_code);

    String queryReagentNameByCaseId(String case_id);

    List<RdsJudicialSubCaseResultModel> queryAllForZhengTaiExt(Map<String,Object> params);

    int queryAllCountForZhengTaiExt(Map<String,Object> params);

}
