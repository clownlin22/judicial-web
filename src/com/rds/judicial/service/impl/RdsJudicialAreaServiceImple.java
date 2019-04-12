package com.rds.judicial.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialAreaMapper;
import com.rds.judicial.mapper.RdsJudicialDicValuesMapper;
import com.rds.judicial.model.RdsJudicialAreaInfo;
import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialUpcAreaModel;
import com.rds.judicial.model.RdsJudicialUpcUserModel;
import com.rds.judicial.service.RdsJudicialAreaService;

@Service
public class RdsJudicialAreaServiceImple implements RdsJudicialAreaService {

	@Autowired
	private RdsJudicialAreaMapper rdsJudicialAreaMapper;

	@Setter
	@Autowired
	private RdsJudicialDicValuesMapper rdsJudicialDicValuesMapper;

	/**
	 * 获取采样员信息
	 */
	@Override
	public RdsJudicialResponse getUpcUserInfo(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialUpcUserModel> upcUserModels = rdsJudicialAreaMapper
				.getUpcUserInfo(params);
		response.setItems(upcUserModels);
		int count = rdsJudicialAreaMapper.countUpcUserInfo(params);
		response.setCount(count);
		System.out.println(response.toString());
		return response;
	}

	/**
	 * 保存采样员信息
	 */
	@Override
	public Object saveUpcUserInfo(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		params.put("area_id", UUIDUtil.getUUID());
		int check = rdsJudicialAreaMapper.queryUpcUserModel(params);
		map.put("check", check);
		if (check <= 0) {
			int result = rdsJudicialAreaMapper.insertUpcUserInfo(params);
			map.put("result", result);
		}
		return map;
	}

	/**
	 * 删除采样员信息
	 */
	@Override
	public boolean delUpcUserInfo(Map<String, Object> params) {
		int result = rdsJudicialAreaMapper.delUpcUserInfo(params);
		if (result > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 修改采样员信息
	 */
	@Override
	public Object updateUpcUserInfo(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		int check = rdsJudicialAreaMapper.queryUpcUserModel(params);
		map.put("check", check);
		int result = 0;
		if (check <= 0) {
			List<RdsJudicialUpcUserModel> users = this
					.getUpcUserInfoById(params);
			RdsJudicialUpcUserModel user = users.get(0);
			if (user.getUserid().equals(params.get("userid"))
					&& user.getAreacode().equals(params.get("areacode"))) {
				result = rdsJudicialAreaMapper.updateUpcUserInfo(params);
			} else {
				rdsJudicialAreaMapper.delUpcUserInfo(params);
				params.put("area_id", UUIDUtil.getUUID());
				result = rdsJudicialAreaMapper.insertUpcUserInfo(params);
			}
			map.put("result", result);
		}
		return map;
	}

	/**
	 * 获取地区字典表
	 */
	@Override
	public List<RdsJudicialDicAreaModel> getDicAreaInfo(
			Map<String, Object> params) {
		params.put("parentID",
				params.get("node") == null ? 0 : (params.get("node").toString()
						.equals("root") ? 0 : params.get("node").toString()));
		List<RdsJudicialDicAreaModel> lists = rdsJudicialAreaMapper.getDicArea(params);
		return lists;
		//省
//        List<RdsJudicialDicAreaModel> dicProvinceModels=rdsJudicialAreaMapper.getAllProvince(params);
//
//        //市
//        List<RdsJudicialDicAreaModel> dicCityModels=rdsJudicialAreaMapper.getAllCity(params);
//
//        //区
//        List<RdsJudicialDicAreaModel> dicZoneModels=rdsJudicialAreaMapper.getAllCounty(params);
//        
//        for(RdsJudicialDicAreaModel city:dicCityModels){
//        	for(RdsJudicialDicAreaModel zone:dicZoneModels){
//        		if(zone.getParentId().equals(city.getId())){
//        			city.setLeaf(false);
//        			city.getChildren().add(zone);
//        		}
//        	}
//        }
//        
//        for(RdsJudicialDicAreaModel province:dicProvinceModels){
//        	for(RdsJudicialDicAreaModel city:dicCityModels){
//        		if(city.getParentId().equals(province.getId())){
//        			 province.setLeaf(false);
//        			 province.getChildren().add(city);
//        		}
//        	}
//        }
//        return dicProvinceModels;
	}

	@Override
	public boolean exsitDicAreaCode(Map<String, Object> params) {
		int count = rdsJudicialAreaMapper.exsitDicAreaCode(params);
		if (count > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 添加字典表信息
	 */
	@Override
	public boolean saveDicAreaInfo(Map<String, Object> params) {
		int result = rdsJudicialAreaMapper.saveDicAreaInfo(params);
		if (result > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除地区字典信息
	 */
	@Override
	public boolean delDicAreaInfo(Map<String, Object> params) {
		if (params.get("areacode") != null) {
			int exsit = rdsJudicialAreaMapper.exsitDicAreaInfo(String
					.valueOf(params.get("areacode")));
			if (exsit > 0) {
				return false;
			} else {
				int result = rdsJudicialAreaMapper.delDicAreaInfo(params);
				if (result > 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取用户字典表地区信息
	 */
	@Override
	public Map<String,Object> getUpcAreaInfo(
			Map<String, Object> params) {
		List<RdsJudicialUpcAreaModel> list = rdsJudicialAreaMapper.getUpcAreaInfo(params);
		int count = rdsJudicialAreaMapper.countAreaInfo(params);
		params.clear();
		params.put("data", list);
		params.put("total", count);
		return params;
	}

	/**
	 * 删除用户字段表地区信息
	 */
	@Override
	public boolean delUpcAreaInfo(@RequestBody Map<String, Object> params) {
		int result = rdsJudicialAreaMapper.delUpcAreaInfo(params);
		if (result > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 保存用户字段表地区信息
	 */
	@Override
	public int saveUpcAreaInfo(Map<String, Object> params) {
		int result = -1;
		try {
			params.put("areacode", UUIDUtil.getUUID());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("area_code", params.get("code"));
			List<RdsJudicialAreaInfo> list = (List<RdsJudicialAreaInfo>) rdsJudicialDicValuesMapper
					.getAreaInfo(map);
			params.put("areaname", list.get(0).getValue());
			int check = rdsJudicialAreaMapper.countAreaInfo(params);
			if (check > 0)
				result = 0;
			else
				result = rdsJudicialAreaMapper.saveUpcAreaInfo(params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 修改用户字段表地区信息
	 */
	@Override
	public int updateUpcAreaInfo(Map<String, Object> params) {
		int result = -1;
		try {
			int check = rdsJudicialAreaMapper.countAreaInfo(params);
			if (check > 0)
				result = 0;
			else
				result = rdsJudicialAreaMapper.updateUpcAreaInfo(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 地区字典
	 */
	@Override
	public boolean updateDicAreaInfo(Map<String, Object> params) {
		int result = rdsJudicialAreaMapper.updateDicAreaInfo(params);
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public int queryUpcUserModel(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return rdsJudicialAreaMapper.queryUpcUserModel(params);
	}

	@Override
	public List<RdsJudicialUpcUserModel> getUpcUserInfoById(
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return rdsJudicialAreaMapper.getUpcUserInfoById(params);
	}

}
