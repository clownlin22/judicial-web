package com.rds.children.mapper;

import java.util.List;
import java.util.Map;

import com.rds.children.model.RdsChildrenPrintCaseModel;

public interface RdsChildrenPrintMapper {

	List<RdsChildrenPrintCaseModel> getCaseInfo(Map<String, Object> params);

	int countCaseInfo(Map<String, Object> params);

	RdsChildrenPrintCaseModel printCaseInfo(String case_id);

	List<Map<String, String>> printCaseResult(String case_id);

	String getCasePhoto(Map<String, Object> params);

	int changePrintState(Map<String, Object> params);

}
