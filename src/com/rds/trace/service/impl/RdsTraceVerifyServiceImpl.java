package com.rds.trace.service.impl;

import com.rds.code.utils.StringUtils;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.trace.mapper.RdsTraceVerifyMapper;
import com.rds.trace.model.RdsTraceCaseInfoModel;
import com.rds.trace.model.RdsTraceCaseInfoModelExt;
import com.rds.trace.service.RdsTraceVerifyService;
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
 * @date 2015/7/23
 */
@Service("RdsTraceVerifyService")
public class RdsTraceVerifyServiceImpl implements RdsTraceVerifyService{
    @Setter
    @Autowired
    RdsTraceVerifyMapper rdsTraceVerifyMapper;

    @Override
    public void insertVerify(Map<String, Object> params) {
        params.put("uuid", UUIDUtil.getUUID());
        params.put("verify_baseinfo_time", StringUtils.dateToSecond(new Date()));
        rdsTraceVerifyMapper.insertVerify(params);
    }

    @Override
    public List<RdsTraceCaseInfoModelExt> queryVerify(Map<String, Object> params) {
        return rdsTraceVerifyMapper.queryVerify(params);
    }

    @Override
    public int queryCountVerify(Map<String, Object> params) {
        return rdsTraceVerifyMapper.queryCountVerify(params);
    }

    @Override
    public List<RdsTraceCaseInfoModel> queryBaseinfoVerifyByCaseid(String case_id) {
        return rdsTraceVerifyMapper.queryBaseinfoVerifyByCaseid(case_id);
    }
}
