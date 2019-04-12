package com.rds.children.mapper;

import java.util.List;
import java.util.Map;

import com.rds.children.model.RdsChildrenTariffModel;

public interface RdsChildrenTariffMapper {

	List<RdsChildrenTariffModel> getTariffInfo(Map<String, Object> params);

	int getTariffInfoCount(Map<String, Object> params);

	int save(Map<String, Object> params);

	int delete(Map<String, Object> params);

}
