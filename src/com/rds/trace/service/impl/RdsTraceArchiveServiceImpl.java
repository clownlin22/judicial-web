package com.rds.trace.service.impl;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.trace.mapper.RdsTraceArchiveMapper;
import com.rds.trace.model.RdsTraceArchiveModel;
import com.rds.trace.model.RdsTraceArchiveReadModel;
import com.rds.trace.model.RdsTraceCaseInfoModel;
import com.rds.trace.service.RdsTraceArchiveService;
import com.rds.trace.service.RdsTraceRegisterService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/30
 */
@Service("RdsTraceArchiveService")
public class RdsTraceArchiveServiceImpl implements RdsTraceArchiveService{
    @Autowired
    @Setter
    private RdsTraceArchiveMapper rdsTraceArchiveMapper;

    @Autowired
    @Setter
    private RdsTraceRegisterService rdsTraceRegisterService;

    @Override
    public int countArchiveInfo(Map<String, Object> params) {
        return rdsTraceArchiveMapper.countArchiveInfo(params);
    }

    @Override
    public List<RdsTraceArchiveModel> getArchiveInfo(Map<String, Object> params) {
        return rdsTraceArchiveMapper.getArchiveInfo(params);
    }

    @Override
    public List<RdsTraceArchiveReadModel> getReadInfo(Map<String, Object> params) {
        return rdsTraceArchiveMapper.getReadInfo(params);
    }

    @Override
    public boolean addArchiveInfo(Map<String, Object> params) {
        params.put("archive_id", UUIDUtil.getUUID());
        params.put("archive_code",
                new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
        rdsTraceArchiveMapper.addArchiveInfo(params);
        Map<String,Object> status = new HashMap<>();
        status.put("case_id",params.get("case_id"));
        status.put("status", RdsTraceCaseInfoModel.ARCHIVED);
        rdsTraceRegisterService.updateStatus(status);
        return true;
    }

    @Override
    public boolean addReadInfo(Map<String, Object> params) {
        params.put("id", UUIDUtil.getUUID());
        rdsTraceArchiveMapper.addReadInfo(params);
        return true;
    }
}
