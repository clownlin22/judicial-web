package com.rds.children.model;

public class RdsChildrenGatherInfoModel {
	private String id;
	private String gather_name;
	private String id_number;
	private String phone;
	private String company_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGather_name() {
		return gather_name;
	}

	public void setGather_name(String gather_name) {
		this.gather_name = gather_name;
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

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public RdsChildrenGatherInfoModel(String id, String gather_name,
			String id_number, String phone, String company_name) {
		super();
		this.id = id;
		this.gather_name = gather_name;
		this.id_number = id_number;
		this.phone = phone;
		this.company_name = company_name;
	}

	public RdsChildrenGatherInfoModel() {
		super();
	}

}
