package com.rds.upc.service.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.upc.mapper.RdsUpcModuleMapper;
import com.rds.upc.service.RdsUpcModuleService;

@Service("rdsUpcModuleService")
public class RdsUpcModuleServiceImpl implements RdsUpcModuleService {
	
	@Setter
	@Autowired
	private RdsUpcModuleMapper rdsUpcModuleMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcModuleMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcModuleMapper.queryModel(params);
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcModuleMapper.queryAllPage(params);
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcModuleMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcModuleMapper.update(params);
	}

	@Override
	public int insert(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcModuleMapper.insert(params);
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		String moduelCodesTemp = rdsUpcModuleMapper.queryChildInfo(params);
		String codes[] = moduelCodesTemp.split(",");
		StringBuffer moduelCodes = new StringBuffer();
		for(int i = 0 ; i < codes.length ; i ++)
		{
			if(StringUtils.isNotEmpty(codes[i]))
			{
				moduelCodes.append("'"+codes[i]+"',");
			}
		}
		String moduelCode = moduelCodes.substring(0, moduelCodes.length()-1);
		Map<String, Object> mapParams = new HashMap<String, Object>();
		mapParams.put("moduelCodes", moduelCode);
		return rdsUpcModuleMapper.delete(mapParams);
	}

	@Override
	public Object queryAllByParent(Map<String, Object> params) throws Exception {
		return rdsUpcModuleMapper.queryAllByParent(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
