package com.rds.upc.mapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component("RdsUpcModuleMapper")
public interface RdsUpcModuleMapper extends RdsUpcBaseMapper {
	
	public List<Object> queryAllByParent(Map<String, Object> params) throws Exception;
	public String queryChildInfo(Object map);
}
