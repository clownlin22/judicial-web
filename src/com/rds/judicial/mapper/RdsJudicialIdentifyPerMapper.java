package com.rds.judicial.mapper;

import com.rds.judicial.model.RdsJudicialIdentifyModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/10/26
 */
public interface RdsJudicialIdentifyPerMapper {
    List<RdsJudicialIdentifyModel> queryIdentify(String laboratory_no);

    int insertCaseToIdentify(Map<String,Object> params);

    List<RdsJudicialIdentifyModel> queryIdentifyByCaseId(String case_id);
    
    int  queryIdentifynameByCaseId(String case_id);
    
    int updateIdentifyByCaseid(Map<String,Object> params);
    
    int deleteIdentifyByCaseid(Map<String,Object> params);
}
