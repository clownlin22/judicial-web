package com.rds.judicial.mapper;

import com.rds.judicial.model.RdsJudicialCompareResultTongBaoModel;
import com.rds.judicial.model.RdsJudicialSampleResultModel;

import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/20
 */
public interface RdsJudicialResultMapper {
    void addResult(Map<String,Object> params);

    void addTongbaoResult(RdsJudicialCompareResultTongBaoModel rdsJudicialCompareResultTongBaoModel);

    int updateResult(Map<String,Object> params);

    RdsJudicialSampleResultModel query(Map<String,Object> params);

    int isSecondeTimeFor2(Map<String,Object> params);

    int isSecondeTimeFor3(Map<String,Object> params);
}
