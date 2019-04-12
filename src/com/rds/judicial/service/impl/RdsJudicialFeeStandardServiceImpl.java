package com.rds.judicial.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.code.utils.JScriptInvoke;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialFeeQuationMapper;
import com.rds.judicial.mapper.RdsJudicialFeeTypeMapper;
import com.rds.judicial.model.RdsJudicialFeeQuationModel;
import com.rds.judicial.model.RdsJudicialFeeTypeModel;
import com.rds.judicial.service.RdsJudicialFeeStandardService;

@Service("RdsJudicialFeeStandardService")
public class RdsJudicialFeeStandardServiceImpl implements
		RdsJudicialFeeStandardService {

//	/**
//	 * 亲子司法
//	 */
//	private static final Integer PATERNITYJUDICIAL = 0;
//	/**
//	 * 亲子医学
//	 */
//	private static final Integer PATERNITYMEDICAL = 1;
	@Setter
	@Autowired
	private RdsJudicialFeeQuationMapper fqMapper;

	@Setter
	@Autowired
	private RdsJudicialFeeTypeMapper feeTypeMapper;

	@Setter
	@Autowired
	private RdsJudicialFeeQuationMapper equationMapper;

	@Override
	@Transactional
	public Map<String, Object> saveEquation(Map<String, Object> params) {
		String equations = (String) params.get("equation");
		try {
			JScriptInvoke.getStandardFee(equations.replaceAll("\n", ""), 1, 1);
		} catch (Exception e) {
			params.clear();
			params.put("result", false);
			params.put("message", "新增失败，公式有误。");
			return params;
		}
		params.put("areaid", params.get("area_id"));
		params.put("case_type",(Integer) params.get("feetype"));
		List<RdsJudicialFeeQuationModel> equation = equationMapper
				.getEquation(params);
		if (equation.size() > 0) {
			params.clear();
			params.put("result", false);
			params.put("message", "新增失败，该地区已配置公式！");
		} else {
			RdsJudicialFeeQuationModel feeQuation = new RdsJudicialFeeQuationModel();
			RdsJudicialFeeTypeModel feeTypeModel = new RdsJudicialFeeTypeModel();
			String type_id = UUIDUtil.getUUID();
			feeTypeModel.setArea_id((String) params.get("area_id"));
			feeTypeModel.setType_id(type_id);
			feeTypeModel.setFeetype((Integer) params.get("feetype"));
			feeQuation.setId(UUIDUtil.getUUID());
			feeQuation.setType_id(type_id);
			feeQuation.setDiscountrate(params
					.get("discountrate").toString());
			feeQuation.setEquation(equations);
			if (fqMapper.insertQuation(feeQuation) > 0
					&& feeTypeMapper.insertType(feeTypeModel) > 0) {
				params.clear();
				params.put("result", true);
				params.put("message", "新增成功");
			} else {
				params.clear();
				params.put("result", false);
				params.put("message", "新增失败，请联系管理员！");
			}
		}
		return params;
	}


	@Override
	@Transactional
	public Map<String, Object> updateEquation(Map<String, Object> params) {
		String equations = (String) params.get("equation");
		try {
			JScriptInvoke.getStandardFee(equations.replaceAll("\n", ""), 1, 1);
			RdsJudicialFeeQuationModel feeQuation = new RdsJudicialFeeQuationModel();
			feeQuation.setType_id(params.get("type_id").toString());
			feeQuation.setEquation(equations);
			System.out.println(params
					.get("discountrate").toString());
			feeQuation.setDiscountrate(params
					.get("discountrate").toString());
			equationMapper.updateFeetype(params);
			equationMapper.updateEquation(params);
			params.clear();
			params.put("result", true);
			params.put("message", "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			params.clear();
			params.put("result", false);
			params.put("message", "新增失败，公式有误。");
		}
		return params;
	}

	@Override
	public Map<String, Object> queryType(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", fqMapper.queryType(params));
		map.put("total", fqMapper.queryTypeCount(params));
		return map;
	}

	@Override
	@Transactional
	public Map<String, Object> delete(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (fqMapper.delete(id) > 0) {
			map.put("result", true);
			map.put("message", "删除成功");
		} else {
			map.put("result", false);
			map.put("message", "删除失败");
		}
		return map;
	}
}
