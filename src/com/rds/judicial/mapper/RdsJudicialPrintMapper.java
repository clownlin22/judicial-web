package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialCompareResultModel;
import com.rds.judicial.model.RdsJudicialIdentifyPer;
import com.rds.judicial.model.RdsJudicialPiInfoModel;
import com.rds.judicial.model.RdsJudicialPrintCaseModel;
import com.rds.judicial.model.RdsJudicialPrintModel;
import com.rds.judicial.model.RdsJudicialPrintCaseResultModel;
import com.rds.judicial.model.RdsJudicialPrintTableModel;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;
import com.rds.judicial.model.RdsJudicialSubCaseInfoModel;

public interface RdsJudicialPrintMapper {


	RdsJudicialPrintCaseModel getPrintCase(String case_code);
	
	List<RdsJudicialSubCaseInfoModel> getSubCaseInfos(String case_code);
	
	List<Map<String, String>> getSampleResult(Map<String, String> map);
	
	List<RdsJudicialSampleInfoModel> getSampleInfo(String case_id);
	
	List<RdsJudicialPrintCaseResultModel> getPrintResult(String case_code);
	
	RdsJudicialCaseAttachmentModel getPrintAttachment(String case_id);

	RdsJudicialCompareResultModel getCompareResult(String sub_case_code);

	int usePrintCount(Map<String, Object> params);
	
	List<RdsJudicialPrintTableModel> getPrintCaseTable(String case_code);
	
	List<RdsJudicialCaseInfoModel> getPrintCaseInfo(
			Map<String, Object> params);

    List<RdsJudicialCaseInfoModel> getPrintCaseInfoForWord(
            Map<String, Object> params);

	int countPrintCaseInfo(Map<String, Object> params);

    int countPrintCaseInfoForWord(Map<String, Object> params);
	
	List<RdsJudicialPiInfoModel> getPiInfoModel(Map<String, String> params);

	RdsJudicialPrintModel getPrintModel(Map<String, Object> params);

	int exsitSampleImg(Map<String, Object> params);

	List<String> getSampleImg(String case_code);

	List<RdsJudicialIdentifyPer> getIdentifyPer(String case_id);
	
	List<RdsJudicialCaseInfoModel> getChangePrintCaseInfo(
			Map<String, Object> params);

    List<RdsJudicialCaseInfoModel> getChangePrintCaseInfoForWord(
            Map<String, Object> params);
    
    int countChangePrintCaseInfo(Map<String, Object> params);

    int countChangePrintCaseInfoForWord(Map<String, Object> params);
    
    String getPiOne(Map<String, String> sample);

}
