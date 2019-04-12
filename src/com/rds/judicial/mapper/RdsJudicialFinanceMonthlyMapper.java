package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialFinanceDailyModel;

public interface RdsJudicialFinanceMonthlyMapper {

	public List<RdsJudicialFinanceDailyModel> getMonthly(String userid);

	public List<Map<String, Object>> getAllMonthly(
			Map<String, Object> params);

	public int getAllMonthlyCount(Map<String, Object> params);

	public int updateStatus(Map<String, Object> params);

	public int confirmStatus(String id);
	
	public int insertMonthly(RdsJudicialFinanceDailyModel dailyModel);
	
	public List<Map<String, Object>> getCase4Monthly(String userid);
	
	public List<String> getManagerListMonthly();

}
