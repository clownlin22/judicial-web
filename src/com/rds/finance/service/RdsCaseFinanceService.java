package com.rds.finance.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.rds.finance.model.RdsCaseFinanceAttachmentModel;
import com.rds.finance.model.RdsFinanceContractAttachmentModel;
import com.rds.finance.model.RdsFinancePromptInfo;
import com.rds.finance.model.RdsRemittanceLogInfoModel;
import com.rds.finance.model.RdsRemittancePlanInfoModel;
import com.rds.judicial.model.RdsJudicialResponse;

public interface RdsCaseFinanceService {

	RdsJudicialResponse getCaseFeeInfo(Map<String, Object> params);

	boolean updateCaseFee(Map<String, Object> params);

	boolean confirmCaseFee(Map<String, Object> params);
	
	public boolean addCaseFee(Map<String, Object> params);
	
	public List<RdsFinancePromptInfo> queryCasefeePrompt();
	
	/**
	 * 插入汇款单
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean insertRemittanceRecord(Map<String, Object> params) throws Exception;
	
	/**
	 * 更新汇款单
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean updateRemittanceRecord(Map<String, Object> params) throws Exception;
	
	/**
	 * 分页查询汇款单信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public RdsJudicialResponse queryPageRemittanceInfo(Map<String, Object> params) throws Exception;
	
	/**
	 * 查询汇款单总数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int countRemittanceInfo(Map<String, Object> params) throws Exception;
	
	/**
	 * 插入合同计划
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean insertContractPlan(Map<String, Object> map) throws Exception;
	
	/**
	 * 插入合同计划
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean insertContractRemittance(Map<String, Object> map) throws Exception;
	
	public boolean deleteRemittancePlan(Map<String, Object> map) throws Exception;
	
	/**
	 * 更新合同计划
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean updateContractPlan(Map<String, Object> map) throws Exception;
	
	public boolean updateRemittancePlan(Map<String, Object> map) throws Exception;
	
	/**
	 * 分页查询合同计划信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public RdsJudicialResponse queryPageContractPlan(Map<String, Object> map) throws Exception;
	
	/**
	 * 计数合同计划
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int queryCountContractPlan(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询合同计划汇款信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<RdsRemittancePlanInfoModel> queryAllContractPlan(Map<String, Object> map) throws Exception;
	
	public boolean updateRemittance(Map<String, Object> map) throws Exception;
	
	public List<RdsRemittanceLogInfoModel> queryRemittanceLogs(Map<String, Object> map) throws Exception;

	public boolean deleteCaseFeeRemittance(Map<String, Object> map) throws Exception;
	
	public int queryConfirmCase(Map<String, Object> map) throws Exception;
	
	public Map<String,Object> confirmCodeAdd(Map<String, Object> map) throws Exception;
	
	public Map<String,Object> queryCaseConfirm(Map<String, Object> map) throws Exception;
	
	public Map<String,Object> addCaseFeeOther(Map<String, Object> params) throws Exception;
	
	public Map<String,Object> addCaseFeeUnite(Map<String, Object> params);
	
	public Map<String,Object> updateCaseFeeUnite(Map<String, Object> params);
	
	/**
	 * 增加儿童基因库财务信息
	 * @param params
	 * @return
	 */
	public Map<String,Object> addCaseFeeChildren(Map<String, Object> params);

	/**
	 * 修改儿童基因库财务
	 * @param params
	 * @return
	 */
	public Map<String,Object> updateCaseFeeChildren(Map<String, Object> params);
	
	/**
	 * 手动修改财务信息
	 * @param params
	 * @return
	 */
	public Map<String,Object> updateCaseFeeByRegister(Map<String, Object> params);
	
	Map<String, Object> upload(String contract_id, String contract_num,MultipartFile[] files,int[] filetype,String userid)
			throws Exception;

	boolean deleteFile(Map<String, Object> params);
	
	List<RdsFinanceContractAttachmentModel> queryContractAttachment(Map<String, Object> params);

	void downloadAttachment(HttpServletResponse response, String id);
	
    void exportCaseFinance(Map<String,Object> params,HttpServletResponse response)  throws Exception;
    
    boolean deductibleCaseInfo(Map<String,Object> params)  throws Exception;
    
	void exportCaseInfoOther(Map<String, Object> params,HttpServletResponse response) throws Exception;

	void getImg(HttpServletResponse response, String filename);

	Map<String, Object> turnImg(Map<String, Object> params);
	
	public boolean updateRemittancePhoto(Map<String, Object> params);
	
	public List<RdsCaseFinanceAttachmentModel> queryFinanceAttachment(
			Map<String, Object> params) throws Exception;;

	public boolean insertFinanceAttachment(Map<String, Object> map)
			throws Exception;
	
	public boolean updateFinanceAttachment(Map<String, Object> map)
			throws Exception;
}
