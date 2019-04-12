package com.rds.upc.service;

import java.util.List;

import com.rds.upc.model.RdsUpcStResultModel;
import com.rds.upc.model.RdsUpcStatisticslModel;


public interface RdsUpcStatisticalService extends RdsUpcBaseService {
	public List<RdsUpcStatisticslModel> queryAllProvice(Object map) throws Exception;
	public List<RdsUpcStatisticslModel> queryAllCity(Object map) throws Exception;
	public List<RdsUpcStatisticslModel> queryAllCounty(Object map) throws Exception;
	public List<RdsUpcStResultModel> queryAllStatisticsl(Object map) throws Exception;
}
