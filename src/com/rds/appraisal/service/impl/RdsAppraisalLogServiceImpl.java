package com.rds.appraisal.service.impl;

/**
 * @author yxb
 */
import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.appraisal.mapper.RdsAppraisalLogMapper;
import com.rds.appraisal.service.RdsAppraisalLogService;

@Service("RdsAppraisalLogService")
public class RdsAppraisalLogServiceImpl implements RdsAppraisalLogService {

	@Setter
	@Autowired
	private RdsAppraisalLogMapper rdsAppraisalLogMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsAppraisalLogMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return rdsAppraisalLogMapper.queryModel(params);
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsAppraisalLogMapper.queryAllCount(params));
		result.put("data", rdsAppraisalLogMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsAppraisalLogMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		return rdsAppraisalLogMapper.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		return rdsAppraisalLogMapper.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsAppraisalLogMapper.delete(params);
	}

}
