package com.rds.bacera.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rds.bacera.model.RdsBaceraFeeModel;
@SuppressWarnings("rawtypes")
@Component("RdsBaceraBoneAgeMapper")
public interface RdsBaceraBoneAgeMapper extends RdsBaceraBaseMapper {
	public int queryNumExit(Map map);
	public int queryFinanceExit(Map map);
	public int queryExpressExit(Map map);
//	public List<Object> queryFinancePage(Object params);
//	public int queryFinanceCount(Object params) throws Exception; 
	public int insertFinance(Object params) throws Exception;
	public int updateFinance(Object params) throws Exception;
	public int insertExpress(Object params) throws Exception;
	public int updateExpress(Object params) throws Exception;
	public int deleteFinance(Object params) throws Exception;
	public int deleteExpress(Object params) throws Exception;
	public List<Object> queryAgentByRece(Object params) throws Exception;
	public int insertOAnum(Object params) throws Exception;
	//确认案例
	public boolean caseFeeConfirm(Map<String, Object> params);
	
	/**
	 * 分页查询骨龄应收金额
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryBoneFee(Object params) throws Exception;
	
	/**
	 * 查询骨龄应收金额总数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryBoneFeeCount(Object params) throws Exception;
	
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
	
	/**
	 * 删除骨龄财务配置
	 */
	public int deleteBoneFee(Object params) throws Exception;
	
	/**
	 * 根据area_id查询配置金额
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryBoneFeeByRec(Object params) throws Exception;
	
	/**
	 * 批量插入财务信息
	 * @param list
	 * @return
	 */
	public boolean insertFinanceList(List<RdsBaceraFeeModel> list);
	
	/**
	 * 批量修改财务信息
	 * @param params
	 * @return
	 */
	public boolean updateFinanceList(Map<String, Object> params);
	
	/**
	 * 财务确认
	 * @param params
	 * @return
	 */
	public boolean confirmFinanceList(Map<String, Object> params);
}
