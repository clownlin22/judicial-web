package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialUpcAreaModel;
import com.rds.judicial.model.RdsJudicialUpcUserModel;

public interface RdsJudicialAreaMapper {

	List<RdsJudicialUpcUserModel> getUpcUserInfo(Map<String, Object> params);

	int countUpcUserInfo(Map<String, Object> params);

	int insertUpcUserInfo(Map<String, Object> params);

	int delUpcUserInfo(Map<String, Object> params);

	int updateUpcUserInfo(Map<String, Object> params);

	int exsitDicAreaCode(Map<String, Object> params);

	int saveDicAreaInfo(Map<String, Object> params);

	int delDicAreaInfo(Map<String, Object> params);

	int exsitDicAreaInfo(String areacode);

	List<RdsJudicialUpcAreaModel> getUpcAreaInfo(Map<String, Object> params);

	int delUpcAreaInfo(Map<String, Object> params);

	int saveUpcAreaInfo(Map<String, Object> params);

	int updateUpcAreaInfo(Map<String, Object> params);
	
	int updateDicAreaInfo(Map<String, Object> params);
	//根据usercode和areacode查询是否存在采样人员
	int queryUpcUserModel(Map<String, Object> params);
	//查看是否有重复采样地区
	int countAreaInfo(Map<String, Object> params);
	
	List<RdsJudicialUpcUserModel> getUpcUserInfoById(Map<String, Object> params);
	
	List<RdsJudicialDicAreaModel> getAllProvince(Map<String, Object> params);

	List<RdsJudicialDicAreaModel> getAllCity(Map<String, Object> params);

	List<RdsJudicialDicAreaModel> getAllCounty(Map<String, Object> params);
	
	List<RdsJudicialDicAreaModel> getDicArea(Map<String, Object> params);
}
