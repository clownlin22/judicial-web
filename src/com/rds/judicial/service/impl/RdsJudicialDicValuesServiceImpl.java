package com.rds.judicial.service.impl;

/**
 * @author chen wei
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.rds.judicial.mapper.RdsJudicialDicValuesMapper;
import com.rds.judicial.model.RdsJudicialAreaInfo;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.service.RdsJudicialDicValuesService;
import com.rds.upc.model.RdsUpcPermitNodeModel;

@Service("RdsJudicialDicValuesService")
public class RdsJudicialDicValuesServiceImpl implements RdsJudicialDicValuesService{
	
	@Setter
	@Autowired
	private RdsJudicialDicValuesMapper RdsJudicialDicValuesMapper;


	/**
	 * 获取模板类型
	 */
	public List<RdsJudicialKeyValueModel> getReportModels(@RequestBody Map<String, Object> params) {

		return RdsJudicialDicValuesMapper.getReportModels(params);
	}
	/**
	 * 获取地区数据
	 */
	public List<RdsJudicialAreaInfo> getAreaInfo(Map<String, Object> params) {
		return RdsJudicialDicValuesMapper.getAreaInfo(params);
	}

	/**
	 * 查询样品类型
	 */
	public List<RdsJudicialKeyValueModel> getSampleType() {
		return RdsJudicialDicValuesMapper.getSampleType();
	}

	/**
	 * 查询样品称谓
	 */
	public List<RdsJudicialKeyValueModel> getSampleCall() {
		return RdsJudicialDicValuesMapper.getSampleCall();
	}
	/**
	 * 获取所有模板类型
	 */
	@Override
	public List<RdsUpcPermitNodeModel> getReportType(String usercode) {
		// TODO Auto-generated method stub
		return RdsJudicialDicValuesMapper.getReportType(usercode);
	}
	
	/**
	 * 获取接收员工
	 */
	@Override
	public 	List<RdsJudicialAreaInfo>  getUpcUsers(Map<String, Object> params) {
		return RdsJudicialDicValuesMapper.getUpcUsers(params);
	}
	/**
	 * 获取单位类型
	 */
	
	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", RdsJudicialDicValuesMapper.queryAllCount(params));
		result.put("data", RdsJudicialDicValuesMapper.queryAllPage(params));
		return result;
	}
	
	@Override
	public Object queryAll(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int update(Object params) throws Exception {
		return RdsJudicialDicValuesMapper.update(params);
	}
	
	@Override
	public int insert(Object params) throws Exception {
		return RdsJudicialDicValuesMapper.insert(params);
	}
	
	@Override
	public int delete(Object params) throws Exception {
		return RdsJudicialDicValuesMapper.delete(params);
	}
	
    /**
     * 获得所有员工
     */
	@Override
	public List<RdsJudicialKeyValueModel> getAllUsers() {
		return RdsJudicialDicValuesMapper.getAllUsers();
	}
	
	@Override
	public List<RdsJudicialKeyValueModel> getUsersId(Map<String, Object> params)
	{
		return RdsJudicialDicValuesMapper.getUsersId(params);
	}
	
	@Override
	public List<RdsJudicialKeyValueModel> getMailModels() {
		return RdsJudicialDicValuesMapper.getMailModels();
	}
	@Override
	public List<Map<String, Object>> getFeeType() {
		return RdsJudicialDicValuesMapper.getFeeType();
	}
	@Override
	public List<RdsJudicialKeyValueModel> getManager(Map<String, Object> params) {
		return RdsJudicialDicValuesMapper.getManager(params);
	}
	@Override
	public List<RdsJudicialKeyValueModel> getCustodyCall() {
		return RdsJudicialDicValuesMapper.getCustodyCall();
	}
	
	@Override
	public List<RdsJudicialKeyValueModel> getUnitTypes() {
		return RdsJudicialDicValuesMapper.getUnitTypes();
	}
	@Override
	public List<RdsJudicialKeyValueModel> getCaseTypes() {
		return RdsJudicialDicValuesMapper.getCaseTypes();
	}
	@Override
	public List<RdsJudicialKeyValueModel> getCaseFeeTypes() {
		return RdsJudicialDicValuesMapper.getCaseFeeTypes();
	}
	@Override
	public List<RdsJudicialKeyValueModel> getReportModelByPartner(
			Map<String, Object> params) {
		return RdsJudicialDicValuesMapper.getReportModelByPartner(params);
	}
	
}
