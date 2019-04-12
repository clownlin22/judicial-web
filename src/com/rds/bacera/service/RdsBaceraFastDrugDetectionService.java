package com.rds.bacera.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.judicial.model.RdsJudicialKeyValueModel;

public interface RdsBaceraFastDrugDetectionService extends RdsBaceraBaseService {
		public int queryNumExit(@SuppressWarnings("rawtypes") Map map);
		void exportFastDrugDetection( Map<String, Object> params,HttpServletResponse response) throws Exception;
		
		public List<RdsJudicialKeyValueModel> queryProgram();
		

	}


