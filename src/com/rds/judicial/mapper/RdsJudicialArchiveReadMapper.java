package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialArchiveReadModel;

/**
 * 2015年4月27日
 * @author ThinK
 * @description 归档阅读
 */
public interface RdsJudicialArchiveReadMapper {
	int insert(Map<String, Object> params);
	List<RdsJudicialArchiveReadModel> queryAll(Map<String, Object> params);
}
