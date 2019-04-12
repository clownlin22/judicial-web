package com.rds.appraisal.service;

import java.util.List;

/**
 * 委托人接口
 * 
 * @author yxb
 *
 */
public interface RdsAppraisalEntrustService extends RdsAppraisalBaseService {
	/**
	 * 查找所有
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryAllMechanism(Object params) throws Exception;

	/**
	 * 单条数据查询
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryModelMechanism(Object params) throws Exception;

	/**
	 * 查询分页
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryAllPageMechanism(Object params) throws Exception;

	/**
	 * 查询总条数
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryAllCountMechanism(Object params) throws Exception;

	/**
	 * 更新
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateMechanism(Object params) throws Exception;

	/**
	 * 新增
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertJudge(Object params) throws Exception;

	/**
	 * 删除
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteMechanism(Object params) throws Exception;
	
	public List<String> queryJudge(Object params) throws Exception;
	
	public int deleteJudge(Object params) throws Exception;


}
