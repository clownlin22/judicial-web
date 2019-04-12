package com.rds.alcohol.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.rds.alcohol.model.RdsAlcoholIdentifyCaseinfo;
import com.rds.alcohol.model.RdsAlcoholIdentifyModel;
import com.rds.alcohol.model.RdsAlcoholResponse;

public interface RdsAlcoholIdentifyMapper {

	List<RdsAlcoholIdentifyModel> getIdentifyInfo(Map<String, Object> params);

	int getcount(Map<String, Object> params);

	Integer insertinfo( Map<String, Object> params);

	int update(Map<String, Object> params);

	int delete(Map<String, Object> params);

	int exsitper_code(Object per_code);

	int exsitper_name(Object per_name);

	String selectper_id(String case_checkper);

	int add(RdsAlcoholIdentifyCaseinfo ps);

	void deleteCaseIdetity(String case_id);


}
