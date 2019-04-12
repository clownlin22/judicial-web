package com.rds.judicial.model;

import lombok.Data;

public class RdsJudicialCaseReportModel {
	private String case_code;
	private String sub_case_code;
	private String sample_name1;
	private String sample_name2;
	private String sample_name3;
	private String sample_code1;
	private String sample_code2;
	private String sample_code3;
	private String result;
	private String report_path;

	public String getCase_code() {
		return case_code;
	}

	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}

	public String getSub_case_code() {
		return sub_case_code;
	}

	public void setSub_case_code(String sub_case_code) {
		this.sub_case_code = sub_case_code;
	}

	public String getSample_name1() {
		return sample_name1;
	}

	public void setSample_name1(String sample_name1) {
		this.sample_name1 = sample_name1;
	}

	public String getSample_name2() {
		return sample_name2;
	}

	public void setSample_name2(String sample_name2) {
		this.sample_name2 = sample_name2;
	}

	public String getSample_name3() {
		return sample_name3;
	}

	public void setSample_name3(String sample_name3) {
		this.sample_name3 = sample_name3;
	}

	public String getSample_code1() {
		return sample_code1;
	}

	public void setSample_code1(String sample_code1) {
		this.sample_code1 = sample_code1;
	}

	public String getSample_code2() {
		return sample_code2;
	}

	public void setSample_code2(String sample_code2) {
		this.sample_code2 = sample_code2;
	}

	public String getSample_code3() {
		return sample_code3;
	}

	public void setSample_code3(String sample_code3) {
		this.sample_code3 = sample_code3;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getReport_path() {
		return report_path == null ? "" : this.report_path;
	}

	public void setReport_path(String report_path) {
		this.report_path = report_path;
	}

}
