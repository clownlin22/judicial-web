package com.rds.trace.service.impl;

import com.rds.code.utils.StringUtils;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.trace.mapper.RdsTraceTypeMapper;
import com.rds.trace.model.RdsTraceTypeModel;
import com.rds.trace.service.RdsTraceTypeService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/9/15
 */
@Service("RdsTraceTypeService")
public class RdsTraceTypeServiceImpl implements RdsTraceTypeService{

    @Autowired
    @Setter
    RdsTraceTypeMapper rdsTraceTypeMapper;

    @Override
    public List<RdsTraceTypeModel> queryType(Map<String, Object> params) {
        return rdsTraceTypeMapper.queryType(params);
    }

    @Override
    public int queryCountType(Map<String, Object> params) {
        return rdsTraceTypeMapper.queryCountType(params);
    }

    @Override
    public void insertType(Map<String, Object> params) {
        params.put("type_id", UUIDUtil.getUUID());
        params.put("create_time", StringUtils.dateToTen(new Date()));
        rdsTraceTypeMapper.insertType(params);
    }

    @Override
    public int updateType(Map<String, Object> params) {
        return rdsTraceTypeMapper.updateType(params);
    }

    @Override
    public int deleteType(Map<String, Object> params) {
        return rdsTraceTypeMapper.deleteType(params);
    }
}
