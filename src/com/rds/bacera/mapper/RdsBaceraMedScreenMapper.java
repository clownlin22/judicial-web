package com.rds.bacera.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rds.judicial.model.RdsJudicialKeyValueModel;

@Component("RdsBaceraMedScreenMapper")
public interface RdsBaceraMedScreenMapper extends RdsBaceraBaseMapper {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	
	public int queryProgramCount(Map<String, Object> params);
	
	public int insertProgram(Map<String, Object> params);
	
	public List<RdsJudicialKeyValueModel> queryProgram();
}
