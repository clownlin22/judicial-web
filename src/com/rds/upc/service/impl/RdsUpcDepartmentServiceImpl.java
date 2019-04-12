package com.rds.upc.service.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.upc.mapper.RdsUpcDepartmentMapper;
import com.rds.upc.service.RdsUpcDepartmentService;

@Service("rdsUpcDepartmentService")
public class RdsUpcDepartmentServiceImpl extends RdsUpcAbstractServiceImpl implements RdsUpcDepartmentService {
	
	@Setter
	@Autowired
	private RdsUpcDepartmentMapper rdsUpcDepartmentMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return rdsUpcDepartmentMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcDepartmentMapper.queryModel(params);
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsUpcDepartmentMapper.queryAllCount(params));
		result.put("data", rdsUpcDepartmentMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return 0;
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsUpcDepartmentMapper.update(params);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
	}
	@Override
	public int updateJunior(Object params) throws Exception {
		try {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Map<String, Object> map = (Map)params;
			map.put("ids", map.get("deptid"));
			
			
			String idtemp = rdsUpcDepartmentMapper.queryChildInfo(map);
			String id[] = idtemp.split(",");
			String ids="";
			if(id.length>0)
			{
				for(int i = 0 ; i < id.length ; i ++)
				{
					if(StringUtils.isNotEmpty(id[i]))
					{
						ids+= "'" + id[i] + "',";
					}
				}
			}
			ids = ids.substring(0,ids.length()-1);
			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("ids", ids);
			mapParams.put("companyid", map.get("companyid"));
			return rdsUpcDepartmentMapper.updateJunior(mapParams);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsUpcDepartmentMapper.insert(params);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		try {
			//删除每个一级部门，下级部门连带删除
			String[] id = (params.toString()).split(",");
			String ids ="";
			//遍历每个部门
			for(int i =0 ; i < id.length ; i ++)
			{			
				//删除部门id集
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ids", id[i]);
				//获取部门所有下级
				String[] idsTemp = rdsUpcDepartmentMapper.queryChildInfo(map).split(",");
				if(idsTemp.length>0)
				{
					for(int j = 0 ; j < idsTemp.length ; j ++)
					{
						if(StringUtils.isNotEmpty(idsTemp[j]))
						{
							ids+= "'" + idsTemp[j] + "',";
						}
					}
				}
				
			}
			ids = ids.substring(0, ids.length()-1);
			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("ids", ids);
			return rdsUpcDepartmentMapper.delete(mapParams);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
		
	}

	@Override
	public Object queryTreeCombo(Object params) throws Exception{
		try {
			Object result = rdsUpcDepartmentMapper.queryTreeCombo(params);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e);
		}
		
	}

	@Override
	public int queryCountByCode(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsUpcDepartmentMapper.queryCountByCode(params);
	}

	@Override
	public Object queryDepartmentList(Object params) throws Exception {
		return rdsUpcDepartmentMapper.queryDepartmentList(params);
	}

}
