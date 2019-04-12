package com.rds.judicial.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialExceptionTypeMapper;
import com.rds.judicial.model.RdsJudicialExceptionTypeModel;
import com.rds.judicial.service.RdsJudicialExceptionTypeService;

@Service("RdsJudicialExceptionTypeService")
@Transactional
public class RdsJudicialExceptionTypeServiceImpl implements RdsJudicialExceptionTypeService{

	@Autowired
	private RdsJudicialExceptionTypeMapper tMapper;
	
	@Override
	public Map<String, Object> getType(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data",  tMapper.getType(params));
		result.put("total", tMapper.getTypeCount(params));
		return result;
	}

	@Override
	public boolean addExceptionType(RdsJudicialExceptionTypeModel tModel) {
		tModel.setType_id(UUIDUtil.getUUID());
		if (tMapper.insertExceptionType(tModel)>0)
			return true;
		return false;
	}

	@Override
	public boolean updateExceptionType(RdsJudicialExceptionTypeModel tModel) {
		if(tMapper.updateExceptionType(tModel)>0)
			return true;
		return false;
	}

	@Override
	public boolean deleteExceptionType(Map<String, Object> params) {
		if (tMapper.deleteExceptionType(params)>0)
			return true;
		return false;
	}



}
