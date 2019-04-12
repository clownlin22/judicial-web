package com.rds.judicial.service.impl;

/**
 * @author chen wei
 */
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.judicial.mapper.RdsJudicialReportTypeMapper;
import com.rds.judicial.service.RdsJudicialReportTypeService;

@Service("RdsJudicialReportTypeService")
public class RdsJudicialReportTypeServiceImpl implements RdsJudicialReportTypeService {
	
	@Setter
	@Autowired
	private RdsJudicialReportTypeMapper RdsJudicialReportTypeMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialReportTypeMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialReportTypeMapper.queryAllPage(params);
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialReportTypeMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialReportTypeMapper.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialReportTypeMapper.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialReportTypeMapper.delete(params);
	}

}
