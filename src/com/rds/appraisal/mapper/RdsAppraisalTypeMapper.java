package com.rds.appraisal.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

@Component("RdsAppraisalTypeMapper")
public interface RdsAppraisalTypeMapper extends RdsAppraisalBaseMapper{
	/**
	 * 分页查询鉴定标准
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryStandardAllPage(Object params) throws Exception;
	
	/**
	 * 分页查询鉴定标准（总数）
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
	 * 更新鉴定标准
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateStandard(Object params) throws Exception; 
	
	/**
	 * 删除标准
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteStandard(Object params) throws Exception; 
	
}
