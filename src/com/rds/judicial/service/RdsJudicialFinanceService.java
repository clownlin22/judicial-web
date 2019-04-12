package com.rds.judicial.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialResponse;

public interface RdsJudicialFinanceService {

	void generateFinanceDaily();
	
	void inversiveFinanceDaily();
	
	void childrenFinanceDaily();

	void generateFinanceMonthly();

	Map<String, Object> queryVerify(Map<String, Object> map);

	Map<String, Object> updatestatus(Map<String, Object> map);

	Map<String, Object> queryAllSum(Map<String, Object> map);

	Map<String, Object> queryDetail(Map<String, Object> map);

	Map<String, Object> getAllMonthly(Map<String, Object> params);

	Map<String, Object> saveMonthlyVerfiy(Map<String, Object> params);

	Map<String, Object> saveReturnSum(Map<String, Object> params);

	RdsJudicialResponse getFinanceDaily(Map<String, Object> params);

	boolean confirmFinanceDaily(Map<String, Object> params);

	Map<String, Object> queryDailyDetail(Map<String, Object> params);
	/** 汇款生成 **/
	int createFinanceDaily(String case_id);
	int createPreFinanceDaily(String case_id);
	int createChildrenFinanceDaily(String case_id);
	int createContractFinanceDaily(String contract_id);
	/** 汇款生成 **/

    void exportCaseFinance(Map<String,Object> params,HttpServletResponse response)  throws Exception;
}
