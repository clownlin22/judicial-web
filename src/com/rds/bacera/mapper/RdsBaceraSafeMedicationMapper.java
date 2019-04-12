package com.rds.bacera.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("RdsBaceraSafeMedicationMapper")
public interface RdsBaceraSafeMedicationMapper extends RdsBaceraBaseMapper {
	@SuppressWarnings("rawtypes")
	public int queryAudltMaxNum(Map map);
	@SuppressWarnings("rawtypes")
	public int queryChildMaxNum(Map map);
}
