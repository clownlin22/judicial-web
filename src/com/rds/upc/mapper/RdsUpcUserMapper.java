package com.rds.upc.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("rdsUpcUserMapper")
public interface RdsUpcUserMapper extends RdsUpcBaseMapper{ 
	
	public List<Object> queryUserType();
	
	public Object queryForLogin(Map<String, Object> params)  throws Exception;
	
	public int updateRole(Map<String, Object> params);

	public List<Object> queryRoleType();
	
	public int updatePass(Map<String, Object> params);
	
	public int insertToken(Map<String, Object> params);

	public List<Object> queryToken(Map<String, Object> params);
	
	public int saveUserReport(Map<String, Object> params);

	public boolean delCompanyPrint(Map<String, Object> params);

	List<Map<String, Object>> queryDeptList(Map<String, Object> params);
	
	public boolean updateDept(Map<String, Object> params);
	
}
