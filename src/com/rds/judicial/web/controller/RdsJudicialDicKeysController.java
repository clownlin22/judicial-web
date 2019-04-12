package com.rds.judicial.web.controller;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.service.RdsJudicialDicKeysService;


@Controller
@RequestMapping("judicial/dickeys")
public class RdsJudicialDicKeysController extends RdsJudicialAbstractController{
	
	@Getter
	@Setter
	private String KEY = "id";

	@Setter
	@Autowired
	private RdsJudicialDicKeysService RdsJudicialDicKeysService;
	
	@Override
	public Integer insert(Map<String, Object> params) throws Exception {
		params.put(KEY, UUIDUtil.getUUID());
		return RdsJudicialDicKeysService.insert(params);
	}

	@Override
	public Integer update(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialDicKeysService.update(params);
	}

	@Override
	public Object delete(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialDicKeysService.delete(params);
	}

	@Override
	public Object queryModel(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAll(@RequestBody Map<String, Object> params) throws Exception{
//		return null;
		return RdsJudicialDicKeysService.queryAll(params);
	}

	@Override
	public Object queryAllPage(@RequestBody Map<String, Object> params) throws Exception {
		return RdsJudicialDicKeysService.queryAllPage(this.pageSet(params));
	}
}
