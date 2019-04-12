package com.rds.judicial.service;

import java.util.Map;

public interface RdsJudicialCaseExceptService {

	Map<String, Object> save(Map<String, Object> params);

	Map<String, Object> queryAll(Map<String, Object> params);

	Map<String, Object> setNormal(Map<String, Object> params);

}
