package com.rds.narcotics.service;

import com.rds.narcotics.model.RdsNarcoticsResponse;

import java.util.Map;

public interface RdsNarcoticsIdentifyService {

    RdsNarcoticsResponse getIdentifyInfo(Map<String, Object> params);

    boolean exsitper_name(Object per_name);

    boolean exsitper_code(Object per_code);

    Integer insert(Map<String, Object> params);

    Integer update(Map<String, Object> params);

    Integer delete(Map<String, Object> params);
}
