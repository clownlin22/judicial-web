package com.rds.judicial.model;

import org.apache.commons.lang.StringUtils;

public class RdsJudicialAreaInfo {
	private String id;
	private String value;
	private String county;
	private String city;
	private String province;
	private String areaname;
	private String username;
	private String name;
	private String agent;
	private String initials;

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return username+(areaname==null?"":"("+areaname+")")+(StringUtils.isNotEmpty(agent)?"-(代理:"+agent+")":"");
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return province + (city==null?"": ("-" + city)) + (county==null?"":("-" + county));
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

}
