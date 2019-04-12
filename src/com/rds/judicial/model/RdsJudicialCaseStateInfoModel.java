package com.rds.judicial.model;


public class RdsJudicialCaseStateInfoModel {

	//案例id
	private String case_id;
	//案例编号
	private String case_code;
	//案例审核状态
	private int verify_state = -1;
	private String accept_time;
	private String close_time;
	private String sample_in_time;
	private String phone;
	private String client;
	private String is_delete;
	private String id_number;
	private String mail_code;
	private String fandm;
	private String child;
	private String username;
	private String process_instance_id;
	
	public String getCase_id() {
		return case_id;
	}
	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}
	public String getCase_code() {
		return case_code;
	}
	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}
	public int getVerify_state() {
		return verify_state;
	}
	public void setVerify_state(int verify_state) {
		this.verify_state = verify_state;
	}
	public String getAccept_time() {
		return accept_time;
	}
	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}
	public String getClose_time() {
		return close_time;
	}
	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}
	public String getSample_in_time() {
		return sample_in_time;
	}
	public void setSample_in_time(String sample_in_time) {
		this.sample_in_time = sample_in_time;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}
	public String getId_number() {
		return id_number;
	}
	public void setId_number(String id_number) {
		this.id_number = id_number;
	}
	public String getMail_code() {
		return mail_code;
	}
	public void setMail_code(String mail_code) {
		this.mail_code = mail_code;
	}
	public String getFandm() {
		return fandm;
	}
	public void setFandm(String fandm) {
		this.fandm = fandm;
	}
	public String getChild() {
		return child;
	}
	public void setChild(String child) {
		this.child = child;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProcess_instance_id() {
		return process_instance_id;
	}
	public void setProcess_instance_id(String process_instance_id) {
		this.process_instance_id = process_instance_id;
	}
	
}
