package com.rds.statistics.model;

public class RdsStatsticsJudicialTimeModel {
	private String case_code;
	private String accept_time;
	private String close_time;
	private String sample_in_time;
	private String verify_baseinfo_time;
	private String verify_sampleinfo_time;
	private String mail_time;
	private String compare_date;
	private String confirm_date;

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

	public String getClose_time() {
		return close_time;
	}

	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}

	public String getSample_in_time() {
		return sample_in_time;
	}

	public void setSample_in_time(String sample_in_time) {
		this.sample_in_time = sample_in_time;
	}

	public String getVerify_baseinfo_time() {
		return verify_baseinfo_time;
	}

	public void setVerify_baseinfo_time(String verify_baseinfo_time) {
		this.verify_baseinfo_time = verify_baseinfo_time;
	}

	public String getVerify_sampleinfo_time() {
		return verify_sampleinfo_time;
	}

	public void setVerify_sampleinfo_time(String verify_sampleinfo_time) {
		this.verify_sampleinfo_time = verify_sampleinfo_time;
	}

	public String getMail_time() {
		return mail_time;
	}

	public void setMail_time(String mail_time) {
		this.mail_time = mail_time;
	}

	public String getCompare_date() {
		return compare_date;
	}

	public void setCompare_date(String compare_date) {
		this.compare_date = compare_date;
	}

	public String getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}

}
