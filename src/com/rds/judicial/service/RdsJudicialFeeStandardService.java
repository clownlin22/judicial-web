package com.rds.judicial.service;

import java.util.Map;

public interface RdsJudicialFeeStandardService {

	Map<String, Object> saveEquation(Map<String, Object> params);

	Map<String, Object> queryType(Map<String, Object> params);

	Map<String, Object> delete(String id);

	Map<String, Object> updateEquation(Map<String, Object> params);

}
