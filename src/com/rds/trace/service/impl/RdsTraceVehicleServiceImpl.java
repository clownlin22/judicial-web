package com.rds.trace.service.impl;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.trace.mapper.RdsTraceVehicleMapper;
import com.rds.trace.model.RdsTraceVehicleInfoModel;
import com.rds.trace.service.RdsTraceVehicleService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/22
 */
@Service("RdsTraceVehicleServiceImpl")
public class RdsTraceVehicleServiceImpl implements RdsTraceVehicleService{
    @Setter
    @Autowired
    RdsTraceVehicleMapper rdsTraceVehicleMapper;

    @Override
    public List<RdsTraceVehicleInfoModel> queryVehicle(Map<String, Object> params) {
        return rdsTraceVehicleMapper.queryVehicle(params);
    }

    @Override
    public int queryCountVehicle(Map<String, Object> params) {
        return rdsTraceVehicleMapper.queryCountVehicle(params);
    }

    @Override
    public void insertVehicle(Map<String, Object> params) {
        params.put("uuid", UUIDUtil.getUUID());
        rdsTraceVehicleMapper.insertVehicle(params);
    }

    @Override
    public int deleteVehicle(String case_id) {
        return rdsTraceVehicleMapper.deleteVehicle(case_id);
    }

    @Override
    public int updateVehicle(Map<String, Object> params) {
        return rdsTraceVehicleMapper.updateVehicle(params);
    }
}
