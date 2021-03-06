package com.rds.bacera.service;

import java.util.Map;

public interface RdsBaceraBaseService {
	
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
	 * 新增
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insert(Object params) throws Exception;
	
	/**
	 * 删除
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int delete(Object params) throws Exception;

	/**
	 * 跟新子节点信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateJunior(Object params) throws Exception;

}
