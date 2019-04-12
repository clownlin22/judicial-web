package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialCaseStateInfoModel;


public interface RdsJudicialCaseStateService extends RdsJudicialBaseService {

	public List<RdsJudicialCaseStateInfoModel> queryCaseState(Map<String, Object> params);
	
//	public Object queryAgent(Map<String, Object> params);
	
	public int queryCaseStateCount(Map<String, Object> params);

	public int queryCompareResultCount(Map<String, Object> params);
}
