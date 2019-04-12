package com.rds.children.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.children.mapper.RdsChildrenTariffMapper;
import com.rds.children.model.RdsChildrenTariffModel;
import com.rds.children.service.RdsChildrenTariffService;
import com.rds.code.utils.uuid.UUIDUtil;

/**
 * 套餐管理
 * @author 少明
 *
 */
@Service("RdsChildrenTariffService")
public class RdsChildrenTariffServiceImpl implements RdsChildrenTariffService {

	@Autowired
	private RdsChildrenTariffMapper tMapper;
	
	@Override
	public Map<String, Object> getTariffInfo(Map<String, Object> params) {
		List<RdsChildrenTariffModel> tlist = tMapper.getTariffInfo(params);
		params.put("data",tlist);
		params.put("count",tMapper.getTariffInfoCount(params));
		return params;
	}

	@Override
	public Map<String, Object> save(Map<String, Object> params) {
		String tariff_id = UUIDUtil.getUUID();
		params.put("tariff_id", tariff_id);
		tMapper.save(params);
		params.clear();
		params.put("success", true);
		params.put("message", "新增成功！");
		return params;
	}

	@Override
	public Map<String, Object> delete(Map<String, Object> params) {
		if(tMapper.delete(params)>0){
			params.clear();
			params.put("success", true);
			params.put("message", "作废成功！");
		}else{
			params.put("success", false);
			params.put("message", "作废失败！");
		}
		return params;
	}

}
