package com.rds.judicial.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.model.RdsJudicialTypeModel;

@Controller
@RequestMapping("judicial/process")
public class RdsJudicialProcessController {
	
	/**
	 * 根据报告编号，查找鉴定事项
	 * @param params
	 * @return
	 */
	@RequestMapping("queryJudicialType")
	@ResponseBody
	public Object queryJudicialType(@RequestBody Map<String, Object> params){
		
		List<Object> l_result = new ArrayList<Object>();
		RdsJudicialTypeModel model = new RdsJudicialTypeModel();
		model.setJudicialTypeCode("000");
		model.setJudicialTypeName("人损");
		l_result.add(model);
		model = new RdsJudicialTypeModel();
		model.setJudicialTypeCode("001");
		model.setJudicialTypeName("三期");
		l_result.add(model);
		return l_result;
	}
	
	/**
	 * 数据录入页面,	输入鉴定编号
	 * @param query
	 * @return
	 */
	@RequestMapping("queryJudicialByCodeForComplate")
	@ResponseBody
	public Object queryJudicialByCodeForComplate(String query){
		List<Object> l_result = new ArrayList<Object>();
		String index = null;
		RdsJudicialKeyValueModel model = null;
		if(query.equals("1")){
			model = new RdsJudicialKeyValueModel();
			index = "111";
			model.setKey(index);
			model.setValue("[2014]法临鉴字第111号;章路峰;");
			l_result.add(model);
		}else if(query.equals("2")) {
			model = new RdsJudicialKeyValueModel();
			index = "222";
			model.setKey(index);
			model.setValue("[2014]法临鉴字第222号;李四;");
			l_result.add(model);
		}
		return l_result;
	}

}
