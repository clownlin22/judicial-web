package com.rds.bacera.service.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.bacera.mapper.RdsBaceraProgramMapper;
import com.rds.bacera.service.RdsBaceraProgramService;
import com.rds.code.utils.uuid.UUIDUtil;

@Service("RdsBaceraProgramServiceImpl")
public class RdsBaceraProgramServiceImpl implements RdsBaceraProgramService {
	
	@Setter
	@Autowired
	private RdsBaceraProgramMapper rdsBaceraProgramMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsBaceraProgramMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsBaceraProgramMapper.queryAllCount(params));
		result.put("data", rdsBaceraProgramMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int update(Object params) throws Exception {
		try {
			Map<String, Object> map = (Map<String, Object>)params;
			return rdsBaceraProgramMapper.update(map);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insert(Object params) throws Exception {
		try {
			Map<String, Object> map = (Map<String, Object>)params;
			map.put("id", UUIDUtil.getUUID());
			return rdsBaceraProgramMapper.insert(map);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsBaceraProgramMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int queryNumExit(Map map) {
		return rdsBaceraProgramMapper.queryNumExit(map);
	}

}
