package com.rds.trace.service;

import com.rds.trace.model.RdsTraceVehicleInfoModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/22
 */
public interface RdsTraceVehicleService {
    void insertVehicle(Map<String,Object> params);

    List<RdsTraceVehicleInfoModel> queryVehicle(Map<String,Object> params);

    int queryCountVehicle(Map<String,Object> params);

    int updateVehicle(Map<String,Object> params);

    int deleteVehicle(String uuid);
}
