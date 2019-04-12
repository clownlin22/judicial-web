package com.rds.judicial.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.model.RdsJudicialStandardModel;

@Controller
@RequestMapping("judicial/standard")
public class RdsJudicialStandardController {

	/**
	 * 鉴定标准自动匹配查询
	 * 
	 * @return
	 */
	@RequestMapping("queryJudicialStandardComplate")
	@ResponseBody
	public Object queryJudicialStandardComplate(String query,String judicialtype){
		List<Object> l_result = new ArrayList<Object>();
		RdsJudicialStandardModel model = null;
		String index = null;
		
		if(query.contains("器官缺失")){
			model = new RdsJudicialStandardModel();
			index = "00000";
			model.setStdid(index+"stdid");
			model.setStdname("1．5．1(1)器官缺失或功能完全丧失，其他器官不能代偿；");
			model.setStdlevel("一级");
			l_result.add(model);
		}else if (query.contains("器官严重缺损")) {
			model = new RdsJudicialStandardModel();
			index = "10000";
			model.setStdid(index+"stdid");
			model.setStdname("1．5．2(1)器官严重缺损或畸形，有严重功能障碍或并发症；");
			model.setStdlevel("二级");
			l_result.add(model);
		}
		return l_result;
	}
}
