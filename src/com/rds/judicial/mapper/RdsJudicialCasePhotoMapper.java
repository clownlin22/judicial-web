package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;

public interface RdsJudicialCasePhotoMapper {
	
	List<RdsJudicialCaseInfoModel> getPrintCaseInfo(
			Map<String, Object> params);

	int countPrintCaseInfo(Map<String, Object> params);

	List<RdsJudicialCaseAttachmentModel> getCaseReceiver(
			Map<String, Object> params);

	List<RdsJudicialCaseAttachmentModel> getAllCasePhoto(
			Map<String, Object> params);

	int getAllCasePhotoCount(Map<String, Object> params);

}
