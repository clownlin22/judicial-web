package com.rds.children.model;

public class RdsChildrenExceptionModel {

	private String exception_id;
	private String case_id;
	private String case_code;
	private String sample_code;
	private String case_areaname;
	private String case_username;
	private String child_name;
	private String birth_date;
	private int child_sex = 0;
	private String id_number;
	private String gather_time;
	private String exception_desc;
	private String exception_type;
	private String exception_time;
	private String handle_time;
	private String exception_per;
	private String exception_pername;
	private String handle_per;
	private String handle_pername;
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

	public String getCase_code() {
		return case_code;
	}

	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}

	public String getSample_code() {
		return sample_code;
	}

	public void setSample_code(String sample_code) {
		this.sample_code = sample_code;
	}

	public String getCase_areaname() {
		return case_areaname;
	}

	public void setCase_areaname(String case_areaname) {
		this.case_areaname = case_areaname;
	}

	public String getCase_username() {
		return case_username;
	}

	public void setCase_username(String case_username) {
		this.case_username = case_username;
	}

	public String getChild_name() {
		return child_name;
	}

	public void setChild_name(String child_name) {
		this.child_name = child_name;
	}

	public String getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}

	public int getChild_sex() {
		return child_sex;
	}

	public void setChild_sex(int child_sex) {
		this.child_sex = child_sex;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getGather_time() {
		return gather_time;
	}

	public void setGather_time(String gather_time) {
		this.gather_time = gather_time;
	}

	public String getHandle_per() {
		return handle_per;
	}

	public void setHandle_per(String handle_per) {
		this.handle_per = handle_per;
	}

	public String getHandle_pername() {
		return handle_pername;
	}

	public void setHandle_pername(String handle_pername) {
		this.handle_pername = handle_pername;
	}

}
