package com.rds.finance.mapper;

import java.util.List;
import java.util.Map;

import com.rds.finance.model.RdsCaseFinanceAttachmentModel;
import com.rds.finance.model.RdsCaseFinanceModel;
import com.rds.finance.model.RdsContractPlanInfoModel;
import com.rds.finance.model.RdsFinanceContractAttachmentModel;
import com.rds.finance.model.RdsFinancePromptInfo;
import com.rds.finance.model.RdsRemittanceInfoModel;
import com.rds.finance.model.RdsRemittanceLogInfoModel;
import com.rds.finance.model.RdsRemittancePlanInfoModel;

public interface RdsCaseFinanceMapper {

	/**
	 * 列表查询财务信息
	 * 
	 * @param params
	 * @return
	 */
	public List<RdsCaseFinanceModel> getCaseFeeInfo(Map<String, Object> params);

	/**
	 * 财务信息计数
	 * 
	 * @param params
	 * @return
	 */
	public int countCaseFeeInfo(Map<String, Object> params);

	/**
	 * 更新财务信息
	 * 
	 * @param params
	 * @return
	 */
	public boolean updateCaseFee(Map<String, Object> params);

	/**
	 * 确认财务信息
	 * 
	 * @param params
	 * @return
	 */
	public boolean confirmCaseFee(Map<String, Object> params);

	/**
	 * 添加财务信息
	 * 
	 * @param params
	 * @return
	 */
	public boolean addCaseFee(Map<String, Object> params);

	/**
	 * 添加财务信息
	 * 
	 * @param params
	 * @return
	 */
	public boolean addCaseFeeOther(Map<String, Object> params);

	/**
	 * 查询新增财务信息
	 * 
	 * @return
	 */
	public List<RdsFinancePromptInfo> queryCasefeePrompt();

	/**
	 * 插入汇款单
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean insertRemittanceRecord(Map<String, Object> params)
			throws Exception;

	/**
	 * 更新汇款单
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean updateRemittanceRecord(Map<String, Object> params)
			throws Exception;

	/**
	 * 分页查询汇款单信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<RdsRemittanceInfoModel> queryPageRemittanceInfo(
			Map<String, Object> params) throws Exception;

	/**
	 * 查询汇款单总数
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int countRemittanceInfo(Map<String, Object> params) throws Exception;

	/**
	 * 更新案例财务汇款单id
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean updateCaseFeeRemittance(Map<String, Object> params)
			throws Exception;

	/**
	 * 根据汇款单Id确认案例财务信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean updateCaseFeeStatus(Map<String, Object> map)
			throws Exception;

	/**
	 * 插入合同计划
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean insertContractPlan(Map<String, Object> map) throws Exception;

	/**
	 * 更新合同计划
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean updateContractPlan(Map<String, Object> map) throws Exception;

	/**
	 * 分页查询合同计划信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<RdsContractPlanInfoModel> queryPageContractPlan(
			Map<String, Object> map) throws Exception;

	/**
	 * 计数合同计划
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int queryCountContractPlan(Map<String, Object> map) throws Exception;

	/**
	 * 查询合同计划汇款信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<RdsRemittancePlanInfoModel> queryAllContractPlan(
			Map<String, Object> map) throws Exception;

	/**
	 * 插入回款计划
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean insertRemittancePlan(Map<String, Object> map)
			throws Exception;

	/**
	 * 删除回款计划
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean deleteRemittancePlan(Map<String, Object> map)
			throws Exception;

	public boolean updateContractRemittance(Map<String, Object> map)
			throws Exception;

	public boolean updateContractSatus(Map<String, Object> map)
			throws Exception;

	public boolean insertRemittanceLogs(Map<String, Object> map)
			throws Exception;

	public boolean updateRemittance(Map<String, Object> map) throws Exception;

	public List<RdsRemittanceLogInfoModel> queryRemittanceLogs(
			Map<String, Object> map) throws Exception;

	/** 删除汇款单号 start **/
	/**
	 * 更新财务表信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCaseFeeRemittance(Map<String, Object> map)
			throws Exception;

	/**
	 * 删除汇款单表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean deleteRemittanceInfo(Map<String, Object> map)
			throws Exception;

	/** 删除汇款单号 end **/

	public int queryConfirmCase(Map<String, Object> map) throws Exception;

	public Map<String, Object> queryCaseFeeById(Map<String, Object> map)
			throws Exception;

	public boolean updateConfirmCodeByCaseId(Map<String, Object> map)
			throws Exception;

	/**
	 * 更新财务信息
	 * 
	 * @param params
	 * @return
	 */
	public boolean updateCaseFeeUnite(Map<String, Object> params);

	public boolean deleteInvasivePre(Map<String, Object> params);

	/** 合同附件管理 */
	public boolean insertContractAttachment(
			RdsFinanceContractAttachmentModel attachmentModel);

	public boolean deleteContractAttachment(Map<String, Object> params);

	public List<RdsFinanceContractAttachmentModel> queryContractAttachment(
			Map<String, Object> params);

	/** 合同附件管理 */

	public boolean updateRemittancePlan(Map<String, Object> params);

	public boolean deductibleCaseInfo(Map<String, Object> params);

	public boolean updateRemittancePhoto(Map<String, Object> map)
			throws Exception;

	public List<RdsCaseFinanceAttachmentModel> queryFinanceAttachment(
			Map<String, Object> params) throws Exception;;

	public boolean insertFinanceAttachment(Map<String, Object> map)
			throws Exception;
	
	public boolean updateFinanceAttachment(Map<String, Object> map)
			throws Exception;

}
