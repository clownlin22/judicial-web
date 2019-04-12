package com.rds.children.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.children.model.RdsChildrenPrintCaseModel;
import com.rds.children.model.RdsChildrenResponse;

public interface RdsChildrenPrintService {

	RdsChildrenResponse getCaseInfo(Map<String, Object> params);

	RdsChildrenPrintCaseModel printCaseInfo(String case_id);

	List<Map<String, String>> printCaseResult(String case_id);

	String getCasePhoto(String case_id);

	void getImg(HttpServletResponse response, String filename);

	boolean changePrintState(Map<String, Object> params);

	Map<String,Object> childrenCardCreate(Map<String, Object> params);
}
