package com.rds.judicial.web.controller;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.service.RdsJudicialBankService;

/**
 * @description 银行账户管理
 * @author fushaoming 2015年4月29日
 *
 */
@Controller
@RequestMapping("judicial/bank")
public class RdsJudicialBankController extends RdsJudicialAbstractController {

	@Getter
	@Setter
	private String KEY = "id";

	@Setter
	@Autowired
	private RdsJudicialBankService bankService;

	@Override
	public Integer insert(Map<String, Object> params) throws Exception {
		params.put(KEY, UUIDUtil.getUUID());
		return bankService.insert(params);
	}

	@Override
	public Integer update(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return bankService.update(params);
	}

	@Override
	public Object delete(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return bankService.delete(params);
	}

	@Override
	public Object queryModel(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAll(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		return bankService.queryAllPage(this.pageSet(params));
	}
	
	@RequestMapping("getBankname")
	@ResponseBody
	public List<Map<String, Object>> getBankname(){
		return bankService.getBankname();
	}
	
	@RequestMapping("getCompany")
	@ResponseBody
	public List<Map<String, Object>> getCompany(){
		return bankService.getCompany();
	}
}
