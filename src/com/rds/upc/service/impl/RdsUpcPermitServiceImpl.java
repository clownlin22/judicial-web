package com.rds.upc.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;

import com.rds.upc.mapper.RdsUpcPermitMapper;
import com.rds.upc.service.RdsUpcPermitService;

@Service("rdsUpcPermitService")
public class RdsUpcPermitServiceImpl implements RdsUpcPermitService {
	
	@Setter
	@Autowired
	private RdsUpcPermitMapper rdsUpcPermitMapper;

	@Override
	public Object queryPermitModelByParentCode(Object params) {
		return rdsUpcPermitMapper.queryPermitModelByParentCode(params);
	}

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsUpcPermitMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return rdsUpcPermitMapper.queryModel(params);
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		return rdsUpcPermitMapper.queryAllPage(params);
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsUpcPermitMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		return rdsUpcPermitMapper.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		return rdsUpcPermitMapper.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsUpcPermitMapper.delete(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean savePermitModule(Object params) throws Exception {
		try {
			rdsUpcPermitMapper.deletePermitModule(((Map<String, Object>)params).get("permitcode"));
			Object list = ((Map<String, Object>)params).get("data");
			rdsUpcPermitMapper.insertPermitModule(list);
			return true;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public Object deletePermitModule(Object params) throws Exception {
		return rdsUpcPermitMapper.deletePermitModule(params);
	}

	@Override
	public Object queryUserPermitModel(Object params) {
		// TODO Auto-generated method stub
		return rdsUpcPermitMapper.queryUserPermitModel(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Object> queryModule(Map<String, Object> params) {
		return rdsUpcPermitMapper.queryModule(params);
	}
	

}
