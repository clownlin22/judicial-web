package com.rds.finance.service.impl;

/**
 * yuanxiaobo 2016-10-08
 */
import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.finance.mapper.RdsFinanceBaceraMapper;
import com.rds.finance.service.RdsFinanceBaceraService;

@Service("RdsFinanceBaceraService")
public class RdsFinanceBaceraServiceImpl implements RdsFinanceBaceraService {

	@Setter
	@Autowired
	private RdsFinanceBaceraMapper rdsFinanceBaceraMapper;
	
	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total",rdsFinanceBaceraMapper.queryAllCount(params));
		result.put("data", rdsFinanceBaceraMapper.queryAllPage(params));
		return result;
		
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return 0;
	}

	@Override
	public boolean update(Object params) throws Exception {
		return rdsFinanceBaceraMapper.update(params);
	}

	@Override
	public boolean insert(Object params) throws Exception {
		return rdsFinanceBaceraMapper.insert(params);
	}

	@Override
	public boolean delete(Object params) throws Exception {
		return rdsFinanceBaceraMapper.delete(params);
	}

	@Override
	public boolean confirmCase(Object params) throws Exception {
		return rdsFinanceBaceraMapper.confirmCase(params);
	}
	
}
