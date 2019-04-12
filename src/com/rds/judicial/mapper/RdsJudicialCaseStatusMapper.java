package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialCaseExceprModel;

public interface RdsJudicialCaseStatusMapper {

	int insert(RdsJudicialCaseExceprModel csModel);

	int getCaseid(String case_id);

	int update(Map<String, Object> params);
	
	List<Map<String, Object>> queryAll(Map<String, Object> params);

	int queryAllCount(Map<String, Object> params);

	int setNormal(Map<String, Object> params);

}
