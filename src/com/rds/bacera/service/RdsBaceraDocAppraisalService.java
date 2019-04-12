package com.rds.bacera.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialKeyValueModel;

/**
 * 文书鉴定-service
 * @author Administrator
 *
 */
public interface RdsBaceraDocAppraisalService extends RdsBaceraBaseService {
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map);
	
	void exportDocAppraisal(Map<String, Object> params,HttpServletResponse response) throws Exception;
	
	public List<RdsJudicialKeyValueModel> queryProgram();
	
	public boolean insertFinanceTax(Map<String, Object> params);
	
	public boolean updateFinanceTax(Map<String, Object> params);

}
