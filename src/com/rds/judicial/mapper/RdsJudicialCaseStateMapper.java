package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("RdsJudicialCaseStateMapper")
public interface RdsJudicialCaseStateMapper extends RdsJudicialBaseMapper{
//	public List<RdsJudicialKeyValueModel> queryUserByType(Map<String, Object> params);
	
	public List<Object> queryCaseState(Map<String, Object> params);
	
	public int queryCaseStateCount(Map<String, Object> params);
	
	public int queryCompareResultCount(Map<String, Object> params);

}
