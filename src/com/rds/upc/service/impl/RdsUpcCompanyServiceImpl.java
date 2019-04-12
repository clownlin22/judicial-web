package com.rds.upc.service.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.upc.mapper.RdsUpcCompanyMapper;
import com.rds.upc.service.RdsUpcCompanyService;

@Service("rdsUpcCompanyService")
public class RdsUpcCompanyServiceImpl extends RdsUpcAbstractServiceImpl implements RdsUpcCompanyService {
	
	@Setter
	@Autowired
	private RdsUpcCompanyMapper rdsUpcCompanyMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsUpcCompanyMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsUpcCompanyMapper.queryAllCount(params));
		result.put("data", rdsUpcCompanyMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsUpcCompanyMapper.update(params);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsUpcCompanyMapper.insert(params);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcCompanyMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

    @Override
    public String queryLaboratoryNo(String companyid) {
        return rdsUpcCompanyMapper.queryLaboratoryNo(companyid);
    }

	@Override
	public boolean verifyId_code(Map<String, Object> params) {
		int flag = rdsUpcCompanyMapper.verifyId_code(params);
		if(null == params.get("companyid") || "".equals(params.get("companyid")))
		{
			return flag>0?true:false;
		}else
		{
			if(flag > 0)
			{
				return false;
			}
			else {

				params.put("companyid", null);
				return rdsUpcCompanyMapper.verifyId_code(params)>0?true:false;
			}
		}
	}
}
