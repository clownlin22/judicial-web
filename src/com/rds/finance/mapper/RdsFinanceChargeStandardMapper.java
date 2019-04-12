package com.rds.finance.mapper;

import java.util.List;
import java.util.Map;

import com.rds.finance.model.RdsFinanceAscriptionInfo;
import com.rds.finance.model.RdsFinanceChargeStandardModel;
import com.rds.finance.model.RdsFinanceSpecialModel;

public interface RdsFinanceChargeStandardMapper{
	
	public List<Object> queryAll(Object params) throws Exception;
	
	public Object queryModel(Object params) throws Exception;
	
	public List<Object> queryAllPage(Object params) throws Exception;
	
	public int queryAllCount(Object params) throws Exception; 
	
	/**
	 * 更新单个收费标准
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int update(Object params) throws Exception;
	
	/**
	 * 批量更新收费标准
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updates(Object params) throws Exception;
	
	/**
	 * 插入单条收费标准
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insert(Object params) throws Exception;
	

	/**
	 * 插入案例归属老系统案例用
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertOld(Object params) throws Exception;
	
	/**
	 * 创建棉麻
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertApplicationCode(Object params) throws Exception;
	
	/**
	 * 激活编码
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateApplicationCode(Map<String, Object> params) throws Exception;
	
	/**
	 * 分页查询激活码信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<RdsFinanceSpecialModel> queryAllSpecialFinance(Object params) throws Exception;
	
	/**
	 * 激活码分页计数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryCountSpecialFinance(Object params) throws Exception; 
	
	/**
	 * 逻辑删除收费标准
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int delete(Object params) throws Exception;
	/**
	 * 根据areacode查询地区
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryAreaInitials(Object params) throws Exception;
	
	/**
	 * 根据代理商id查询被代理人信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryMarketByAgent(Object params) throws Exception;
	
	/**
	 * 查询归属人信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<RdsFinanceAscriptionInfo> queryAscription(Object params) throws Exception;
	
	/**
	 * 获取收费公式
	 * @param map
	 * @return
	 */
	List<RdsFinanceChargeStandardModel> getEquation(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询该收费标准是否已配置
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int queryExistCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 标识激活码
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateCodeUsed(Map<String, Object> map) throws Exception;
	
	public int queryFinanceSpecialExist(Map<String, Object> map) throws Exception;
	
	public boolean deleteConfirm(Map<String, Object> map) throws Exception;
	
	public List<Object> queryInvasiveAllPage(Object params) throws Exception;
	
	public int queryInvasiveAllCount(Object params) throws Exception; 
	
	public int insertInvasiveStandard(Object params) throws Exception;
	
	public int updateInvasiveStandard(Object params) throws Exception;
	
	public int deleteInvasiveStandard(Object params) throws Exception;
	
	public int queryExistInversiveCount(Map<String, Object> map) throws Exception;
	/**
	 * 获取无创收费
	 * @param map
	 * @return
	 */
	List<RdsFinanceChargeStandardModel> getInversivePerFinance(Map<String, Object> map) throws Exception;
	
}
