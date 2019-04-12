package com.rds.upc.mapper;

import org.springframework.stereotype.Component;

@Component("RdsUpcPartnerConfigMapper")
public interface RdsUpcPartnerConfigMapper extends RdsUpcBaseMapper {
	
	public int queryQartnerExist(Object params) throws Exception;
	
	public String getCaseTask(String str) throws Exception;
	
	public String getLaboratoryNo(String str) throws Exception;
	
	public int insertPartnerModel(Object params) throws Exception;
	
	public int deletePartnerModel(Object params) throws Exception;
}
