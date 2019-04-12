package com.rds.alcohol.service;

import com.rds.alcohol.model.*;
import com.rds.judicial.model.RdsJudicialCaseFeeModel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface RdsAlcoholRegisterService {

	RdsAlcoholResponse getCaseInfo(Map<String, Object> params);

	boolean deleteCaseInfo(Map<String, Object> params);

	boolean exsitCaseCode(String case_code);

	boolean addCaseInfo(RdsAlcoholCaseInfoModel caseInfoModel,
			RdsAlcoholSampleInfoModel sampleInfomodel, RdsJudicialCaseFeeModel casefee, MultipartFile[] files, RdsAlcoholAttachmentModel att);

	void getImg(HttpServletResponse response, String filename);

	boolean updateCaseCode(RdsAlcoholCaseInfoModel caseInfoModel,
			RdsAlcoholSampleInfoModel sampleInfoModel);

	void exportCaseInfo(RdsAlcoholQueryParam param,
			HttpServletResponse response);

	RdsAlcoholSampleInfoModel getSampleInfo(Map<String, Object> params);

	Map<String, Object> getClient();
	
	void exportCaseInfo(Map<String, Object> params,HttpServletResponse response)  throws Exception;

	Map<String, Object> getClient2();

	boolean exsitcase_code(String case_code);

    Map<String,Object> getIdentificationPer();
}
