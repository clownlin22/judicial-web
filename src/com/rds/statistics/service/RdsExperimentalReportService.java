package com.rds.statistics.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;



import org.springframework.web.multipart.MultipartFile;

import com.rds.statistics.model.RdsExperimentalReportModel;

public interface RdsExperimentalReportService {
	


    int insertReport(Map<String,Object> params);
    
    int insert(Map<String,Object> params);

    List<RdsExperimentalReportModel> queryAllReport(Map<String,Object> params);
    
    RdsExperimentalReportModel queryReport(String uuid);

    int querySubject(String subject);
    
    List<RdsExperimentalReportModel> queryAllSubject(Map<String,Object> params);

    int queryAllCount(Map<String,Object> params);

    void download(HttpServletResponse response, Map<String,Object> map) throws Exception;
    //删除单个附件
    int deleteReport(Map<String,Object> params);
    //删除任务
    int deleteSubject(Map<String,Object> params);
    
    int updateReport(Map<String,Object> params);
  
    Map<String, Object> upload(String username,String report,String id,MultipartFile[] files)
            throws Exception;

}
