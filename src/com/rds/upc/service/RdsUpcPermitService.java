package com.rds.upc.service;

import java.util.List;
import java.util.Map;

public interface RdsUpcPermitService extends RdsUpcBaseService {
	
	public Object queryPermitModelByParentCode(Object params);
	
	public Object queryUserPermitModel(Object params);
	
	public boolean savePermitModule(Object params) throws Exception;
	
	public Object deletePermitModule(Object params) throws Exception;

	public  List<Object> queryModule(Map<String, Object> params);
}
