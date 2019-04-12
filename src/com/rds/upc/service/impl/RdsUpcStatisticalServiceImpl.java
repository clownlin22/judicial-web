package com.rds.upc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.upc.mapper.RdsUpcStatisticslMapper;
import com.rds.upc.model.RdsUpcStResultModel;
import com.rds.upc.model.RdsUpcStatisticslModel;
import com.rds.upc.service.RdsUpcStatisticalService;

@Service("rdsUpcStatisticalService")
public class RdsUpcStatisticalServiceImpl implements RdsUpcStatisticalService {
	
	@Setter
	@Autowired
	private RdsUpcStatisticslMapper rdsUpcStatisticslMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return 0;
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return 0;
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return 0;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return 0;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return 0;
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@Override
	public List<RdsUpcStatisticslModel> queryAllProvice(Object map) throws Exception {
		return rdsUpcStatisticslMapper.queryAllProvice(map);
	}
	@Override
	public List<RdsUpcStatisticslModel> queryAllCity(Object map) throws Exception {
		return rdsUpcStatisticslMapper.queryAllCity(map);
	}
	@Override
	public List<RdsUpcStatisticslModel> queryAllCounty(Object map) throws Exception {
		return rdsUpcStatisticslMapper.queryAllCounty(map);
	}

	@Override
	public List<RdsUpcStResultModel> queryAllStatisticsl(Object map)
			throws Exception {
		return rdsUpcStatisticslMapper.queryAllStatisticsl(map);
	}

}
