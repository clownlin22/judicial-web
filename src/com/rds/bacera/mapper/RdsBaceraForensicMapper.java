package com.rds.bacera.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("RdsBaceraForensicMapper")
public interface RdsBaceraForensicMapper extends RdsBaceraBaseMapper {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
}
