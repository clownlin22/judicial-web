package com.rds.judicial.mapper;

import com.rds.judicial.model.RdsJudicialPiInfoModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/28
 */
public interface RdsJudicialCaseParamMapper {
    String queryGen(Map<String,Object> params);

    String queryGenMin(Map<String,Object> params);
    
    String queryregientgByKey(Map<String,Object> params);

    int insertPiInfo(RdsJudicialPiInfoModel rdsJudicialPiInfoModel);

    List<RdsJudicialPiInfoModel> queryPiInfo(String sub_case_code);

    List<RdsJudicialPiInfoModel>  queryPiInfobyalient(Map<String,Object> params);
    
    List<RdsJudicialPiInfoModel> queryPiByKey(Map<String,Object> params);
    
    List<RdsJudicialPiInfoModel> queryPiByKeybyone(Map<String,Object> params);
    
    String queryValuebycode(Map<String,Object> params);
}
