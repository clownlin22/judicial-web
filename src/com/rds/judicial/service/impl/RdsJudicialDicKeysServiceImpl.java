package com.rds.judicial.service.impl;

/**
 * @author chen wei
 */
import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.judicial.mapper.RdsJudicialDicKeysMapper;
import com.rds.judicial.service.RdsJudicialDicKeysService;

@Service("RdsJudicialDicKeysService")
public class RdsJudicialDicKeysServiceImpl implements RdsJudicialDicKeysService {
	
	@Setter
	@Autowired
	private RdsJudicialDicKeysMapper RdsJudicialDicKeysMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialDicKeysMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", RdsJudicialDicKeysMapper.queryAllCount(params));
		result.put("data", RdsJudicialDicKeysMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialDicKeysMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialDicKeysMapper.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialDicKeysMapper.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialDicKeysMapper.delete(params);
	}

}
