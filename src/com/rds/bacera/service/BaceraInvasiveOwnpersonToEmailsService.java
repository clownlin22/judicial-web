package com.rds.bacera.service;

import java.util.Map;

public interface BaceraInvasiveOwnpersonToEmailsService extends RdsBaceraBaseService {
	  public int queryExsit(Map<String, Object> map) throws Exception;
	     public String queryOwnperson (String areacode)throws Exception;
}
