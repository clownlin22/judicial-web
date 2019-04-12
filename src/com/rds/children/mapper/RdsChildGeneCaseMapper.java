package com.rds.children.mapper;

import java.util.List;

import com.rds.children.model.RdsChildrenCaseInfoModel;
import com.rds.children.model.RdsChildrenCaseLocusModel;
import com.rds.children.model.RdsChildrenCustodyInfoModel;
import com.rds.children.model.RdsChildrenQueryModel;

public interface RdsChildGeneCaseMapper {
	List<RdsChildrenCaseInfoModel> getChild(RdsChildrenQueryModel queryModel);

	List<RdsChildrenCustodyInfoModel> getCustody(String case_id);

	List<RdsChildrenCaseLocusModel> getLocus(String case_id);
}
