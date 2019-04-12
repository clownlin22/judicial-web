package com.rds.upc.service.impl;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.upc.mapper.RdsUpcUserPermitMapper;
import com.rds.upc.service.RdsUpcUserPermitService;

@Service("rdsUpcUserPermitService")
public class RdsUpcUserPermitServiceImpl extends RdsUpcAbstractServiceImpl implements RdsUpcUserPermitService {
	
	@Setter
	@Autowired
	private RdsUpcUserPermitMapper rdsUpcUserPermitMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsUpcUserPermitMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		return null;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsUpcUserPermitMapper.update(params);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsUpcUserPermitMapper.insert(params);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcUserPermitMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
