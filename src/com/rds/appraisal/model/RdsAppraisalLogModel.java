package com.rds.appraisal.model;

import lombok.Data;

/**
 * 操作日志实例
 * 
 * @author yxb 2015-08-10
 */
@Data
public class RdsAppraisalLogModel {
	// id
	private String id;
	// 案例id
	private String case_id;
	//当前状态
	private String nowstatus;
	//上一状态
	private String prestatus;
	//发起人
	private String fquser;
	//接收人
	private String jsuser;
	
}
