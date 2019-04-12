package com.rds.bacera.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("BaceraInvasiveOwnpersonToEmailsMapper")
public interface BaceraInvasiveOwnpersonToEmailsMapper extends RdsBaceraBaseMapper{
	   int queryExsit(Map<String, Object> params);
	   String queryHospital(String areacode);
}
