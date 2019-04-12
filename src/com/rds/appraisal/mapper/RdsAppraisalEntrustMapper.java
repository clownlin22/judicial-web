package com.rds.appraisal.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

@Component("RdsAppraisalEntrustMapper")
public interface RdsAppraisalEntrustMapper extends RdsAppraisalBaseMapper{
	/**
	 * 查询所有记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryAllMechanism(Object params) throws Exception;
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryModelMechanism(Object params) throws Exception;
	
	/**
	 * 分页查询记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryAllPageMechanism(Object params) throws Exception;
	
	/**
	 * 根据条件查询所有记录数量
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryAllCountMechanism(Object params) throws Exception; 
	
	/**
	 * 根据条件更新记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateMechanism(Object params) throws Exception;
	
	/**
	 * 插入新记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertJudge(Object params) throws Exception;
	
	/**
	 * 逻辑删除记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteMechanism(Object params) throws Exception;
	
	public int deleteJudge(Object params) throws Exception;
	
	public List<String> queryJudge(Object params) throws Exception;

}
