package com.rds.bacera.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("RdsBaceraHpvMapper")
public interface RdsBaceraHpvMapper extends RdsBaceraBaseMapper {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
}
