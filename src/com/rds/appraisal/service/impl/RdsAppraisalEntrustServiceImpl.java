package com.rds.appraisal.service.impl;

/**
 * @author yxb
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.appraisal.mapper.RdsAppraisalEntrustMapper;
import com.rds.appraisal.service.RdsAppraisalEntrustService;

@Service("RdsAppraisalEntrustService")
public class RdsAppraisalEntrustServiceImpl implements RdsAppraisalEntrustService {

	@Setter
	@Autowired
	private RdsAppraisalEntrustMapper rdsAppraisalEntrustMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.queryModel(params);
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsAppraisalEntrustMapper.queryAllCount(params));
		result.put("data", rdsAppraisalEntrustMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.delete(params);
	}

	@Override
	public Object queryAllMechanism(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.queryAllMechanism(params);
	}

	@Override
	public Object queryModelMechanism(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.queryModelMechanism(params);
	}

	@Override
	public Object queryAllPageMechanism(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.queryAllPageMechanism(params);
	}

	@Override
	public int queryAllCountMechanism(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.queryAllCountMechanism(params);
	}

	@Override
	public int updateMechanism(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.updateMechanism(params);
	}

	@Override
	public int insertJudge(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.insertJudge(params);
	}

	@Override
	public int deleteMechanism(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.deleteMechanism(params);
	}

	@Override
	public List<String> queryJudge(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.queryJudge(params);
	}

	@Override
	public int deleteJudge(Object params) throws Exception {
		return rdsAppraisalEntrustMapper.deleteJudge(params);
	}


}
