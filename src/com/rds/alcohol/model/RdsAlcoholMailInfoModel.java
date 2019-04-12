package com.rds.alcohol.model;

public class RdsAlcoholMailInfoModel {
	private String mail_id;
	private String mail_code;
	private String mail_type;
	private String mail_per;
	private String mail_time;
	private String mail_typename;
	

	public String getMail_typename() {
		return mail_typename;
	}

	public void setMail_typename(String mail_typename) {
		this.mail_typename = mail_typename;
	}

	private String case_id;

	public String getMail_id() {
		return mail_id;
	}

	public void setMail_id(String mail_id) {
		this.mail_id = mail_id;
	}

	public String getMail_code() {
		return mail_code;
	}

	public void setMail_code(String mail_code) {
		this.mail_code = mail_code;
	}

	public String getMail_type() {
		return mail_type;
	}

	public void setMail_type(String mail_type) {
		this.mail_type = mail_type;
	}

	public String getMail_per() {
		return mail_per;
	}

	public void setMail_per(String mail_per) {
		this.mail_per = mail_per;
	}

	public String getMail_time() {
		return mail_time;
	}

	public void setMail_time(String mail_time) {
		this.mail_time = mail_time;
	}

	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}

}
