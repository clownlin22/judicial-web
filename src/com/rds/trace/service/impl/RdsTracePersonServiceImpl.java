package com.rds.trace.service.impl;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.trace.mapper.RdsTracePersonMapper;
import com.rds.trace.model.RdsTracePersonInfoModel;
import com.rds.trace.service.RdsTracePersonService;
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
@Service("RdsTracePersonServiceImpl")
public class RdsTracePersonServiceImpl implements RdsTracePersonService{
    @Setter
    @Autowired
    RdsTracePersonMapper rdsTracePersonMapper;

    @Override
    public List<RdsTracePersonInfoModel> queryPerson(Map<String, Object> params) {
        return rdsTracePersonMapper.queryPerson(params);
    }

    @Override
    public int queryCountPerson(Map<String, Object> params) {
        return rdsTracePersonMapper.queryCountPerson(params);
    }

    @Override
    public void insertPerson(Map<String, Object> params) {
        params.put("uuid", UUIDUtil.getUUID());
        rdsTracePersonMapper.insertPerson(params);
    }

    @Override
    public int deletePerson(String case_id) {
        return rdsTracePersonMapper.deletePerson(case_id);
    }

    @Override
    public int updatePerson(Map<String, Object> params) {
        return rdsTracePersonMapper.updatePerson(params);
    }
}
