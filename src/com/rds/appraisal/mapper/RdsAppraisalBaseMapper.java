package com.rds.appraisal.mapper;

import java.util.List;

/**
 * 操作工具基础接口
 * @author yxb
 * @date 2015-07-22
 */
public interface RdsAppraisalBaseMapper {
	/**
	 * 查询所有记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryAll(Object params) throws Exception;
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryModel(Object params) throws Exception;
	
	/**
	 * 分页查询记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryAllPage(Object params) throws Exception;
	
	/**
	 * 根据条件查询所有记录数量
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryAllCount(Object params) throws Exception; 
	
	/**
	 * 根据条件更新记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int update(Object params) throws Exception;
	
	/**
	 * 插入新记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insert(Object params) throws Exception;
	
	/**
	 * 逻辑删除记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int delete(Object params) throws Exception;

}
