package com.rds.children.mapper;

import java.util.List;
import java.util.Map;

import com.rds.children.model.RdsChildrenAgentiaLocusModel;
import com.rds.children.model.RdsChildrenAgentiaModel;

public interface RdsChildrenAgentiaMapper {
	List<RdsChildrenAgentiaModel> getAgentiaInfo(Map<String, Object> params);

	int getAgentiaInfoCount(Map<String, Object> params);

	int insertAgentia(RdsChildrenAgentiaModel aModel);

	int insertLocus(List<RdsChildrenAgentiaLocusModel> locusList);

	List<RdsChildrenAgentiaModel> getAgentiaCombo();
	
	List<String> getLocusName(String agentia_name);
	
	List<RdsChildrenAgentiaLocusModel> getLocusInfo(Map<String, Object> params);
	
	int delete(Map<String, Object> params);
}
