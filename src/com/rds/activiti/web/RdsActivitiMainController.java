package com.rds.activiti.web;

import com.rds.activiti.model.RdsActivitiTaskDetailModel;
import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.upc.model.RdsUpcMessageModel;
import com.rds.upc.model.RdsUpcUserModel;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XiangKang on 2017/1/15.
 */
@Controller
@RequestMapping("activiti/main")
public class RdsActivitiMainController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RdsActivitiJudicialService rdsActivitiJudicialService;

    /**
     * 设置回调参数
     *
     * @param result
     * @param message
     * @return RdsUpcMessageModel
     */
    protected RdsUpcMessageModel setModel(boolean result, String message) {
        RdsUpcMessageModel model = new RdsUpcMessageModel();
        model.setResult(result);
        model.setMessage(message);
        return model;
    }

    /**
     * 签收任务
     */
    @RequestMapping("claim")
    @ResponseBody
    public Object claim(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String taskId = params.get("taskId").toString();
        String processInstanceId = params.get("processInstanceId").toString();
        String comment = params.get("comment").toString();
        RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
        identityService.setAuthenticatedUserId(user.getUsername());
        taskService.addComment(taskId, processInstanceId, comment);
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//        if (task == null) {
//            return this.setModel(false, "该流程步不存在");
//        }
//        RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
//        taskService.claim(taskId, user.getUserid());
        return this.setModel(true, "操作成功");
    }

    /**
     * 签收并处理完成
     */
    @RequestMapping("claimAndComplete")
    @ResponseBody
    public Object claimAndComplete(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String taskId = params.get("taskId").toString();
        String[] taskIds = taskId.split(",");
        
        //批量处理
        for (String string : taskIds) {
        	Map<String, Object> variables = new HashMap<String, Object>();
    		variables.put("isNoReport", 0);
            Task task = taskService.createTaskQuery().taskId(string).singleResult();
            if (task == null) {
                return this.setModel(false, "该流程步不存在");
            }
            if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
                RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
                taskService.claim(string, user.getUsername());
                taskService.complete(string,variables);
            } else {
                taskService.complete(string,variables);
            }
		}
        return this.setModel(true, "操作成功");
    }
    /**
     * 签收并处理完成
     */
    @RequestMapping("claimAndCompleteInvasivePre")
    @ResponseBody
    public Object claimAndCompleteInvasivePre(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String taskId = params.get("taskId").toString();
        String[] taskIds = taskId.split(",");
        
        //批量处理
        for (String string : taskIds) {
        	Map<String, Object> variables = new HashMap<String, Object>();
    		variables.put("ispass", 1);
            Task task = taskService.createTaskQuery().taskId(string).singleResult();
            if (task == null) {
                return this.setModel(false, "该流程步不存在");
            }
            if (task.getAssignee() == null || task.getAssignee().isEmpty()) {
                RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
                taskService.claim(string, user.getUsername());
                taskService.complete(string,variables);
            } else {
                taskService.complete(string,variables);
            }
		}
        

        return this.setModel(true, "操作成功");
    }

    /**
     * 查询流程具体步骤
     */
    @RequestMapping("taskDetail")
    @ResponseBody
    public Object taskDetail(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String processInstanceId = params.get("processInstanceId").toString();
        List<RdsActivitiTaskDetailModel> rdsActivitiTaskDetailModelList = new ArrayList<RdsActivitiTaskDetailModel>();
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().desc().list();
        for (HistoricTaskInstance item :
                historicTaskInstanceList) {
            RdsActivitiTaskDetailModel model = new RdsActivitiTaskDetailModel();
            model.setId(item.getId());
            model.setName(item.getName());
            model.setTaskDefinitionKey(item.getTaskDefinitionKey());
            model.setAssignee(item.getAssignee());
            model.setClaimTime(item.getClaimTime());
            model.setStartTime(item.getStartTime());
            model.setEndTime(item.getEndTime());
            model.setDurationInMillis(item.getDurationInMillis());

            List<Comment> comments = taskService.getTaskComments(item.getId(), "comment");
            if (comments.size() > 0) {
                model.setComment(comments.get(0).getFullMessage());
            }

            rdsActivitiTaskDetailModelList.add(model);
        }
        return rdsActivitiTaskDetailModelList;
    }

    /**
     * 查询流程具体步骤
     */
    @RequestMapping("getProcessInstanceComments")
    @ResponseBody
    public Object getProcessInstanceComments(@RequestBody Map<String, Object> params) {
        String processInstanceId = params.get("processInstanceId").toString();
        List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
        return comments;
    }

    /**
     * 根据case_code执行下一步
     */
    @RequestMapping("runByCaseCode")
    @ResponseBody
    public Object runByCaseCode(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String caseCode = params.get("case_code").toString();
        RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
        boolean result = rdsActivitiJudicialService.runByCaseCode(caseCode, null, user);
        if (result) {
            return this.setModel(true, "操作成功");
        } else {
            return this.setModel(false, "操作失败");
        }
    }

    @RequestMapping("expSuccess")
    @ResponseBody
    public Object expSuccess(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String caseCode = params.get("case_code").toString();
        RdsUpcUserModel user = (RdsUpcUserModel) request.getSession().getAttribute("user");
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("isyesresult", 1);
        variables.put("isback", -1);
        boolean result = rdsActivitiJudicialService.runByCaseCode(caseCode, variables, user);
        if (result) {
            return this.setModel(true, "操作成功");
        } else {
            return this.setModel(false, "操作失败");
        }
    }
}
