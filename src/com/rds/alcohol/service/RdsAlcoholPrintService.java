package com.rds.alcohol.service;

import java.util.List;
import java.util.Map;

import com.rds.alcohol.model.RdsAlcoholAttachmentModel;
import com.rds.alcohol.model.RdsAlcoholCaseInfoModel;
import com.rds.alcohol.model.RdsAlcoholIdentifyModel;
import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.model.RdsAlcoholSampleInfoModel;
import com.rds.judicial.model.RdsJudicialPrintModel;

public interface RdsAlcoholPrintService {

	RdsAlcoholResponse getPrintCaseInfo(Map<String, Object> params);

	List<RdsJudicialPrintModel> getPrintModel(String type);

	RdsAlcoholCaseInfoModel getCaseInfo(String case_code);

	RdsAlcoholSampleInfoModel getSampleInfo(String sample_id);

	boolean printCase(Map<String, Object> params);

    void createJudicialDocByCaseCode(Map<String,Object> params) throws Exception;

	RdsAlcoholIdentifyModel getIdentify(String case_checkper);

//	RdsAlcoholAttachmentModel getAttachment(String case_id);
	List<RdsAlcoholAttachmentModel> getAttachment(String case_id);
}
