package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * 肿瘤个体-service
 * @author Administrator
 *
 */
public interface RdsBaceraTumorPerService extends RdsBaceraBaseService {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	void exportTumorPer(Map<String, Object> params,HttpServletResponse response) throws Exception;
	public Object queryTumorPerItems(Object params) throws Exception;
	
	public Object queryTumorPerItemsAll(Object params) throws Exception;
	
	public int countTumorPerItems(Object params) throws Exception;
	
	public int saveTumorPerItems(Object params) throws Exception;
	
	public int updateTumorPerItems(Object params) throws Exception;
	
	public int deleteTumorPerItems(Object params) throws Exception;
}
