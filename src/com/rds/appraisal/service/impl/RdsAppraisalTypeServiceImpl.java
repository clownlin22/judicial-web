package com.rds.appraisal.service.impl;

/**
 * @author yxb
 */
import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.appraisal.mapper.RdsAppraisalTypeMapper;
import com.rds.appraisal.service.RdsAppraisalTypeService;

@Service("RdsAppraisalTypeService")
public class RdsAppraisalTypeServiceImpl implements RdsAppraisalTypeService {

	@Setter
	@Autowired
	private RdsAppraisalTypeMapper rdsAppraisalTypeMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsAppraisalTypeMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return rdsAppraisalTypeMapper.queryModel(params);
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsAppraisalTypeMapper.queryAllCount(params));
		result.put("data", rdsAppraisalTypeMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsAppraisalTypeMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		return rdsAppraisalTypeMapper.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		return rdsAppraisalTypeMapper.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsAppraisalTypeMapper.delete(params);
	}

	@Override
	public Object queryStandardAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", rdsAppraisalTypeMapper.queryStandardAllPage(params));
		result.put("total", rdsAppraisalTypeMapper.queryStandardCount(params));
		return result;
	}

	@Override
	public int queryStandardCount(Object params) throws Exception {
		return rdsAppraisalTypeMapper.queryStandardCount(params);
	}

	@Override
	public int insertStandard(Object params) throws Exception {
		return rdsAppraisalTypeMapper.insertStandard(params);
	}

	@Override
	public int updateStandard(Object params) throws Exception {
		return rdsAppraisalTypeMapper.updateStandard(params);
	}

	@Override
	public int deleteStandard(Object params) throws Exception {
		return rdsAppraisalTypeMapper.deleteStandard(params);
	}
	

}
