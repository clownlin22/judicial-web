package com.rds.children.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

public interface RdsChildrenGeneWebService {
	@GET
	@Path("/getChildrenGene/{id_number}/{child_name}/{birth_date}")
	String queryChildGene(
			@PathParam(value = "id_number") String id_number,
			@PathParam(value = "child_name") String child_name,
			@PathParam(value = "birth_date") String birth_date,
			@QueryParam(value = "_jsonp") String callback);
}
