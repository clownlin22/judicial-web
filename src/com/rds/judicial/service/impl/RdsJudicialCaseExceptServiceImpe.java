package com.rds.judicial.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.SimpleDateFormat;
import com.rds.judicial.mapper.RdsJudicialCaseStatusMapper;
import com.rds.judicial.mapper.RdsJudicialPhoneMapper;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.model.RdsJudicialCaseExceprModel;
import com.rds.judicial.service.RdsJudicialCaseExceptService;

@Service("RdsJudicialCaseExceptService")
public class RdsJudicialCaseExceptServiceImpe implements
		RdsJudicialCaseExceptService {

	@Setter
	@Autowired
	private RdsJudicialCaseStatusMapper csMapper;
	@Setter
	@Autowired
	private RdsJudicialRegisterMapper rMapper;

	@Setter
	@Autowired
	private RdsJudicialPhoneMapper pMapper;

	@Override
	@Transactional
	public Map<String, Object> save(Map<String, Object> params) {
		RdsJudicialCaseExceprModel csModel = new RdsJudicialCaseExceprModel();
		if (!"".equals(params.get("case_id")) && params.get("case_id") != null) {
			if (csMapper.update(params) > 0) {
				params.clear();
				params.put("success", true);
				params.put("message", "更新成功");
			} else {
				params.clear();
				params.put("success", false);
				params.put("message", "更新失败");
			}

		} else {
			String case_id = pMapper
					.getCaseID((String) params.get("case_code"));
			if (csMapper.getCaseid(case_id) > 0) {
				params.clear();
				params.put("success", false);
				params.put("message", "案例已经存在无法新增");
				return params;
			} else {
				csModel.setCase_id(case_id);
				csModel.setStatus(1);
				csModel.setStatusmessage((String) params.get("statusmessage"));
				csModel.setDate(new SimpleDateFormat("yyyy-MM-dd")
						.format(new Date()));
				if (csMapper.insert(csModel) > 0) {
					params.clear();
					params.put("success", true);
					params.put("message", "新增成功");
				} else {
					params.clear();
					params.put("success", false);
					params.put("message", "新增失败");
				}
			}
		}
		return params;
	}

	@Override
	public Map<String, Object> queryAll(Map<String, Object> params) {
		List<Map<String, Object>> list = csMapper.queryAll(params);
		int count = csMapper.queryAllCount(params);
		params.clear();
		params.put("data", list);
		params.put("total", count);
		return params;
	}

	@Override
	public Map<String, Object> setNormal(Map<String, Object> params) {
		if (csMapper.setNormal(params) > 0) {
			params.clear();
			params.put("success", true);
			params.put("message", "修改成功");
		} else {
			params.clear();
			params.put("success", false);
			params.put("message", "修改失败");
		}
		return params;
	}

}
