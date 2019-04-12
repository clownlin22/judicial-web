package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialExceptionTypeModel;

public interface RdsJudicialExceptionTypeMapper {
	List<RdsJudicialExceptionTypeModel> getType(Map<String, Object> params);

	int getTypeCount(Map<String, Object> params);

	int insertExceptionType(RdsJudicialExceptionTypeModel tModel);

	int updateExceptionType(RdsJudicialExceptionTypeModel tModel);

	int deleteExceptionType(Map<String, Object> params);
}
