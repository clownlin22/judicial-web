package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.rds.judicial.model.RdsJudicialAttachmentModel;

public interface RdsJudicialAttachmentService {
    void insertAttachement(Map<String,Object> params);

    List<RdsJudicialAttachmentModel> queryAttachment(Map<String,Object> params);

    String getDataPath();

    int queryCountAttachment(Map<String,Object> params);

    void download(HttpServletResponse response, Map<String,Object> map) throws Exception;

    Map<String, Object> upload(String userid,MultipartFile[] files)
            throws Exception;

    int updateAttachment(Map<String,Object> params);
    
    int deleteAttachment(Map<String,Object> params);
    
    List<RdsJudicialAttachmentModel> queryExperimentLog(Map<String,Object> params);
    
    int countExperimentLog(Map<String,Object> params); 

    void exportExperimentLog(Map<String, Object> params,HttpServletResponse response) throws Exception;
}
