package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialArchiveModel;
import com.rds.judicial.model.RdsJudicialVerifyCaseInfoModel;
/**
 * @description 归档Mapper
 * @author ThinK
 *  2015年4月27日
 */
public interface RdsJudicialArchiveMapper {
	public int insert(Map<String, Object> params);
	
	public List<RdsJudicialArchiveModel> queryAll(Map<String, Object> params);
	
	public int queryAllCount(Map<String, Object> params);
	
	public int queryMailCount(Map<String, Object> params);

	public List<RdsJudicialVerifyCaseInfoModel> queryAllCase(Map<String, Object> params);

	public int queryAllCaseCount(Map<String, Object> params);
}
