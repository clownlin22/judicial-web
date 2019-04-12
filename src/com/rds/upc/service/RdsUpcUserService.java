package com.rds.upc.service;

import java.util.List;
import java.util.Map;


public interface RdsUpcUserService extends RdsUpcBaseService{
	
	public Object queryUserType();
	
	public Object queryForLogin(Map<String, Object> params) throws Exception;

	public int updateRole(Map<String, Object> params);
	
	public Object queryRoleType();
	
	public int updatePass(Map<String, Object> params);
	
	public int insertToken(Map<String, Object> params);
	
	public  List<Object> queryToken(Map<String, Object> params);
	
	public int saveUserReport(Map<String, Object> params);
	
	Map<String, Object> queryDeptList(Map<String, Object> params);
	
	public boolean updateDept(Map<String, Object> params);

}
