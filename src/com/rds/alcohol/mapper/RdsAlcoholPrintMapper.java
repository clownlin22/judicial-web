package com.rds.alcohol.mapper;

import java.util.List;
import java.util.Map;

import com.rds.alcohol.model.RdsAlcoholAttachmentModel;
import com.rds.alcohol.model.RdsAlcoholCaseInfoModel;
import com.rds.alcohol.model.RdsAlcoholIdentifyModel;
import com.rds.alcohol.model.RdsAlcoholPrintInfoModel;
import com.rds.alcohol.model.RdsAlcoholSampleInfoModel;
import com.rds.judicial.model.RdsJudicialPrintModel;

public interface RdsAlcoholPrintMapper {

	List<RdsAlcoholCaseInfoModel> getPrintCaseInfo(Map<String, Object> params);

	int countPrintCaseInfo(Map<String, Object> params);

	List<RdsJudicialPrintModel> getPrintModel(String type);

	RdsAlcoholCaseInfoModel getCaseInfo(String case_code);

	RdsAlcoholSampleInfoModel getSampleInfo(String sample_id);

	int printCase(Map<String, Object> params);

	RdsAlcoholPrintInfoModel getPrintInfo(String case_id);

	List<Double> getPrintResult(String exper_id);

	RdsAlcoholIdentifyModel getIdentify(String case_checkper);

	List<RdsAlcoholAttachmentModel> getAttachment(String case_id);

	List<RdsAlcoholIdentifyModel> getIdentifyById(String case_id);

}
