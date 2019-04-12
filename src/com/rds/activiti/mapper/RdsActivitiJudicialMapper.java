package com.rds.activiti.mapper;

/**
 * @author XiangKang on 2017/1/17.
 */
public interface RdsActivitiJudicialMapper {
    /*根据案例编号查询所处任务ID*/
    String getCaseTask(String case_code);
    
    String getCaseTask1(String id);
    
    String getCaseProcessInstanceId(String case_code);
    
    String getCaseProcessInstanceId1(String id);
    
    String getChildCaseTask(String case_code);
    
    String getChildCaseProcessInstanceId(String case_code);
}
