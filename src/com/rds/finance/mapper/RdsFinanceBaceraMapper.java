package com.rds.finance.mapper;

import java.util.List;

public interface RdsFinanceBaceraMapper{
	
	public List<Object> queryAllPage(Object params) throws Exception;
	
	public int queryAllCount(Object params) throws Exception; 
	
	public boolean update(Object params) throws Exception;
	
	public boolean insert(Object params) throws Exception;
	
	public boolean delete(Object params) throws Exception;
	
	public boolean confirmCase(Object params) throws Exception;
	
}
