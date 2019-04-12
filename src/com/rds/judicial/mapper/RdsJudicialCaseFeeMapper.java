package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialCaseFeeModel;
import com.rds.judicial.model.RdsJudicialConfirmReturnModel;
import com.rds.judicial.model.RdsJudicialFinanceVerifyModel;

public interface RdsJudicialCaseFeeMapper {

	public List<RdsJudicialFinanceVerifyModel> queryVerify(Map<String, Object> map);

	public int queryVerifyCount();

	public int updatestatus(Map<String, Object> map);
	
	public int insertCaseFee(RdsJudicialCaseFeeModel casefee);

	public int updateCaseFee(RdsJudicialConfirmReturnModel returnModel);

	public int update(RdsJudicialCaseFeeModel rdsJudicialCaseFeeModel);

	public int isMonthlyCase(RdsJudicialConfirmReturnModel returnModel);

	public List<RdsJudicialCaseFeeModel> getCaseFeeInfo(
			Map<String, Object> params);

	public int countCaseFeeInfo(Map<String, Object> params);

	public boolean confirmCaseFee(Map<String, Object> params);

	public boolean insertOAnum(Map<String, Object> params);

	public boolean caseFeeConfirm(Map<String, Object> params);

}
