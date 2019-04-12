package com.rds.trace.mapper;

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
public interface RdsTraceArchiveMapper {
    void addArchiveInfo(Map<String,Object> params);

    List<RdsTraceArchiveModel> getArchiveInfo(Map<String,Object> params);

    int countArchiveInfo(Map<String,Object> params);

    List<RdsTraceArchiveReadModel> getReadInfo(Map<String,Object> params);

    void addReadInfo(Map<String,Object> params);


}
