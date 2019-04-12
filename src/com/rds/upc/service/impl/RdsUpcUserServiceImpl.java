package com.rds.upc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.upc.mapper.RdsUpcUserMapper;
import com.rds.upc.service.RdsUpcUserService;

@Service("rdsUpcUserService")
public class RdsUpcUserServiceImpl implements RdsUpcUserService {

	@Setter
	@Autowired
	private RdsUpcUserMapper rdsUpcUserMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsUpcUserMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return rdsUpcUserMapper.queryModel(params);
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("total", rdsUpcUserMapper.queryAllCount(params));
		result.put("data", rdsUpcUserMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsUpcUserMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		return rdsUpcUserMapper.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		return rdsUpcUserMapper.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsUpcUserMapper.delete(params);
	}

	@Override
	public Object queryUserType() {
		return rdsUpcUserMapper.queryUserType();
	}

	@Override
	public Object queryForLogin(Map<String, Object> params) throws Exception {
		return rdsUpcUserMapper.queryForLogin(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateRole(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return rdsUpcUserMapper.updateRole(params);
	}

	@Override
	public Object queryRoleType() {
		return rdsUpcUserMapper.queryRoleType();
	}

	@Override
	public int updatePass(Map<String, Object> params) {
		return rdsUpcUserMapper.updatePass(params);
	}

	@Override
	public int insertToken(Map<String, Object> params) {
		return rdsUpcUserMapper.insertToken(params);
	}

	@Override
	public List<Object> queryToken(Map<String, Object> params) {
		return rdsUpcUserMapper.queryToken(params);
	}

	@Override
	public int saveUserReport(Map<String, Object> params) {
		return rdsUpcUserMapper.saveUserReport(params);
	}

	@Override
	public Map<String, Object> queryDeptList(Map<String, Object> params) {
		List<Map<String, Object>> lists = rdsUpcUserMapper.queryDeptList(params);
		return lists==null?null:lists.get(0);
	}

	@Override
	public boolean updateDept(Map<String, Object> params) {
		return rdsUpcUserMapper.updateDept(params);
	}

}
