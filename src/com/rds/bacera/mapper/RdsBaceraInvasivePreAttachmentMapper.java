package com.rds.bacera.mapper;

import java.util.List;
import java.util.Map;

import com.rds.bacera.model.RdsBaceraInvasivePreAttachmentModel;
import com.rds.judicial.model.RdsJudicialAttachmentModel;

public interface RdsBaceraInvasivePreAttachmentMapper {
	
	void insertAttachment(Map<String,Object> params);

    int deleteAttachement(Map<String,Object> params);

    List<RdsBaceraInvasivePreAttachmentModel> queryAttachment(Map<String,Object> params);
   //根据uuid查询记录
    int queryCountAttachment(String params);
    //查询所有记录条数
    int queryAllCount(Map<String,Object> params);
    
    int queryCountSameAttachment(String params);
    
    int updateAttachment (Map<String,Object> params);
    
    int insertExperimentLog(Map<String,Object> params);
    
    int updateExperimentLog(Map<String,Object> params);
    
    List<RdsJudicialAttachmentModel> queryExperimentLog(Map<String,Object> params);
    
    int countExperimentLog(Map<String,Object> params);

	

	

}
