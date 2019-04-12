package com.rds.children.service;

import java.util.Map;

public interface RdsChildrenTariffService {

	Map<String, Object> getTariffInfo(Map<String, Object> params);

	Map<String, Object> save(Map<String, Object> params);

	Map<String, Object> delete(Map<String, Object> params);

}
