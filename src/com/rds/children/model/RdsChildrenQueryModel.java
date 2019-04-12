package com.rds.children.model;

public class RdsChildrenQueryModel {
	private String id_number;
	private String child_name;
	private String birth_date;
	public String getId_number() {
		return id_number;
	}
	public void setId_number(String id_number) {
		this.id_number = id_number;
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
	
	@Override
	public String toString() {
		return "RdsChildrenQueryModel [id_number=" + id_number
				+ ", child_name=" + child_name + ", birth_date=" + birth_date
				+ "]";
	}
	
	public RdsChildrenQueryModel() {
		super();
	}
	public RdsChildrenQueryModel(String id_number, String child_name,
			String birth_date) {
		super();
		this.id_number = id_number;
		this.child_name = child_name;
		this.birth_date = birth_date;
	}
}
