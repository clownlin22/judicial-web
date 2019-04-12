package com.rds.trace.mapper;

import com.rds.trace.model.RdsTraceAttachmentModel;
import com.rds.trace.model.RdsTraceAttachmentModelExt;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/21
 */
public interface RdsTraceAttachmentMapper {
    void insertAttachment(Map<String,Object> params);

    int deleteJpg(String case_id);

    List<RdsTraceAttachmentModelExt> queryAttachment(Map<String,Object> params);

    int queryCountAttachment(Map<String,Object> params);
}
