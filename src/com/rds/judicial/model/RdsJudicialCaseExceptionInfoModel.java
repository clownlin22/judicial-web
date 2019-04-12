package com.rds.judicial.model;


public class RdsJudicialCaseExceptionInfoModel {

	private String case_id;
	private String case_code;
	private String client;
	private int fee_status = 0;
	private String agent;
	private int fee_type = 0;
	private String close_time;
	private String mail_time;
	private int is_super_time =0;
	private int is_to_time=0;
	private int per_photo=0;
	private String case_receiver;
	private String receiver_area;
	private String sample_in_time;
	private int verify_state = 0;
	private int is_report = 0;
	private String remittance_id;
	private String accept_time;
	private String fee_id;
	public int getIs_super_time() {
		return is_super_time;
	}

	public int getIs_to_time() {
		return is_to_time;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public void setIs_to_time(int is_to_time) {
		this.is_to_time = is_to_time;
	}

	public int getPer_photo() {
		return per_photo;
	}

	public void setPer_photo(int per_photo) {
		this.per_photo = per_photo;
	}

	public void setIs_super_time(int is_super_time) {
		this.is_super_time = is_super_time;
	}

	public String getCase_receiver() {
		return case_receiver;
	}

	public void setCase_receiver(String case_receiver) {
		this.case_receiver = case_receiver;
	}

	public String getReceiver_area() {
		return receiver_area;
	}

	public void setReceiver_area(String receiver_area) {
		this.receiver_area = receiver_area;
	}

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

	public int getFee_status() {
		return fee_status;
	}

	public void setFee_status(int fee_status) {
		this.fee_status = fee_status;
	}

	public int getFee_type() {
		return fee_type;
	}

	public void setFee_type(int fee_type) {
		this.fee_type = fee_type;
	}

	public String getClose_time() {
		return close_time;
	}

	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}

	public String getMail_time() {
		return mail_time;
	}

	public void setMail_time(String mail_time) {
		this.mail_time = mail_time;
	}

	public String getSample_in_time() {
		return sample_in_time;
	}

	public void setSample_in_time(String sample_in_time) {
		this.sample_in_time = sample_in_time;
	}
	public int getVerify_state() {
		return verify_state;
	}

	public void setVerify_state(int verify_state) {
		this.verify_state = verify_state;
	}

	public int getIs_report() {
		return is_report;
	}

	public void setIs_report(int is_report) {
		this.is_report = is_report;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getRemittance_id() {
		return remittance_id;
	}

	public void setRemittance_id(String remittance_id) {
		this.remittance_id = remittance_id;
	}

	public String getAccept_time() {
		return accept_time;
	}

	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}

	public String getFee_id() {
		return fee_id;
	}

	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}
	
}
