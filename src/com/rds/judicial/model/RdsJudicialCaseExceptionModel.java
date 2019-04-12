package com.rds.judicial.model;

public class RdsJudicialCaseExceptionModel {

	private String exception_id;
	private String case_id;
	private String exception_desc;
	private String exception_type;
	private String exception_time;
	private String handle_time;
	private String exception_type_str;
	private String exception_per;
	private String exception_pername;
	private int is_handle;

	public String getHandle_time() {
		return handle_time;
	}

	public void setHandle_time(String handle_time) {
		this.handle_time = handle_time;
	}

	public String getException_id() {
		return exception_id;
	}

	public void setException_id(String exception_id) {
		this.exception_id = exception_id;
	}

	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}

	public String getException_desc() {
		return exception_desc;
	}

	public void setException_desc(String exception_desc) {
		this.exception_desc = exception_desc;
	}

	public String getException_type() {
		return exception_type;
	}

	public void setException_type(String exception_type) {
		this.exception_type = exception_type;
	}

	public String getException_time() {
		return exception_time;
	}

	public void setException_time(String exception_time) {
		this.exception_time = exception_time;
	}

	public String getException_type_str() {
		return exception_type_str;
	}

	public void setException_type_str(String exception_type_str) {
		this.exception_type_str = exception_type_str;
	}

	public String getException_per() {
		return exception_per;
	}

	public void setException_per(String exception_per) {
		this.exception_per = exception_per;
	}

	public String getException_pername() {
		return exception_pername;
	}

	public void setException_pername(String exception_pername) {
		this.exception_pername = exception_pername;
	}

	public int getIs_handle() {
		return is_handle;
	}

	public void setIs_handle(int is_handle) {
		this.is_handle = is_handle;
	}

}
