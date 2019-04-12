package com.rds.trace.service;

import com.rds.trace.model.RdsTracePersonInfoModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/31
 */
public interface RdsTracePersonService {
    void insertPerson(Map<String, Object> params);

    List<RdsTracePersonInfoModel> queryPerson(Map<String, Object> params);

    int queryCountPerson(Map<String, Object> params);

    int updatePerson(Map<String, Object> params);

    int deletePerson(String case_id);
}
