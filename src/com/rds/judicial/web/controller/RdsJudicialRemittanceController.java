package com.rds.judicial.web.controller;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.service.RdsJudicialRemittanceService;

/**
 * @description 银行账户管理
 * @author yxb
 * @time 2015-05-10
 */
@Controller
@RequestMapping("judicial/remittance")
public class RdsJudicialRemittanceController extends
		RdsJudicialAbstractController {

	@Getter
	@Setter
	private String KEY = "id";

	@Setter
	@Autowired
	private RdsJudicialRemittanceService rdsJudicialRemittanceService;

	@Override
	public Integer insert(Map<String, Object> params) throws Exception {
		params.put(KEY, UUIDUtil.getUUID());
		return rdsJudicialRemittanceService.insert(params);
	}

	@Override
	public Integer update(Map<String, Object> params) throws Exception {
		return rdsJudicialRemittanceService.update(params);
	}

	@Override
	public Object delete(Map<String, Object> params) throws Exception {
		return rdsJudicialRemittanceService.delete(params);
	}

	@Override
	public Object queryModel(Map<String, Object> params) throws Exception {
		return null;
	}

	@Override
	public Object queryAll(Map<String, Object> params) throws Exception {
		return rdsJudicialRemittanceService.queryAll(params);
	}

	@Override
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsJudicialRemittanceService.queryAllPage(this.pageSet(params));
	}
	
	/**
	 * 查重账户是否存在
	 * @param params
	 * @return
	 */
	@RequestMapping("queryAccount")
	@ResponseBody
	public boolean queryAccount(@RequestBody Map<String, Object> params)
			throws Exception {
		return (rdsJudicialRemittanceService.queryExistCount(params)) >= 1 ? true
				: false;
	}
	
	/**
	 * 查重账户是否存在
	 * @param params
	 * @return
	 */
	@RequestMapping("queryAllCount")
	@ResponseBody
	public boolean queryAllCount(@RequestBody Map<String, Object> params)
			throws Exception {
		return (rdsJudicialRemittanceService.queryAllCount(params)) >= 1 ? true
				: false;
	}
}
