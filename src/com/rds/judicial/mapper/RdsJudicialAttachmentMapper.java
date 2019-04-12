package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialAttachmentModel;


public interface RdsJudicialAttachmentMapper {
    void insertAttachment(Map<String,Object> params);

    int deleteAttachement(String uuid);

    List<RdsJudicialAttachmentModel> queryAttachment(Map<String,Object> params);

    int queryCountAttachment(Map<String,Object> params);
    
    int updateAttachment(Map<String,Object> params);
    
    int insertExperimentLog(Map<String,Object> params);
    
    int updateExperimentLog(Map<String,Object> params);
    
    List<RdsJudicialAttachmentModel> queryExperimentLog(Map<String,Object> params);
    
    int countExperimentLog(Map<String,Object> params);
}
