package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialFeeQuationModel;

public interface RdsJudicialFeeQuationMapper {
	int insertQuation(RdsJudicialFeeQuationModel feeQuation);

	List<Map<String, Object>> queryType(Map<String, Object> params);

	int queryTypeCount(Map<String, Object> params);

	List<RdsJudicialFeeQuationModel> getEquation(Map<String, Object> map);

	int delete(String id);

	void updateFeetype(Map<String, Object> params);

	void updateEquation(Map<String, Object> params);
}
