package com.rds.upc.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.rds.upc.model.RdsUpcStResultModel;
import com.rds.upc.model.RdsUpcStatisticslModel;

@Component("RdsUpcStatisticslMapper")
public interface RdsUpcStatisticslMapper extends RdsUpcBaseMapper {
	public List<RdsUpcStatisticslModel> queryAllProvice(Object map) throws Exception;
	public List<RdsUpcStatisticslModel> queryAllCity(Object map) throws Exception;
	public List<RdsUpcStatisticslModel> queryAllCounty(Object map) throws Exception;
	public List<RdsUpcStResultModel> queryAllStatisticsl(Object map) throws Exception;
}
