package com.rds.children.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.children.model.RdsChildrenQueryModel;
import com.rds.children.service.RdsChildrenGeneQueryService;

@Controller
@RequestMapping("children/info")
public class RdsChildrenGeneInfoController {
	
	@Autowired
	private RdsChildrenGeneQueryService queryService;
	
	@RequestMapping("getChildGene")
	@ResponseBody
	public Map<String, Object> getGeneInfo(RdsChildrenQueryModel queryModel){
		return queryService.getGenInfo(queryModel);
	}
}
