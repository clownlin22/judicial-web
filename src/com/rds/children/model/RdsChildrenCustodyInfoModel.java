package com.rds.children.model;

public class RdsChildrenCustodyInfoModel {
	private String custody_id;
	private String custody_name;
	private String custody_call;
	private String custody_callname;
	private String id_number;
	private String phone;
	private String case_id;

	public String getCustody_id() {
		return custody_id;
	}

	public void setCustody_id(String custody_id) {
		this.custody_id = custody_id;
	}

	public String getCustody_name() {
		return custody_name;
	}

	public void setCustody_name(String custody_name) {
		this.custody_name = custody_name;
	}

	public String getCustody_call() {
		return custody_call;
	}

	public void setCustody_call(String custody_call) {
		this.custody_call = custody_call;
	}

	public String getCustody_callname() {
		return custody_callname;
	}

	public void setCustody_callname(String custody_callname) {
		this.custody_callname = custody_callname;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}

	public RdsChildrenCustodyInfoModel(String custody_id, String custody_name,
			String custody_call, String id_number, String phone, String case_id) {
		super();
		this.custody_id = custody_id;
		this.custody_name = custody_name;
		this.custody_call = custody_call;
		this.id_number = id_number;
		this.phone = phone;
		this.case_id = case_id;
	}

	public RdsChildrenCustodyInfoModel() {
		super();
	}

}
