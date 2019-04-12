package com.rds.trace.service;

import com.rds.trace.model.RdsTraceDocumentModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/9/15
 */
public interface RdsTraceDocumentService {
    List<RdsTraceDocumentModel> queryDocument(Map<String,Object> params);

    int queryCountDocument(Map<String,Object> params);

    void insertDocument(Map<String,Object> params);

    int updateDocument(Map<String,Object> params);

    int deleteDocument(Map<String,Object> params);
}
