package com.rds.children.mapper;

import java.util.List;
import java.util.Map;

import com.rds.children.model.RdsChildrenExceptionModel;

public interface RdsChildrenExceptionMapper {

	List<RdsChildrenExceptionModel> getExceptionInfo(Map<String, Object> params);

	int getExceptionInfoCount(Map<String, Object> params);

	boolean saveException(Map<String, Object> params);

	boolean deleteException(Map<String, Object> params);
	
	boolean updateExcetion(Map<String, Object> params);

}
