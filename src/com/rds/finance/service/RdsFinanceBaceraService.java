package com.rds.finance.service;



public interface RdsFinanceBaceraService {
	
	
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
	public boolean update(Object params) throws Exception;
	
	
	/**
	 * 新增
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean insert(Object params) throws Exception;
	
	
	/**
	 * 删除
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean delete(Object params) throws Exception;
	
	/**
	 * 确认
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public boolean confirmCase(Object params) throws Exception;

}
