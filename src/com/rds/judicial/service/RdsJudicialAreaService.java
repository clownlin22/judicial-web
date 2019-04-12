package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialUpcAreaModel;
import com.rds.judicial.model.RdsJudicialUpcUserModel;
import com.rds.upc.model.RdsUpcPermitNodeModel;

public interface RdsJudicialAreaService {

	RdsJudicialResponse getUpcUserInfo(Map<String, Object> params);

	Object saveUpcUserInfo(Map<String, Object> params);

	boolean delUpcUserInfo(Map<String, Object> params);

	Object updateUpcUserInfo(Map<String, Object> params);

	List<RdsJudicialDicAreaModel> getDicAreaInfo(Map<String, Object> params);

	boolean exsitDicAreaCode(Map<String, Object> params);

	boolean saveDicAreaInfo(Map<String, Object> params);

	boolean delDicAreaInfo(Map<String, Object> params);

	Map<String, Object> getUpcAreaInfo(Map<String, Object> params);

	boolean delUpcAreaInfo(Map<String, Object> params);

	int saveUpcAreaInfo(Map<String, Object> params);

	int updateUpcAreaInfo(Map<String, Object> params);
	
	public boolean updateDicAreaInfo (Map<String, Object> params);
	
	public int queryUpcUserModel(Map<String, Object> params);
	
	List<RdsJudicialUpcUserModel> getUpcUserInfoById(Map<String, Object> params);
}
