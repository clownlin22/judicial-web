package com.rds.upc.service;

public interface RdsUpcDepartmentService extends RdsUpcBaseService {
	
	public Object queryTreeCombo(Object params) throws Exception;
	
	public int queryCountByCode(Object params) throws Exception;
	
	public Object queryDepartmentList(Object params) throws Exception;

}
