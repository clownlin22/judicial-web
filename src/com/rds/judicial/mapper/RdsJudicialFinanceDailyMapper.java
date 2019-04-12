package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialDailyDetailModel;
import com.rds.judicial.model.RdsJudicialFinanceDailyModel;
import com.rds.judicial.model.RdsJudicialFinanceReturnModel;

public interface RdsJudicialFinanceDailyMapper {
	public int insertDaily(RdsJudicialFinanceDailyModel dailyModel);
	
	public List<String> getManagerList();
	
	public List<String> getInversiveManagerList();
	
	public List<String> getChildrenManagerList();
	
	public List<Map<String , Object>> getChildrenCase4Daily(String userid);
	
	public List<Map<String , Object>> getCase4Daily(String userid);
	
	public List<Map<String , Object>> getInversiveCase4Daily(String userid);
	
	public List<Map<String , Object>> getCase4DailyById(String userid);
	//无创产前
	public List<Map<String , Object>> getPreCase4DailyById(String userid);

	//儿童基因库
	public List<Map<String , Object>> getChildrenCase4DailyById(String userid);

	public int updateFeeStatus(Map<String, Object> params);

	public List<RdsJudicialFinanceReturnModel> getAllSum(Map<String, Object> params);

	public List<Map<String, Object>> getDetailByUser(Map<String, Object> map);

	public List<RdsJudicialFinanceDailyModel> getDaily(String userid);

	public int updateFee(Map<String, Object> params);

	public List<RdsJudicialFinanceDailyModel> getUnPay(
			Map<String, Object> params);

	public int updateStatus(Map<String, Object> params);

//	public void updateCaseGatherid(Map<String, Object> params);

	public Double getBalance(Map<String, Object> params);

	public List<RdsJudicialFinanceDailyModel> getFinanceDaily(
			Map<String, Object> params);

	public int countFinanceDaily(Map<String, Object> params);

	public int confirmFinanceDaily(Map<String, Object> params);

	public List<RdsJudicialDailyDetailModel> queryDailyDetail(
			Map<String, Object> params);

	public int queryDailyDetailCount(Map<String, Object> params);
	
	public List<String> queryContractUser();
	
	public List<Map<String , Object>> queryContractDaily(String contract_userid);
	
	public List<Map<String , Object>> queryContractDailyById(String contract_id);
	
	public int updateContractStatus(Map<String, Object> params);

	public boolean updateUserState() throws Exception;
	
	
	
}
