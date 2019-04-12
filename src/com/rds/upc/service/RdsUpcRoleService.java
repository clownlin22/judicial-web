package com.rds.upc.service;

import java.util.Map;

public interface RdsUpcRoleService extends RdsUpcBaseService {
	
	//插入角色权限
	public int insertPermit(Map<String, Object> params);
	
	//删除角色权限
	public int deletePermit(Map<String, Object> params);
}
