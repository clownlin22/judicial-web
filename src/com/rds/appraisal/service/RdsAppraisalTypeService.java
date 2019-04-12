package com.rds.appraisal.service;


/**
 * 鉴定类型接口
 * 
 * @author yxb
 *
 */
public interface RdsAppraisalTypeService extends RdsAppraisalBaseService {
	/**
	 * 分页查询鉴定标准
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryStandardAllPage(Object params) throws Exception;
	
	/**
	 * 鉴定标准总数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryStandardCount(Object params) throws Exception; 
	
	/**
	 * 鉴定标准插入
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertStandard(Object params) throws Exception; 
	
	/**
	 * 更新标准
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateStandard(Object params) throws Exception; 

	/**
	 * 删除标准
	 * @param params
	 * @return
	 */
	public int deleteStandard(Object params) throws Exception;
}
