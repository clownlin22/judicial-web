package com.rds.bacera.service.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.bacera.mapper.RdsBaceraHospitalAreaNameMapper;
import com.rds.bacera.service.RdsBaceraHospitalAreaNameService;
@Service("RdsBaceraHospitalAreaNameService")
public class RdsBaceraHospitalAreaNameServicelmpl implements RdsBaceraHospitalAreaNameService{
   
	@Setter
	@Autowired
	private RdsBaceraHospitalAreaNameMapper rdsBaceraHospitalAreaNameMapper;
	
	
	
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
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsBaceraHospitalAreaNameMapper.queryAllCount(params));
		result.put("data", rdsBaceraHospitalAreaNameMapper.queryAllPage(params));
		
		return result;
				
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsBaceraHospitalAreaNameMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		
		return rdsBaceraHospitalAreaNameMapper.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		
		return rdsBaceraHospitalAreaNameMapper.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsBaceraHospitalAreaNameMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryExsit(@SuppressWarnings("rawtypes") Map map) throws Exception {
		return rdsBaceraHospitalAreaNameMapper.queryExsit(map);
	}

	@Override
	public String queryHospital(String areacode) throws Exception {

		return rdsBaceraHospitalAreaNameMapper.queryHospital(areacode);
	}

	@Override
	public int queryOne(Map<String, Object> params) throws Exception {
		
		return rdsBaceraHospitalAreaNameMapper.queryOne(params);
	}

}
