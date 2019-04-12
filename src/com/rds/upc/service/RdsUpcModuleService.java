package com.rds.upc.service;

import java.util.Map;

public interface RdsUpcModuleService extends RdsUpcBaseService{
	
	public Object queryAllByParent(Map<String, Object> params) throws Exception;

}
