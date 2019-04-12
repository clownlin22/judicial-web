package com.rds.judicial.service;

import java.util.Map;

import com.rds.judicial.model.RdsJudicialExceptionTypeModel;

public interface RdsJudicialExceptionTypeService {

	public Map<String, Object> getType(Map<String, Object> params);

	public boolean addExceptionType(RdsJudicialExceptionTypeModel tModel);

	public boolean updateExceptionType(RdsJudicialExceptionTypeModel tModel);

	public boolean deleteExceptionType(Map<String, Object> params);
}
