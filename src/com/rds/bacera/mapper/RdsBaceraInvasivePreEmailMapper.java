package com.rds.bacera.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;


@Component("RdsBaceraInvasivePreEmailMapper")
public interface RdsBaceraInvasivePreEmailMapper {
	public int insert(Map<String, Object>params) throws Exception;
	public List<Object> queryEmail(Map<String, Object> params) throws Exception;
}
