package com.rds.children.service;

import java.util.Map;

import com.rds.children.model.RdsChildrenQueryModel;

public interface RdsChildrenGeneQueryService {

	Map<String, Object> getGenInfo(RdsChildrenQueryModel queryModel);
	
//	@Produces("application/json")
//	@GET
//	@Path("/getChildrenGene/{id_number}/{child_name}/{birth_date}")
//	Map<String, Object> queryChildGene(
//			@PathParam(value = "id_number") String id_number,
//			@PathParam(value = "child_name") String child_name,
//			@PathParam(value = "birth_date") String birth_date)
//			throws JsonProcessingException;
}
