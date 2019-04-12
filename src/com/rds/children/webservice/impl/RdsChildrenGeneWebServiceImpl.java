package com.rds.children.webservice.impl;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.children.model.RdsChildrenQueryModel;
import com.rds.children.service.RdsChildrenGeneQueryService;
import com.rds.children.webservice.RdsChildrenGeneWebService;
@Service("com.rds.children.webservice.RdsChildrenGeneWebService")
public class RdsChildrenGeneWebServiceImpl implements RdsChildrenGeneWebService{

	@Autowired
	private RdsChildrenGeneQueryService queryService;
	@Override
	public String queryChildGene(String id_number,
			String child_name, String birth_date,String callback) {
		RdsChildrenQueryModel queryModel = new RdsChildrenQueryModel(id_number,child_name,birth_date);
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		try {
			result =  mapper.writeValueAsString(queryService.getGenInfo(queryModel));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return callback+"("+result+")";
	}

}
