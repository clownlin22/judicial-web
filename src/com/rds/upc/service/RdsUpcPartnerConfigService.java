package com.rds.upc.service;

public interface RdsUpcPartnerConfigService extends RdsUpcBaseService {
	public int queryQartnerExist(Object params) throws Exception;
	
	public String getCaseTask(String str) throws Exception;
	
	public String getLaboratoryNo(String str) throws Exception;
	
	public int insertPartnerModel(Object params) throws Exception;
	
	public int deletePartnerModel(Object params) throws Exception;
}
