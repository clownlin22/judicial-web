package com.rds.trace.service;

import com.rds.trace.model.RdsTraceArchiveModel;
import com.rds.trace.model.RdsTraceArchiveReadModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/30
 */
public interface RdsTraceArchiveService {
    boolean addArchiveInfo(Map<String,Object> params);

    List<RdsTraceArchiveModel> getArchiveInfo(Map<String,Object> params);

    int countArchiveInfo(Map<String,Object> params);

    List<RdsTraceArchiveReadModel> getReadInfo(Map<String,Object> params);

    boolean addReadInfo(Map<String,Object> params);
}
