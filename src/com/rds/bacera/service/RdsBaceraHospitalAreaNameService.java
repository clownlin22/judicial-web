package com.rds.bacera.service;

import java.util.Map;

public interface RdsBaceraHospitalAreaNameService extends RdsBaceraBaseService{
     public int queryExsit(@SuppressWarnings("rawtypes") Map map) throws Exception;
     public int queryOne(Map<String, Object> params) throws Exception;
     public String queryHospital (String areacode)throws Exception;
}
