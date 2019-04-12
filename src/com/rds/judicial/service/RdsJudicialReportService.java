package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.rds.judicial.model.RdsJudicialCaseReportModel;
import com.rds.judicial.model.RdsJudicialHeadReportModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSubCaseInfoModel;

public interface RdsJudicialReportService {

	RdsJudicialResponse getCaseInfo(Map<String, Object> params);

	List<RdsJudicialCaseReportModel> getCaseReport(Map<String, Object> params);

	boolean updateReport(RdsJudicialHeadReportModel headReportModel,
			MultipartFile[] files);

	void downloadReport(String filepath, HttpServletResponse response);

}
