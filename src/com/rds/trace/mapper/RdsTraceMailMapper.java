package com.rds.trace.mapper;

import com.rds.trace.model.RdsTraceMailModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/29
 */
public interface RdsTraceMailMapper {
    int delMailInfo(Map<String,Object> params);

    void addMailInfo(Map<String,Object> params);

    int updateMailInfo(Map<String,Object> params);

    List<RdsTraceMailModel> getMailInfo(Map<String,Object> params);

    int getCountMailInfo(Map<String,Object> params);
}
