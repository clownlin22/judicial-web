package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialIdentifyPer;
import com.rds.judicial.model.RdsJudicialPrintModel;
import com.rds.judicial.model.RdsJudicialPrintTableModel;
import com.rds.judicial.model.RdsJudicialPrintCaseModel;
import com.rds.judicial.model.RdsJudicialResponse;

public interface RdsJudicialPrintService {

	RdsJudicialPrintCaseModel getPrintCaseZY(String case_code, String case_id,String type);

	boolean usePrintCount(Map<String, Object> params);

	List<RdsJudicialPrintTableModel> getPrintTable(String case_code,String case_id,String type);

	boolean exsitPrintTable(Map<String, Object> params);

	RdsJudicialPrintModel getPrintModel(Map<String, Object> params);

	boolean exsitSampleImg(Map<String, Object> params);

	List<String> getSampleImg(String case_code);

	RdsJudicialResponse getPrintCaseInfo(Map<String, Object> params);

    RdsJudicialResponse getPrintCaseInfoForWord(Map<String, Object> params);

	List<RdsJudicialIdentifyPer> getIdentifyPer(String case_id);

    void createJudicialDoc(Map<String,Object> params) throws Exception;

    void createJudicialDocBySubCaseCode(Map<String,Object> params) throws Exception;
    
	RdsJudicialResponse getChangePrintCaseInfo(Map<String, Object> params);

    RdsJudicialResponse getChangePrintCaseInfoForWord(Map<String, Object> params);

}
