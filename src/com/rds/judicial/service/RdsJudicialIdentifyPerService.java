package com.rds.judicial.service;

import com.rds.judicial.model.RdsJudicialIdentifyModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/10/26
 */
public interface RdsJudicialIdentifyPerService {
    List<RdsJudicialIdentifyModel> queryIdentify(String laboratory_no);

    int insertCaseToIdentify(Map<String, Object> params);

    List<RdsJudicialIdentifyModel> queryIdentifyByCaseId(String case_id);
    
    
 int  queryIdentifynameByCaseId(String case_id);

}
