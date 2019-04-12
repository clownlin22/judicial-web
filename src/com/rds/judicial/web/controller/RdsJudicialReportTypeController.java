package com.rds.judicial.web.controller;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.service.RdsJudicialReportTypeService;


@Controller
@RequestMapping("judicial/reporttype")
public class RdsJudicialReportTypeController extends RdsJudicialAbstractController{
	
	@Getter
	@Setter
	private String KEY = "typeid";

	@Setter
	@Autowired
	private RdsJudicialReportTypeService RdsJudicialReportTypeService;
	
	@Override
	public Integer insert(@RequestBody Map<String, Object> params) throws Exception {
		params.put(KEY, UUIDUtil.getUUID());
		return RdsJudicialReportTypeService.insert(params);
	}

	@Override
	public Integer update(@RequestBody Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialReportTypeService.update(params);
	}

	@Override
	public Object delete(@RequestBody Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return RdsJudicialReportTypeService.delete(params);
	}

	@Override
	public Object queryModel(@RequestBody Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAll(@RequestBody Map<String, Object> params) throws Exception{
		String node = params.get("node")==null?"":params.get("node").toString();
		if(!"root".equals(node))
		{
			params.put("parentid", node);
		}
		return RdsJudicialReportTypeService.queryAll(params);
	}

	@Override
	public Object queryAllPage(@RequestBody Map<String, Object> params) throws Exception {
		return RdsJudicialReportTypeService.queryAllPage(this.pageSet(params));
	}
	
	

}
