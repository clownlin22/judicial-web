package com.rds.bacera.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("RdsBaceraTumorSusMapper")
public interface RdsBaceraTumorSusMapper extends RdsBaceraBaseMapper {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
}
