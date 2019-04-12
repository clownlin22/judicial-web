package com.rds.upc.service.impl;

import org.apache.log4j.Logger;

import com.rds.upc.model.RdsUpcMessageModel;
import com.rds.upc.service.RdsUpcBaseService;

public abstract class RdsUpcAbstractServiceImpl implements RdsUpcBaseService {
	
	public RdsUpcMessageModel model = new RdsUpcMessageModel();
	
	public Logger logger = Logger.getLogger(this.getClass());

	@Override
	public abstract Object queryAll(Object params) throws Exception;

	@Override
	public abstract Object queryModel(Object params) throws Exception;

	@Override
	public abstract Object queryAllPage(Object params) throws Exception;

	@Override
	public abstract int queryAllCount(Object params) throws Exception;

	@Override
	public abstract int update(Object params) throws Exception;

	@Override
	public abstract int insert(Object params) throws Exception;

	@Override
	public abstract int delete(Object params) throws Exception;
	
	@Override
	public abstract int updateJunior(Object params) throws Exception;

}
