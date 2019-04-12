package com.rds.children.model;

import java.sql.Date;

public class RdsChildrenCaseResultModel {
	private String result_id;
	private String case_id;
	private String child_name;
	private String case_code;
	private String sample_code;
	private String result;
	private String reagent;
	private String result_in_time;
	private Date gather_time;
	private int child_sex;

	public String getResult_id() {
		return result_id;
	}

	public void setResult_id(String result_id) {
		this.result_id = result_id;
	}

	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}

	public String getChild_name() {
		return child_name;
	}

	public void setChild_name(String child_name) {
		this.child_name = child_name;
	}

	public String getCase_code() {
		return case_code;
	}

	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getReagent() {
		return reagent;
	}

	public void setReagent(String reagent) {
		this.reagent = reagent;
	}

	public String getResult_in_time() {
		return result_in_time;
	}

	public void setResult_in_time(String result_in_time) {
		this.result_in_time = result_in_time;
	}

	public String getSample_code() {
		return sample_code;
	}

	public void setSample_code(String sample_code) {
		this.sample_code = sample_code;
	}

	public Date getGather_time() {
		return gather_time;
	}

	public void setGather_time(Date gather_time) {
		this.gather_time = gather_time;
	}

	public int getChild_sex() {
		return child_sex;
	}

	public void setChild_sex(int child_sex) {
		this.child_sex = child_sex;
	}

}
