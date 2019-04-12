package com.rds.finance.model;


public class RdsFinanceAscriptionInfo {
	private String id;
	private String areaname;
	private String username;
	private String agentname;
	@SuppressWarnings("unused")
	private String ascription;
	private String type;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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

	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	public String getAscription() {
		String temp="";
		temp += (username==null || "".equals(username))?"":"("+username+")";
		temp += (areaname==null || "".equals(areaname))?"":"("+areaname+")";
		temp += (agentname==null || "".equals(agentname))?"":"(代理市场人员："+agentname+")";
		temp += ("0".equals(type)?"":("1".equals(type)?"--医学":""));
		return temp;
	}
	public void setAscription(String ascription) {
		this.ascription = ascription;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
