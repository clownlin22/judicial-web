package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.bacera.model.RdsBaceraBoneAgeFeeModel;

/**
 * 骨龄-service
 * @author Administrator
 *
 */
@SuppressWarnings("rawtypes")
public interface RdsBaceraBoneAgeService extends RdsBaceraBaseService {
	public int queryNumExit(Map map);
//	public Object queryFinancePage(Object params);
//	public int queryFinanceCount(Object params) throws Exception; 
	public int insertFinance(Object params) throws Exception;
	public int updateFinance(Object params) throws Exception;
	public int queryFinanceExit(Map map);
	public int queryExpressExit(Map map);
	public int insertExpress(Object params) throws Exception;
	public int updateExpress(Object params) throws Exception;
	public Object queryAgentByRece(Object params) throws Exception;
	public int insertOAnum(Object params) throws Exception;
	void exportBoneInfo(Map<String, Object> params,HttpServletResponse response) throws Exception;
	//确认案例
	boolean caseFeeConfirm(Map<String, Object> params);
	/**
	 * 骨龄财务公式分页
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryBoneFee(Object params) throws Exception;
	
	/**
	 * 保存骨龄财务信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveBoneFee(Object params) throws Exception;
	
	/**
	 * 更新骨龄财务信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateBoneFee(Object params) throws Exception;
	
	public int queryBoneFeeCount(Object params) throws Exception;
	
	public int deleteBoneFee(Object params) throws Exception;
	
	public RdsBaceraBoneAgeFeeModel queryBoneFeeByRec(Object params) throws Exception;
	
	public boolean insertFinanceList(Map<String, Object> params) throws Exception;
	
	public boolean updateFinanceList(Map<String, Object> params) throws Exception;
	
	public boolean confirmFinanceList(Map<String, Object> params) throws Exception;
}
