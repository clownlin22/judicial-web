package com.rds.bacera.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("RdsBaceraProgramMapper")
public interface RdsBaceraProgramMapper extends RdsBaceraBaseMapper {
	
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);

}
