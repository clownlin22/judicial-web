package com.rds.bacera.model;

public class RdsBaceraFastDrugDetectionModel {
	private String id;
	private String num;
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	private String address;
	private String date;
	private String person;
	private String reagent_type;
	private String reagent_count;
	private String remark;
	private String cancelif;
	private String trial_type;
	private String input;
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input= input;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getReagent_type() {
		return reagent_type;
	}
	public void setReagent_type(String reagent_type) {
		this.reagent_type = reagent_type;
	}
	public String getReagent_count() {
		return reagent_count;
	}
	public void setReagent_count(String reagent_count) {
		this.reagent_count = reagent_count;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCancelif() {
		return cancelif;
	}
	public void setCancelif(String cancelif) {
		this.cancelif = cancelif;
	}
	public String getTrial_type() {
		return trial_type;
	}
	public void setTrial_type(String trial_type) {
		this.trial_type = trial_type;
	}

}
