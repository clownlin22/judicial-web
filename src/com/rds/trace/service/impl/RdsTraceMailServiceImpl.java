package com.rds.trace.service.impl;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.trace.mapper.RdsTraceMailMapper;
import com.rds.trace.model.RdsTraceCaseInfoModel;
import com.rds.trace.model.RdsTraceMailModel;
import com.rds.trace.service.RdsTraceMailService;
import com.rds.trace.service.RdsTraceRegisterService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/29
 */
@Service("RdsTraceMailServiceImpl")
public class RdsTraceMailServiceImpl implements RdsTraceMailService{
    @Autowired
    @Setter
    RdsTraceMailMapper rdsTraceMailMapper;

    @Autowired
    @Setter
    RdsTraceRegisterService rdsTraceRegisterService;

    @Override
    public boolean addMailInfo(Map<String, Object> params) {
        params.put("mail_id", UUIDUtil.getUUID());
        rdsTraceMailMapper.addMailInfo(params);
        Map<String,Object> status = new HashMap<>();
        status.put("case_id",params.get("case_id"));
        status.put("status", RdsTraceCaseInfoModel.DELIVERED);
        rdsTraceRegisterService.updateStatus(status);
        return true;
    }

    @Override
    public boolean updateMailInfo(Map<String, Object> params) {
        if(rdsTraceMailMapper.updateMailInfo(params)>0)
            return true;
        else
            return false;
    }

    @Override
    public boolean delMailInfo(Map<String, Object> params) {
        if(rdsTraceMailMapper.delMailInfo(params)>0)
            return true;
        else
            return false;
    }

    @Override
    public int getCountMailInfo(Map<String, Object> params) {
        return rdsTraceMailMapper.getCountMailInfo(params);
    }

    @Override
    public List<RdsTraceMailModel> getMailInfo(Map<String, Object> params) {
        return rdsTraceMailMapper.getMailInfo(params);
    }
}
