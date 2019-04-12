package com.rds.finance.model;

import com.mysql.jdbc.StringUtils;

/**
 * 财务浏览器提示
 * @author yxb
 *
 */
public class RdsFinancePromptInfo {
	private String username;
	private String agentname;
	private String case_code;
	@SuppressWarnings("unused")
	private String promptInfo;
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
	public String getPromptInfo() {
		return StringUtils.isNullOrEmpty(agentname)?username:(agentname+"(代理"+username+")");
	}
	public void setPromptInfo(String promptInfo) {
		this.promptInfo = promptInfo;
	}
	public String getCase_code() {
		return case_code;
	}
	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}
	
}
