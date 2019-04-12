package com.rds.children.mapper;

import java.util.List;
import java.util.Map;

import com.rds.children.model.RdsChildrenCaseInfoModel;
import com.rds.children.model.RdsChildrenCaseLocusModel;
import com.rds.children.model.RdsChildrenCaseResultModel;

public interface RdsChildrenResultMapper {

	List<RdsChildrenCaseResultModel> getResultInfo(Map<String, Object> params);

	int countResultInfo(Map<String, Object> params);

	void insertResult(RdsChildrenCaseResultModel resultModel);

	int exsitCase_code(String case_code);

	void deleteCaseResult(String case_id);
	
	String getCaseId(String case_code);

	void insertCaseLocus(List<RdsChildrenCaseLocusModel> locusList);

	RdsChildrenCaseInfoModel getCaseInfoModel(String case_code);

	List<RdsChildrenCaseLocusModel> getLoucsInfo(Map<String, Object> params);
	
	void deleteCaseLocus(String case_id);
	
	boolean updateCaseAgentia(Map<String, Object> params);
	
	List<String> queryOtherRecord(Map<String, Object> params);
	
	boolean insertHistoryRecord(Map<String, Object> params);
	
	List<Map<String,String>> queryIdentifySample(String sample_code);
	
	List<Map<String,String>> queryCaseCodeBySampleCode(String sample_code);
	
	List<Map<String,String>> queryChildrenRecordData(Map<String, Object> params);
	
	boolean deleteCaseHistory(String case_code);

}