package com.rds.trace.service;

import com.rds.trace.model.RdsTraceMailModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/29
 */
public interface RdsTraceMailService {
    boolean delMailInfo(Map<String,Object> params);

    boolean addMailInfo(Map<String,Object> params);

    boolean updateMailInfo(Map<String,Object> params);

    List<RdsTraceMailModel> getMailInfo(Map<String,Object> params);

    int getCountMailInfo(Map<String,Object> params);
}
