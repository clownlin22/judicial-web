package com.rds.bacera.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("RdsBaceraFolicAcidMapper")
public interface RdsBaceraFolicAcidMapper extends RdsBaceraBaseMapper {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
}
