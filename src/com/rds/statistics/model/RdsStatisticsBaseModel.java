package com.rds.statistics.model;

public class RdsStatisticsBaseModel {
	private String case_type;
	private String date;
	private int rec_total = 0;
	private int pre_total = 0;
	private String ownperson;

	public String getCase_type() {
		return case_type;
	}

	public void setCase_type(String case_type) {
		this.case_type = case_type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getRec_total() {
		return rec_total;
	}

	public void setRec_total(int rec_total) {
		this.rec_total = rec_total;
	}

	public int getPre_total() {
		return pre_total;
	}

	public void setPre_total(int pre_total) {
		this.pre_total = pre_total;
	}

	public String getOwnperson() {
		return ownperson;
	}

	public void setOwnperson(String ownperson) {
		this.ownperson = ownperson;
	}
}
