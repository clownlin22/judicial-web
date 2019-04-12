package com.rds.judicial.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class RdsJudicialCaseProcessModel {
	private String case_code;
	private String accept_time;
	private String sample_in_time;
	private String client;
	private String phone;
	private String ASSIGNEE_;
	private String NAME_;
	private String START_TIME_;
	private String END_TIME_;
	private String MESSAGE_;
	public String getCase_code() {
		return case_code;
	}
	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}
	public String getAccept_time() {
		return accept_time;
	}
	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}
	public String getSample_in_time() {
		return sample_in_time;
	}
	public void setSample_in_time(String sample_in_time) {
		this.sample_in_time = sample_in_time;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getASSIGNEE_() {
		return ASSIGNEE_;
	}
	public void setASSIGNEE_(String aSSIGNEE_) {
		ASSIGNEE_ = aSSIGNEE_;
	}
	public String getNAME_() {
		return NAME_;
	}
	public void setNAME_(String nAME_) {
		NAME_ = nAME_;
	}
	public String getSTART_TIME_() {
		return START_TIME_;
	}
	public void setSTART_TIME_(String sTART_TIME_) {
		START_TIME_ = sTART_TIME_;
	}
	public String getEND_TIME_() {
		return END_TIME_;
	}
	public void setEND_TIME_(String eND_TIME_) {
		END_TIME_ = eND_TIME_;
	}
	public String getMESSAGE_() {
		return MESSAGE_;
	}
	public void setMESSAGE_(String mESSAGE_) {
		MESSAGE_ = mESSAGE_;
	}
	
}
