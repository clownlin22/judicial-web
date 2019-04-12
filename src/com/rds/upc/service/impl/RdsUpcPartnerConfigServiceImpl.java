package com.rds.upc.service.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.upc.mapper.RdsUpcPartnerConfigMapper;
import com.rds.upc.service.RdsUpcPartnerConfigService;

@Service("rdsUpcPartnerConfigService")
public class RdsUpcPartnerConfigServiceImpl implements RdsUpcPartnerConfigService {
	
	@Setter
	@Autowired
	private RdsUpcPartnerConfigMapper rdsUpcPartnerConfigMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsUpcPartnerConfigMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsUpcPartnerConfigMapper.queryAllCount(params));
		result.put("data", rdsUpcPartnerConfigMapper.queryAllPage(params));
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
			return rdsUpcPartnerConfigMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsUpcPartnerConfigMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsUpcPartnerConfigMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@Override
	public int queryQartnerExist(Object params) throws Exception {
		return rdsUpcPartnerConfigMapper.queryQartnerExist(params);
	}

	@Override
	public String getCaseTask(String str) throws Exception {
		return rdsUpcPartnerConfigMapper.getCaseTask(str);
	}

	@Override
	public String getLaboratoryNo(String str) throws Exception {
		return rdsUpcPartnerConfigMapper.getLaboratoryNo(str);
	}

	@Override
	public int insertPartnerModel(Object params) throws Exception {
		return rdsUpcPartnerConfigMapper.insertPartnerModel(params);
	}

	@Override
	public int deletePartnerModel(Object params) throws Exception {
		return rdsUpcPartnerConfigMapper.deletePartnerModel(params);
	}

}
