package com.rds.judicial.model;


public class RdsJudicialUpcAreaModel {
	private String id;
	private String text;
	private String areacode;
	private String areaname;
	private String address;
	private int print_copies=0;
	private int is_delete;
	private boolean leaf = true;
	private String code;
	private String achieve;
	private String phone;

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public int getPrint_copies() {
		return print_copies;
	}

	public void setPrint_copies(int print_copies) {
		this.print_copies = print_copies;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getId() {
		return areacode;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return areaname+((achieve==null||"".equals(achieve))?"":"("+achieve+")");
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAchieve() {
		return achieve;
	}

	public void setAchieve(String achieve) {
		this.achieve = achieve;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
