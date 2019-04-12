package com.rds.judicial.mapper;

import com.rds.judicial.model.*;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/22
 */
public interface RdsJudicialSubCaseMapper extends
		RdsJudicialExperimentBaseMapper {
	List<String> querySubCase(Map<String, Object> params);

	List<String> querySubCaseCode(String case_code);

	RdsJudicialSubCaseInfoModel querySubCaseRecord(String sub_case_code);

	String queryMainCaseCode(String sub_case_code);

	int deleteSubCaseInfo(Map<String, Object> params);

	int queryCountForGen(String case_code);

	int verifySampleCode(Map<String, Object> params);

	List<Map<String,Object>> queryCaseIdBySubCaseCode1(Map<String, Object> params);
	
	String queryCaseIdBySubCaseCode(String sub_case_code);

	void insertCheckNegReport(Map<String, Object> params);

	int updateCheckNegReport(String case_code);

	int updateCaseinfofinalbycode(String case_code);

	int updatePi1Formz(Map<String, Object> paramMap);

	int updatePi1ForUnPaent(Map<String, Object> paramMap);

	int updatePi2Formz(Map<String, Object> paramMap);

	int deleteData(String case_code);

	RdsJudicialCaseInfoModel queryCaseInfoByCaseCode(String case_code);

	List<Map<String, Object>> queryALLcaseForWord(Map<String, Object> params);

	List<Map<String, Object>> querySampleType(String paramString);

	int updateCaseReagentName(Map<String, Object> params);

	int updateCaseReagentNameExt(Map<String, Object> params);

	String queryReagentNameByCaseId(String case_id);

	String queryReagentNameEXTByCaseId(String case_id);

	RdsJudicialCompareResultModel querySubCaseCompareResult(String sub_case_code);

	List<String> querySubCaseForWord(Map<String, Object> params);

	List<RdsJudicialSubCaseResultModel> queryAllForZhengTaiExt(
			Map<String, Object> params);

	int queryAllCountForZhengTaiExt(Map<String, Object> params);

	List<Map<String, Object>> querySampleResult(String paramString);

	String querysamplecallBySamplecode(String sample_code);

	String querysampleunmathcodeBySamplecode(Map<String, Object> params);

	List<Map<String, Object>> querySampleResultAlierts(
			Map<String, Object> params);

	List<Map<String, Object>> querySampleResultAliertsExt(
			Map<String, Object> params);

	List<Map<String, Object>> querySampleResultpiinfoAliertsExt(
			Map<String, Object> params);

	int updateCaseLaboratoryNo(Map<String, Object> params);
	List<Map<String, String>> querySampleCodes(Map<String, Object> params);
	int deleteSamples(Map<String, Object> mapCheck1);
	int deleteSampless(Map<String, Object> mapCheck1);
	int allDelete(Map<String, Object> mapCheck);
	int allDelete1(Map<String, Object> mapCheck);
	int allDelete2(Map<String, Object> mapCheck);
	int allDelete3(Map<String, Object> mapCheck);
	int allDelete4(Map<String, Object> mapCheck);
	int updatesubcase(String case_code);
	int deleteOne(String case_code);
	int deleteTwo(String case_code);
}
