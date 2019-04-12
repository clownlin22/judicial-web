package com.rds.activiti.service;

import com.rds.upc.model.RdsUpcUserModel;

import java.util.Map;

/**
 * @author XiangKang on 2017/1/17.
 */
public interface RdsActivitiJudicialService {
	/* 根据案例编号来执行流程 */
	boolean runByCaseCode(String case_code, Map<String, Object> variables,
			RdsUpcUserModel user);

	boolean runByCaseCode(String case_code, String currentTaskDefKey,
			Map<String, Object> variables, RdsUpcUserModel user);

	boolean addComment(String case_code, Map<String, Object> variables,
			RdsUpcUserModel user);

	boolean runByCaseNum(String id, Map<String, Object> variables,
			RdsUpcUserModel user);

	boolean runByChildCaseCode(String case_code, Map<String, Object> variables,
			RdsUpcUserModel user);

	public boolean runByChildCaseCode(String case_code,
			String currentTaskDefKey, Map<String, Object> variables,
			RdsUpcUserModel user);
}
