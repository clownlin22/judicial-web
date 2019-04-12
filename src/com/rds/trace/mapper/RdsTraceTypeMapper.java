package com.rds.trace.mapper;

import com.rds.trace.model.RdsTraceTypeModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/9/15
 */
public interface RdsTraceTypeMapper {
    List<RdsTraceTypeModel> queryType(Map<String,Object> params);

    int queryCountType(Map<String,Object> params);

    void insertType(Map<String,Object> params);

    int updateType(Map<String,Object> params);

    int deleteType(Map<String,Object> params);
}
