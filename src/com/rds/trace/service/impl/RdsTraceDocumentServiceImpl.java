package com.rds.trace.service.impl;

import com.rds.code.utils.StringUtils;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.trace.mapper.RdsTraceDocumentMapper;
import com.rds.trace.model.RdsTraceDocumentModel;
import com.rds.trace.service.RdsTraceDocumentService;
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
@Service("RdsTraceDocumentService")
public class RdsTraceDocumentServiceImpl implements RdsTraceDocumentService {

    @Autowired
    @Setter
    RdsTraceDocumentMapper rdsTraceDocumentMapper;

    @Override
    public List<RdsTraceDocumentModel> queryDocument(Map<String, Object> params) {
        return rdsTraceDocumentMapper.queryDocument(params);
    }

    @Override
    public int queryCountDocument(Map<String, Object> params) {
        return rdsTraceDocumentMapper.queryCountDocument(params);
    }

    @Override
    public void insertDocument(Map<String, Object> params) {
        params.put("document_id", UUIDUtil.getUUID());
        params.put("create_time", StringUtils.dateToTen(new Date()));
        rdsTraceDocumentMapper.insertDocument(params);
    }

    @Override
    public int updateDocument(Map<String, Object> params) {
        return rdsTraceDocumentMapper.updateDocument(params);
    }

    @Override
    public int deleteDocument(Map<String, Object> params) {
        return rdsTraceDocumentMapper.deleteDocument(params);
    }
}
