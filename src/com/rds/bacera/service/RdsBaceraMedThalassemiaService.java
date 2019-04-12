package com.rds.bacera.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialKeyValueModel;

/**
 * 地中海贫血-service
 * @author Administrator
 *
 */
public interface RdsBaceraMedThalassemiaService extends RdsBaceraBaseService {
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map);
	
	void exportMedThalassemia(Map<String, Object> params,HttpServletResponse response) throws Exception;
	
	public List<RdsJudicialKeyValueModel> queryProgram();

}
