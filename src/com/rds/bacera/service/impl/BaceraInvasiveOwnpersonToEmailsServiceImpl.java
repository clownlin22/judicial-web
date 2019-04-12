package com.rds.bacera.service.impl;

import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.bacera.mapper.BaceraInvasiveOwnpersonToEmailsMapper;
import com.rds.bacera.service.BaceraInvasiveOwnpersonToEmailsService;
@Service("BaceraInvasiveOwnpersonToEmailsService")
public class BaceraInvasiveOwnpersonToEmailsServiceImpl implements BaceraInvasiveOwnpersonToEmailsService{
    @Setter
    @Autowired
	BaceraInvasiveOwnpersonToEmailsMapper OTM;
	
	@Override
	public Object queryAll(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		return OTM.queryAllPage(params);
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return OTM.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		return OTM.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		return OTM.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		return OTM.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryExsit(Map<String, Object> map) throws Exception {
		return OTM.queryExsit(map);
	}

	@Override
	public String queryOwnperson(String areacode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
