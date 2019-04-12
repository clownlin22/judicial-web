package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rds.judicial.model.RdsJudicialApplySampleCodeModel;
import com.rds.judicial.model.RdsJudicialCaseAttachmentPrintModel;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.model.RdsJudicialParamsModel;
import com.rds.judicial.model.RdsJudicialSampleExpressModel;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;
import com.rds.judicial.model.RdsJudicialSecondModel;

@Component("RdsJudicialRegisterMapper")
public interface RdsJudicialRegisterMapper extends RdsJudicialBaseMapper{

	List<RdsJudicialCaseInfoModel> queryAllCaseInfo(RdsJudicialParamsModel params);

	int countCaseInfo(Map<String, Object> params);

	int deleteCaseInfo(Map<String, Object> params);

	int deleteSampleInfo(Map<String, Object> params);

	List<RdsJudicialSampleInfoModel> getSampleInfo(Object case_id);

	int insertCaseInfo(RdsJudicialCaseInfoModel caseInfoModel);

	int insertSampleInfo(RdsJudicialSampleInfoModel model);

	List<RdsJudicialCaseInfoModel> queryCaseInfo(Map<String, Object> params);

	int updateCaseInfo(RdsJudicialCaseInfoModel caseInfoModel);
	
	
	int updateIsBill(Map<String, Object> params);
	
	int updateIsArchived(Map<String, Object> params);

    int updateSampleVerifyinfo(Map<String, Object> params);

	int exsitCaseCode(Map<String, Object> params);
	
	int exsitSampleCode(Map<String, Object> params);

	String getClient(Map<String, Object> params);

	List<RdsJudicialCaseAttachmentPrintModel> getPrintAttachment(
			Map<String, Object> params);

	int countPrintAttachment(Map<String, Object> params);

	boolean printAttachment(Map<String, Object> params);

	int exsitBlackNumber(String id_number);
	
	void addCaseFee(Map<String, Object> result);

	List<RdsJudicialSampleInfoModel> queryParentSampleInfo(String case_id);

	List<RdsJudicialSampleInfoModel> queryChildSampleInfo(String case_id);

	boolean returnCaseInfoState(Map<String, Object> params);

    RdsJudicialCaseInfoModel queryCaseInfoByCaseCode(String case_code);

	void updateClient(RdsJudicialCaseInfoModel caseInfoModel);

	List<RdsJudicialSampleInfoModel> queryParentSampleInfoList(
			List<RdsJudicialCaseInfoModel> caseInfoModels);

	List<RdsJudicialSampleInfoModel> queryChildSampleInfoList(
			List<RdsJudicialCaseInfoModel> caseInfoModels);
	
	int updatenewtimebycode(Map<String, Object> params);


	//添加样本组合
	int addCaseSample(Map<String, Object> params);
	//更新样本组合
	int updateCaseSample(Map<String, Object> params);
	
	RdsJudicialKeyValueModel getSampleCallKey(String keyid);
	
	/* 案例编号自动 start */ 
	int insertCaseCodeNum(Map<String, Object> params);
	
	List<Map<String, Object>> queryCaseCodeNum(Map<String, Object> params);
	/* 案例编号自动  end */ 
	
	/* 样本快递信息 start */ 
	int insertSampleExpress(Map<String, Object> params);
	
	int updateSampleExpress(Map<String, Object> params);
	
	int deleteSampleExpress(Map<String, Object> params);
	
	List<RdsJudicialSampleExpressModel> querySampleExpress(Map<String, Object> params);
	/* 样本快递信息  end */ 
	
	int updateCaseCompareDate(Map<String, Object> params);
	
	int updateCaseVerifyState(Map<String, Object> params);
	
	int insertCaseCodeSecond(Map<String, Object> params);
	
	int updateCaseCodeSecond(Map<String, Object> params);
	
	List<RdsJudicialSecondModel> queryCaseCodeSecond(Map<String, Object> params);
	
	/**
	 * 批量插入样本条形码
	 * @param params
	 * @return
	 */
	int insertSampleCode(Map<String, Object> params);
	
	/**
	 * 分页查询样本条形码
	 * @param params
	 * @return
	 */
	List<RdsJudicialApplySampleCodeModel> queryApplySampleCode(Map<String, Object> params);
	
	int countApplySampleCode(Map<String, Object> params);
	
	int queryMaxApplyBefore();
	int queryMaxApplyAfter();
	
	List<RdsJudicialCaseInfoModel> queryChangeCaseInfo(Map<String, Object> params);
	
	int countChangeCaseInfo(Map<String, Object> params);
	
	int insertCompanyCodeCaseid(Map<String, Object> params);
	
	int updateCompanyCodeCaseid(Map<String, Object> params);
	
	int selectProvinceUser(String userid);
	
	int deleteSubCaseInfo(Map<String, Object> params);
	
	//添加江苏省内子渊案例流水号
	int insertSerialNumber(Map<String, Object> params);
	

	
}

