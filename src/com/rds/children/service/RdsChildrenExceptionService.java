package com.rds.children.service;

import java.util.Map;

import com.rds.children.model.RdsChildrenResponse;

public interface RdsChildrenExceptionService {

	RdsChildrenResponse getExceptionInfo(Map<String, Object> params);

	int getExceptionInfoCount(Map<String, Object> params);

	boolean saveException(Map<String, Object> params);

	boolean deleteException(Map<String, Object> params);
	
	boolean updateException(Map<String, Object> params);

}
