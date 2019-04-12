package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.rds.judicial.model.RdsJudicialConfirmReturnModel;
import com.rds.judicial.model.RdsJudicialFinanceDailyModel;
import com.rds.judicial.model.RdsJudicialFinanceMonthlyModel;
import com.rds.judicial.model.RdsJudicialMailInfoModel;
import com.rds.judicial.model.RdsJudicialPhoneRequestModel;
import com.rds.judicial.model.RdsJudicialRimitModel;

public interface RdsJudicialPhoneRegisterService {

	public Map<String, Object> queryBank();

	public Map<String, Object> queryHistory(Map<String, Object> params)
			throws Exception;

	public Map<String, Object> register(String username, String userid,
			String case_code, double charge, String remark, int filenum,
			MultipartFile[] file, String filetype) throws Exception;

	public Map<String, Object> reregister(MultipartFile[] file,
			RdsJudicialPhoneRequestModel phoneReq) throws Exception;

	public Map<String, Object> isCaseCodeExist(String case_code);

	public Map<String, Object> repassword(String usercode, String oldpass,
			String newpass) throws Exception;

	// public Map<String, Object> updateFeePerCase(String case_code, int
	// case_fee);

	public Map<String, Object> getManager(String userid);

	public Map<String, Object> getArea(String userid);

	/**
	 * 
	 * @param typeid
	 *            单双亲类型
	 * @param pernum
	 *            样本数量
	 * @param areaid
	 *            归属人id
	 * @param case_type
	 *            案例类型：司法、医学
	 * @return
	 */
	public Map<String, Object> getStandFee(Integer typeid, Integer pernum,
			String areaid, Integer case_type);

	public Map<String, Object> register(MultipartFile[] file,
			RdsJudicialPhoneRequestModel phoneReq) throws Exception;

	public Map<String, Object> getDaily(String userid);

	public Map<String, Object> getMonthly(String userid);

	public Map<String, Object> getAllManager();

	public Map<String, Object> confirmReturn(
			RdsJudicialConfirmReturnModel returnModel);

	public Map<String, Object> rimit(RdsJudicialRimitModel rimit);

	public Map<String, Object> confirmDaily(
			RdsJudicialFinanceDailyModel dailyModel);

	public Map<String, Object> confirmMonthly(
			RdsJudicialFinanceMonthlyModel monthlyModel);

	public Map<String, Object> getCaseStatus(String case_id);

	public Map<String, Object> getDailyDetail(String id);

	public Map<String, Object> getMonthlyDetail(String id);

	public Map<String, Object> getEquation(String areaid, String case_type);

	public Map<String, Object> getUnConfirm(String userid);
	
	public Map<String, Object> getCaseStatesList(Map<String, Object> params);

	public Map<String, Object> getCaseStateById(Map<String, String> params);
	
	
	//查询案例流程
	public String  getProcessInstanceId (String id_number);
	List<RdsJudicialMailInfoModel> getMail (String id_number);
}
