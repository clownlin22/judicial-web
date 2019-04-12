package com.rds.upc.service.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.upc.mapper.RdsUpcBlackListMapper;
import com.rds.upc.service.RdsUpcBlackListService;

@Service("rdsUpcBlackListService")
public class RdsUpcBlackListServiceImpl implements RdsUpcBlackListService {
	
	@Setter
	@Autowired
	private RdsUpcBlackListMapper rdsUpcBlackListMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return 0;
//		return rdsUpcBlackListMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsUpcBlackListMapper.queryAllCount(params));
		result.put("data", rdsUpcBlackListMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsUpcBlackListMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsUpcBlackListMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcBlackListMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
