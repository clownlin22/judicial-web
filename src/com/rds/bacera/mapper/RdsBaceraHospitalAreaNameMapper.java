package com.rds.bacera.mapper;


import java.util.Map;

import org.springframework.stereotype.Component;

@Component("RdsBaceraHospitalAreaNameMapper")
public interface RdsBaceraHospitalAreaNameMapper extends RdsBaceraBaseMapper{
   int queryExsit(Object params);
   int queryOne(Map<String, Object> params);
   String queryHospital(String areacode);
}
