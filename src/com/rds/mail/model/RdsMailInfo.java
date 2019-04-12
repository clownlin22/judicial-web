package com.rds.mail.model;

import java.util.ArrayList;
import java.util.List;

public class RdsMailInfo {
	private String mail_id = "";
	private String mail_code = "";
	private String mail_per = "";
	private String mail_pername = "";
	private String mail_typeStr = "";
	private String mail_type = "";
	private String mail_time = "";
	private String addressee = "";
	private List<RdsMailCaseInfo> caseInfos = new ArrayList<RdsMailCaseInfo>();

	public String getMail_id() {
		return mail_id;
	}

	public void setMail_id(String mail_id) {
		this.mail_id = mail_id;
	}

	public String getMail_pername() {
		return mail_pername;
	}

	public void setMail_pername(String mail_pername) {
		this.mail_pername = mail_pername;
	}

	public String getMail_typeStr() {
		return mail_typeStr;
	}

	public void setMail_typeStr(String mail_typeStr) {
		this.mail_typeStr = mail_typeStr;
	}

	public String getMail_code() {
		return mail_code;
	}

	public void setMail_code(String mail_code) {
		this.mail_code = mail_code;
	}

	public String getMail_per() {
		return mail_per;
	}

	public void setMail_per(String mail_per) {
		this.mail_per = mail_per;
	}

	public String getMail_type() {
		return mail_type;
	}

	public void setMail_type(String mail_type) {
		this.mail_type = mail_type;
	}

	public String getMail_time() {
		return mail_time;
	}

	public void setMail_time(String mail_time) {
		this.mail_time = mail_time;
	}

	public String getAddressee() {
		return addressee;
	}

	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	public List<RdsMailCaseInfo> getCaseInfos() {
		return caseInfos;
	}

	public void setCaseInfos(List<RdsMailCaseInfo> caseInfos) {
		this.caseInfos = caseInfos;
	}
}
