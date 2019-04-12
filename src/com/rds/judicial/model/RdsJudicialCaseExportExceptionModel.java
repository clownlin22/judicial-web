package com.rds.judicial.model;

public class RdsJudicialCaseExportExceptionModel {

	private String case_code;
	private String exception_desc;
	private String username;
	private String client;
	private String exception_time;
	private String accept_time;
	private String agent;
	public String getCase_code() {
		return case_code;
	}
	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}
	public String getException_desc() {
		return exception_desc;
	}
	public void setException_desc(String exception_desc) {
		this.exception_desc = exception_desc;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getException_time() {
		return exception_time;
	}
	public void setException_time(String exception_time) {
		this.exception_time = exception_time;
	}
	public String getAccept_time() {
		return accept_time;
	}
	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}

}
