package com.rds.children.service;

import java.util.Map;

public interface RdsChildrenAgentiaService {

	Map<String, Object> getAgentiaInfo(Map<String, Object> params);

	Map<String, Object> save(Map<String, Object> params);

	Map<String, Object> getAgentiaCombo();
	
	Map<String, Object> getLocusInfo(Map<String, Object> params);
	
	Map<String, Object> delete(Map<String, Object> params);

}
