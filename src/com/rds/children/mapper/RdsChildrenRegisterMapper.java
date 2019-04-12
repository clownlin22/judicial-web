package com.rds.children.mapper;

import java.util.List;
import java.util.Map;

import com.rds.children.model.RdsChildrenCaseInfoModel;
import com.rds.children.model.RdsChildrenCasePhotoModel;
import com.rds.children.model.RdsChildrenCustodyInfoModel;
import com.rds.children.model.RdsChildrenGatherInfoModel;
import com.rds.children.model.RdsChildrenTariffModel;

public interface RdsChildrenRegisterMapper {

	List<RdsChildrenCaseInfoModel> getCaseInfo(Map<String, Object> params);

	int countCaseInfo(Map<String, Object> params);

	List<RdsChildrenCustodyInfoModel> getCustodyInfo(Map<String, Object> params);

	RdsChildrenGatherInfoModel getGatherInfo(Map<String, Object> params);

	boolean deleteCaseInfo(Map<String, Object> params);

	void addGatherInfo(RdsChildrenGatherInfoModel gatherInfoModel);

	boolean addCaseInfo(Map<String, Object> params);

	void addCustodyInfo(RdsChildrenCustodyInfoModel custodyInfoModel);

	int exsitCaseCode(Map<String, Object> params);
	
	int exsitSampleCode(Map<String, Object> params);

	void deleteGatherInfo(Map<String, Object> params);

	void deleteCustodyInfo(Map<String, Object> params);

	boolean updateCaseInfo(Map<String, Object> params);

	List<RdsChildrenTariffModel> getTariff();

	int insertHeadPhoto(RdsChildrenCasePhotoModel pmodel);
	
	int deleteCasePhoto(RdsChildrenCasePhotoModel pmodel);

	List<RdsChildrenCaseInfoModel> exportInfo(Map<String, Object> params);
	
	int updateCaseState(Map<String, Object> params);
	
	/**
	 * 照片处理查询
	 * @param params
	 * @return
	 */
	List<RdsChildrenCasePhotoModel> queryCasePhoto(Map<String, Object> params);
	
	/**
	 * 照片处理查询总数
	 * @param params
	 * @return
	 */
	int queryCasePhotoCount(Map<String, Object> params);
	
	/**
	 * 查询生成好的图片
	 * @param params
	 * @return
	 */
	List<RdsChildrenCasePhotoModel> queryProductCasePhoto(Map<String, Object> params);
	
	//打印查询单个案例
	RdsChildrenCaseInfoModel getcaseinfobyself(Map<String, Object> params);
	
	/**
	 * 查询基因位点
	 */
	List<Map<String, String>>  queryOrderbyid(Map<String,Object> params);
}
