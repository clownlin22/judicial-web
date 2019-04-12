package com.rds.activiti.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author XiangKang on 2017/1/20.
 */
public class RdsActivitiTaskDetailModel {
	SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private String id;
    private String name;
    private String taskDefinitionKey;
    private String assignee;
    private Date claimTime;
    private Date startTime;
    private Date endTime;
    private Long durationInMillis;
    private String comment;
    private String mail;//接口邮寄信息使用

    public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getClaimTime() {
        return claimTime;
    }

    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }

    public Date getStartTime() {
        return startTime;
    }
    public String getStartTime1() {
        return dateFormater.format(startTime);
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getDurationInMillis() {
        return durationInMillis;
    }

    public void setDurationInMillis(Long durationInMillis) {
        this.durationInMillis = durationInMillis;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
