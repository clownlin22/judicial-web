package com.rds.upc.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("rdsUpcPermitMapper")
public interface RdsUpcPermitMapper extends RdsUpcBaseMapper {
	
	public List<Object> queryPermitModelByParentCode(Object params);
	
	public List<Object> queryUserPermitModel(Object params);
	
	public int insertPermitModule(Object params) throws Exception;
	
	public int deletePermitModule(Object params) throws Exception; 

	public List<Object> queryModule(Map<String, Object> params);
}
