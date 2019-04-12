package com.rds.upc.service.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.upc.mapper.RdsUpcRoleMapper;
import com.rds.upc.service.RdsUpcRoleService;

@Service("rdsUpcRoleService")
public class RdsUpcRoleServiceImpl extends RdsUpcAbstractServiceImpl implements RdsUpcRoleService {
	@Setter
	@Autowired
	private RdsUpcRoleMapper rdsUpcRoleMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsUpcRoleMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	//分页查询角色
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsUpcRoleMapper.queryAllCount(params));
		result.put("data", rdsUpcRoleMapper.queryAllPage(params));
		return result;
	}

	@Override
	//角色总数
	public int queryAllCount(Object params) throws Exception {
		return rdsUpcRoleMapper.queryAllCount(params);
	}

	@Override
	//更新角色
	public int update(Object params) throws Exception {
		try {
			return rdsUpcRoleMapper.update(params);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
	}

	@Override
	//插入角色
	public int insert(Object params) throws Exception {
		try {
			return rdsUpcRoleMapper.insert(params);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
	}

	@Override
	//删除角色
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcRoleMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	//新增角色权限
	public int insertPermit(Map<String, Object> params)
	{
		return rdsUpcRoleMapper.insertPermit(params);
	}

	@Override
	//删除角色权限
	public int deletePermit(Map<String, Object> params)
	{
		return rdsUpcRoleMapper.deletePermit(params);
	}

}
