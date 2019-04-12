package com.rds.upc.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("rdsUpcRoleMapper")
public interface RdsUpcRoleMapper extends RdsUpcBaseMapper {
	
	/**
	 * 插入角色权限
	 * @param params
	 * @return
	 */
	public int insertPermit(Map<String, Object> params);
	
	/**
	 * 删除角色权限
	 * @param params
	 * @return
	 */
	public int deletePermit(Map<String, Object> params);
}
