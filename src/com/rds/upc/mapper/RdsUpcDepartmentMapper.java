package com.rds.upc.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

@Component("rdsUpcDepartmentMapper")
public interface RdsUpcDepartmentMapper extends RdsUpcBaseMapper {
	
	public String queryChildInfo(Object map);
	
	public List<Object> queryTreeCombo(Object map) throws Exception;

	public int updateJunior(Object params);
	
	public int queryCountByCode(Object params);
	
	public List<Object> queryDepartmentList(Object params);

}
