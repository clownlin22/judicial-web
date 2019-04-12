package com.rds.finance.service;

import java.util.List;
import java.util.Map;

import com.rds.finance.model.RdsFinanceAscriptionInfo;
import com.rds.finance.model.RdsFinanceSpecialModel;


public interface RdsFinanceChargeStandardService {
	
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
	 * 获取收费标准
	 * @param typeid
	 * @param pernum
	 * @param areaid
	 * @param case_type
	 * @return
	 */
	public Map<String, Object> getStandFee(Integer typeid, Integer pernum,String areaid, Integer case_type);
	
	/**
	 * 查找所有
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryAll(Object params) throws Exception;
	
	/**
	 * 单挑数据查询
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryModel(Object params) throws Exception;
	
	/**
	 * 查询分页
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryAllPage(Object params) throws Exception;
	
	/**
	 * 查询总条数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryAllCount(Object params) throws Exception; 
	
	/**
	 * 更新
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int update(Object params) throws Exception;
	
	/**
	 * 批量更新
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updates(Object params) throws Exception;
	
	/**
	 * 新增
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insert(Object params) throws Exception;
	
	/**
	 * 新增
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertOld(Object params) throws Exception;
	
	/**
	 * 删除
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int delete(Object params) throws Exception;
	
	/**
	 * 查询是否已配置公式
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int queryExistCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 插入优惠码
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertApplicationCode(Map<String, Object> params) throws Exception;
	
	/**
	 * 激活优惠码
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object updateApplicationCode(Map<String, Object> params) throws Exception;
	
	/**
	 * 分页优惠码
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<RdsFinanceSpecialModel> queryAllSpecialFinance(Object params) throws Exception;
	
	/**
	 * 计数优惠码
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryCountSpecialFinance(Object params) throws Exception; 
	
	/**
	 * 标识激活码
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateCodeUsed(Map<String, Object> map) throws Exception;
	
	public boolean queryFinanceSpecialExist(Map<String, Object> map) throws Exception;
	
	public boolean deleteConfirm(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询无创财务设置分页
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryInvasiveAllPage(Object params) throws Exception;
	
	public int insertInvasiveStandard(Object params) throws Exception;
	
	public int updateInvasiveStandard(Object params) throws Exception;
	
	public int deleteInvasiveStandard(Object params) throws Exception;
	
	public int queryExistInversiveCount(Map<String, Object> map) throws Exception;

}
