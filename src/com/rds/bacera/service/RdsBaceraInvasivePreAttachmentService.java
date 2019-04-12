package com.rds.bacera.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.rds.bacera.model.RdsBaceraInvasivePreAttachmentModel;
import com.rds.judicial.model.RdsJudicialAttachmentModel;

public interface RdsBaceraInvasivePreAttachmentService {

	    void insertAttachement(Map<String,Object> params);

	    List<RdsBaceraInvasivePreAttachmentModel> queryAttachment(Map<String,Object> params);

	    String getDataPath();
	    
        int mailAttachment(Map<String,Object> params);
        
        int onMailOver(Map<String,Object> params);
        
        int onFile(Map<String,Object> params);
        
	    int queryCountAttachment(Map<String,Object> params);
	    
	    int queryAllCount(Map<String,Object> params);

	    void download(HttpServletResponse response, Map<String,Object> map) throws Exception;

	    Map<String, Object> upload(String userid,String id,String num,String attachment_path,String task_def_key,MultipartFile[] files)
	            throws Exception;

	    int updateAttachment(Map<String,Object> params);
	    
	    int deleteAttachment(Map<String,Object> params);
	    
	    List<RdsJudicialAttachmentModel> queryExperimentLog(Map<String,Object> params);
	    
	    int countExperimentLog(Map<String,Object> params); 

	    void exportExperimentLog(Map<String, Object> params,HttpServletResponse response) throws Exception;
	    
	    void exportPreAttachment(Map<String, Object> params,HttpServletResponse response)throws Exception;
	}


