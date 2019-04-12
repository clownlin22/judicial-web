package com.rds.statistics.mapper;

import java.util.List;
import java.util.Map;

import com.rds.statistics.model.RdsExperimentalReportModel;

public interface RdsExperimentalReportMapper {

	//添加报告
	int insertReport(Map<String,Object> params);
	//添加工作
	int insert(Map<String,Object> params);
  
    int deleteReport(Map<String,Object> params);
    
    int deleteSubject(Map<String,Object> params);
    
    int queryAllCount(Map<String,Object> params);
  
    List<RdsExperimentalReportModel> queryAllReport(Map<String,Object> params);
    //下载附件查询附件
    RdsExperimentalReportModel queryReport(String uuid);
    
    int queryReportByName(Map<String,Object> map);
    
    List<RdsExperimentalReportModel> queryAllSubject(Map<String,Object> params);
    
    int querySubject(String subject);
    
    RdsExperimentalReportModel querySubjectById(String id);

    
	int updateReport(Map<String,Object> params);
}
