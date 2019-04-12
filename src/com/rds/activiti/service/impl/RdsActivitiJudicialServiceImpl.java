package com.rds.activiti.service.impl;

import com.rds.activiti.mapper.RdsActivitiJudicialMapper;
import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.upc.model.RdsUpcUserModel;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author XiangKang on 2017/1/17.
 */
@Service
public class RdsActivitiJudicialServiceImpl implements RdsActivitiJudicialService {
    @Autowired
    private TaskService taskService;

    @Autowired
    private RdsActivitiJudicialMapper rdsActivitiJudicialMapper;


    @Override
    public boolean runByCaseCode(String case_code, Map<String, Object> variables, RdsUpcUserModel user) {
        String taskId = rdsActivitiJudicialMapper.getCaseTask(case_code);
        String processInstanceId =  rdsActivitiJudicialMapper.getCaseProcessInstanceId(case_code);
        if(null != variables.get("comment"))
        	taskService.addComment(taskId, processInstanceId, variables.get("comment").toString());
        if(taskId==null){
            return false;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return false;
        }
        if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
            taskService.claim(taskId, user.getUsername());
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId, variables);
        }
        return true;
    }

    @Override
    public boolean runByCaseCode(String case_code, String currentTaskDefKey, Map<String, Object> variables, RdsUpcUserModel user) {
        String taskId = rdsActivitiJudicialMapper.getCaseTask(case_code);
        if (taskId == null) {
            return false;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return false;
        }
        if (!task.getTaskDefinitionKey().equals(currentTaskDefKey)) {
            return false;
        }
        if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
            taskService.claim(taskId, user.getUsername());
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId, variables);
        }
        return true;
    }

	@Override
	public boolean addComment(String case_code, Map<String, Object> variables,
			RdsUpcUserModel user) {
		 String taskId = rdsActivitiJudicialMapper.getCaseTask(case_code);
	        String processInstanceId =  rdsActivitiJudicialMapper.getCaseProcessInstanceId(case_code);
        if(null != variables.get("comment"))
        	taskService.addComment(taskId, processInstanceId, variables.get("comment").toString());
		return true;
	}

	@Override
	public boolean runByCaseNum(String num, Map<String, Object> variables,
			RdsUpcUserModel user) {

        String taskId = rdsActivitiJudicialMapper.getCaseTask1(num);
        String processInstanceId =  rdsActivitiJudicialMapper.getCaseProcessInstanceId1(num);
        if(null != variables.get("comment"))
        	taskService.addComment(taskId, processInstanceId, variables.get("comment").toString());
        if(taskId==null){
            return false;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return false;
        }
        if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
            taskService.claim(taskId, user.getUsername());
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId, variables);
        }
        return true;
    
	}

	/**
	 * 儿童基因库流程节点流转
	 */
	@Override
	public boolean runByChildCaseCode(String case_code,
			Map<String, Object> variables, RdsUpcUserModel user) {
		//根据case_code查询任务id
        String taskId = rdsActivitiJudicialMapper.getChildCaseTask(case_code);
        String processInstanceId =  rdsActivitiJudicialMapper.getChildCaseProcessInstanceId(case_code);
        if(null != variables.get("comment"))
        	taskService.addComment(taskId, processInstanceId, variables.get("comment").toString());
        if(taskId==null){
            return false;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return false;
        }
        if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
            taskService.claim(taskId, user.getUsername());
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId, variables);
        }
        return true;
    
	}
	
	/**
	 * 儿童基因库流程节点流转
	 */
	@Override
	public boolean runByChildCaseCode(String case_code,String currentTaskDefKey,
			Map<String, Object> variables, RdsUpcUserModel user) {
		//根据case_code查询任务id
        String taskId = rdsActivitiJudicialMapper.getChildCaseTask(case_code);
        String processInstanceId =  rdsActivitiJudicialMapper.getChildCaseProcessInstanceId(case_code);
        if(null != variables.get("comment"))
        	taskService.addComment(taskId, processInstanceId, variables.get("comment").toString());
        if(taskId==null){
            return false;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return false;
        }
        if (!task.getTaskDefinitionKey().equals(currentTaskDefKey)) {
            return false;
        }
        if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
            taskService.claim(taskId, user.getUsername());
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId, variables);
        }
        return true;
    
	}
	
}
