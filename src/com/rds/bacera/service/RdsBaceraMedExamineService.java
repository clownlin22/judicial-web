package com.rds.bacera.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialKeyValueModel;

/**
 * 医学检测-service
 * @author Administrator
 *
 */
public interface RdsBaceraMedExamineService extends RdsBaceraBaseService {
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map);
	void exportMedExamine(Map<String, Object> params,HttpServletResponse response) throws Exception;
	
	public List<RdsJudicialKeyValueModel> queryProgram();
	
	public Map<String,String> queryNotify();

}
