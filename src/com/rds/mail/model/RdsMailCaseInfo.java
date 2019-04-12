package com.rds.mail.model;

public class RdsMailCaseInfo {
 
	private String case_id;
	private String case_code;
	private String case_time;
	private String client;
	private String fee_remark;
	private int fee_status;
	private String mail_codes;
	private String fee_date;
	private int fee_type;
	private String sample_str;
	private String receiver_name;
	private String areaname;
	private int mail_state;
	private String case_type;
	private String case_type_str;
	private int mail_count;
	
	public String getCase_id() {
		return case_id;
	}
	public String getCase_type() {
		return case_type;
	}
	public void setCase_type(String case_type) {
		this.case_type = case_type;
	}
	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}
	public String getCase_type_str() {
		return case_type_str;
	}
	public void setCase_type_str(String case_type_str) {
		this.case_type_str = case_type_str;
	}
	public String getCase_code() {
		return case_code;
	}
	public String getCase_time() {
		return case_time;
	}
	public void setCase_time(String case_time) {
		this.case_time = case_time;
	}
	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getFee_remark() {
		return fee_remark;
	}
	public void setFee_remark(String fee_remark) {
		this.fee_remark = fee_remark;
	}
	public int getFee_status() {
		return fee_status;
	}
	public String getMail_codes() {
		return mail_codes;
	}
	public void setMail_codes(String mail_codes) {
		this.mail_codes = mail_codes;
	}
	public void setFee_status(int fee_status) {
		this.fee_status = fee_status;
	}
	public String getFee_date() {
		return fee_date;
	}
	public void setFee_date(String fee_date) {
		this.fee_date = fee_date;
	}
	public int getFee_type() {
		return fee_type;
	}
	public void setFee_type(int fee_type) {
		this.fee_type = fee_type;
	}
	public String getSample_str() {
		return sample_str;
	}
	public void setSample_str(String sample_str) {
		this.sample_str = sample_str;
	}
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public int getMail_state() {
		return mail_state;
	}
	public void setMail_state(int mail_state) {
		this.mail_state = mail_state;
	}
	public int getMail_count() {
		return mail_count;
	}
	public void setMail_count(int mail_count) {
		this.mail_count = mail_count;
	}
	
}
