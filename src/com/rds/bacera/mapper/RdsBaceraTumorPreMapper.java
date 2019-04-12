package com.rds.bacera.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("RdsBaceraTumorPreMapper")
public interface RdsBaceraTumorPreMapper extends RdsBaceraBaseMapper {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	
	public List<Object> queryTumorPerItems(Object params) throws Exception;
	
	public int countTumorPerItems(Object params) throws Exception;
	
	public int saveTumorPerItems(Object params) throws Exception;
	
	public int updateTumorPerItems(Object params) throws Exception;
	
	public int deleteTumorPerItems(Object params) throws Exception;
}
