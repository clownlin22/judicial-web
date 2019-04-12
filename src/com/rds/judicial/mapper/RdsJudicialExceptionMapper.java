package com.rds.judicial.mapper;

import com.rds.judicial.model.RdsJudicialExceptionModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/16
 */
public interface RdsJudicialExceptionMapper extends RdsJudicialExperimentBaseMapper{
	
    List<RdsJudicialExceptionModel> queryCrossCompare(Map<String, Object> params);

    int queryCountCrossCompare(Map<String, Object> params);
}
