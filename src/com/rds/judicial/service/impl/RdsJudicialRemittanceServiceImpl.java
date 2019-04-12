package com.rds.judicial.service.impl;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.judicial.mapper.RdsJudicialRemittanceMapper;
import com.rds.judicial.service.RdsJudicialRemittanceService;

/**
 * @description 汇款账户管理
 * @author yxb
 * 2016-05-10
 */
@Service("RdsJudicialRemittanceService")
@Transactional
public class RdsJudicialRemittanceServiceImpl implements RdsJudicialRemittanceService{

	@Setter
	@Autowired
	private RdsJudicialRemittanceMapper rdsJudicialRemittanceMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsJudicialRemittanceMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		return rdsJudicialRemittanceMapper.queryAllPage(params);
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsJudicialRemittanceMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		return rdsJudicialRemittanceMapper.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		return rdsJudicialRemittanceMapper.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsJudicialRemittanceMapper.delete(params);
	}

	@Override
	public int queryExistCount(Object params) throws Exception {
		return rdsJudicialRemittanceMapper.queryExistCount(params);
	}

}
