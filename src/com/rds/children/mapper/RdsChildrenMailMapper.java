package com.rds.children.mapper;

import java.util.List;
import java.util.Map;

import com.rds.children.model.RdsChildrenMailCaseModel;

public interface RdsChildrenMailMapper {

	List<RdsChildrenMailCaseModel> getMailCaseInfo(Map<String, Object> params);

	int countMailCaseInfo(Map<String, Object> params);

}
