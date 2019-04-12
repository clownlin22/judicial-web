package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialCaseReportModel;
import com.rds.judicial.model.RdsJudicialHeadReportModel;

public interface RdsJudicialReportMapper {

	
	public List<RdsJudicialCaseInfoModel> getCaseInfo(Map<String,Object> params);

	public int countCaseInfo(Map<String, Object> params);

	public List<RdsJudicialCaseReportModel> getCaseReport(Map<String, Object> params);

	public int updateReport(RdsJudicialHeadReportModel headReportModel);

	public void updateCaseResult(RdsJudicialHeadReportModel headReportModel);

	public void delReport(RdsJudicialHeadReportModel headReportModel);
}
