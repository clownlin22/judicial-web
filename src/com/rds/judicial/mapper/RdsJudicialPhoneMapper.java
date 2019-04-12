package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialCaseFeeModel;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialCaseStatusModel;
import com.rds.judicial.model.RdsJudicialConfirmReturnModel;
import com.rds.judicial.model.RdsJudicialDailyDetailModel;
import com.rds.judicial.model.RdsJudicialMailInfoModel;
import com.rds.judicial.model.RdsJudicialPhoneCaseListModel;

public interface RdsJudicialPhoneMapper {

	List<Map<String, Object>> getArea(String userid);

	List<Map<String, Object>> getAllManager();

	List<Map<String, Object>> getManager(String userid);
	
	List<RdsJudicialPhoneCaseListModel> queryByCaseinper(Map<String, Object> params);
	
	int queryByCaseinperCount(Map<String, Object> params);
	
	int firstInsertCase(RdsJudicialCaseInfoModel caseInfoModel);
	
//	int updateGatherID(Map<String, Object> map);
	
	String getCaseID(String case_code);
	
	List<String> queryUnPay(Map<String,Object> params);
	
	List<RdsJudicialCaseFeeModel> getIsNullCase(String isnullcase_code);

	List<RdsJudicialCaseStatusModel> getStatus(String case_id);

	List<RdsJudicialDailyDetailModel> getDailyDetail(String id);

	List<RdsJudicialDailyDetailModel> getMonthlyDetail(String id);
	
	int exsitCaseCode(Map<String, Object> params);

	List<RdsJudicialConfirmReturnModel> getUnConfirm(String userid);
	
	List<RdsJudicialCaseStatusModel> getCaseStatue(Map<String, String> params);

	List<RdsJudicialCaseStatusModel> getCaseStatueByIdnumber(
			Map<String, String> params);
	//查询流程
	String getProcessInstanceId(String id_number);
	List<RdsJudicialMailInfoModel> getMail (String id_number);
}
