package com.rds.children.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.children.mapper.RdsChildrenExceptionMapper;
import com.rds.children.model.RdsChildrenExceptionModel;
import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.service.RdsChildrenExceptionService;
import com.rds.code.utils.uuid.UUIDUtil;

@Service("RdsChildrenExceptionService")
public class RdsChildrenExceptionServiceImpl implements
		RdsChildrenExceptionService {

	@Autowired
	private RdsChildrenExceptionMapper rdsChildrenExceptionMapper;

	@Override
	public RdsChildrenResponse getExceptionInfo(Map<String, Object> params) {
		RdsChildrenResponse response = new RdsChildrenResponse();
		List<RdsChildrenExceptionModel> caseInfoModels = rdsChildrenExceptionMapper
				.getExceptionInfo(params);
		int count = rdsChildrenExceptionMapper.getExceptionInfoCount(params);
		response.setItems(caseInfoModels);
		response.setCount(count);
		return response;
	}

	@Override
	public int getExceptionInfoCount(Map<String, Object> params) {
		return rdsChildrenExceptionMapper.getExceptionInfoCount(params);
	}

	@Override
	public boolean saveException(Map<String, Object> params) {
		params.put("exception_id", UUIDUtil.getUUID());
		return rdsChildrenExceptionMapper.saveException(params);
	}

	@Override
	public boolean deleteException(Map<String, Object> params) {
		return rdsChildrenExceptionMapper.deleteException(params);
	}

	@Override
	public boolean updateException(Map<String, Object> params) {
		return rdsChildrenExceptionMapper.updateExcetion(params);
	}

}
