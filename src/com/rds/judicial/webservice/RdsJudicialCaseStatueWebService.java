package com.rds.judicial.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * 查询案例状态ws接口
 * 
 * @author 少明
 *
 */
public interface RdsJudicialCaseStatueWebService {
	@GET
	// @Path("/queryCaseProcessStatus/{casecode}/{other}")
	@Path("/queryCaseProcessStatus")
	public String queryCaseProcessStatus(
			@QueryParam(value = "casecode") String casecode,
			@QueryParam(value = "other") String other,
			@QueryParam(value = "_jsonp") String callback);

	@GET
	@Path("/queryCaseProcessStatus/{identitycard}")
	public String queryCaseProcessStatus(
			@PathParam(value = "identitycard") String identitycard,
			String callback);
}
