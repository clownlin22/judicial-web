package com.rds.statistics.model;

import java.text.DecimalFormat;

public class RdsFinanceWagesModel {
	DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置
	private String id;
	private String attachment_id;
	private String wages_name;
	private String user_dept_level1;
	private String user_dept_level2;
	private String user_dept_level3;
	private String user_dept_level4;
	private String user_dept_level5;
	private String workcode;
	private String wages_month;
	private double wages;
	private double wages_social;
	private double wages_accumulation;
	private double wages_middle;
	private double wages_end;
	private double wages_other;
	private String create_pername;
	private String create_date;
	private String remark;

	public DecimalFormat getDecimalFormat() {
		return decimalFormat;
	}

	public void setDecimalFormat(DecimalFormat decimalFormat) {
		this.decimalFormat = decimalFormat;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAttachment_id() {
		return attachment_id;
	}

	public void setAttachment_id(String attachment_id) {
		this.attachment_id = attachment_id;
	}

	public String getWages_name() {
		return wages_name;
	}

	public void setWages_name(String wages_name) {
		this.wages_name = wages_name;
	}

	public String getUser_dept_level1() {
		return user_dept_level1;
	}

	public void setUser_dept_level1(String user_dept_level1) {
		this.user_dept_level1 = user_dept_level1;
	}

	public String getUser_dept_level2() {
		return user_dept_level2;
	}

	public void setUser_dept_level2(String user_dept_level2) {
		this.user_dept_level2 = user_dept_level2;
	}

	public String getUser_dept_level3() {
		return user_dept_level3;
	}

	public void setUser_dept_level3(String user_dept_level3) {
		this.user_dept_level3 = user_dept_level3;
	}

	public String getWages_month() {
		return wages_month;
	}

	public void setWages_month(String wages_month) {
		this.wages_month = wages_month;
	}

	public double getWages() {
		return wages;
	}

	public String getWagesTemp() {
		return decimalFormat.format(wages);
	}

	public void setWages(double wages) {
		this.wages = wages;
	}

	public double getWages_social() {
		return wages_social;
	}

	public void setWages_social(double wages_social) {
		this.wages_social = wages_social;
	}

	public String getWages_socialTemp() {
		return decimalFormat.format(wages_social);
	}

	public double getWages_accumulation() {
		return wages_accumulation;
	}

	public void setWages_accumulation(double wages_accumulation) {
		this.wages_accumulation = wages_accumulation;
	}

	public String getWages_accumulationTemp() {
		return decimalFormat.format(wages_accumulation);
	}

	public double getWages_middle() {
		return wages_middle;
	}

	public String getWages_middleTemp() {
		return decimalFormat.format(wages_middle);
	}

	public void setWages_middle(double wages_middle) {
		this.wages_middle = wages_middle;
	}

	public double getWages_end() {
		return wages_end;
	}

	public String getWages_endTemp() {
		return decimalFormat.format(wages_end);
	}

	public void setWages_end(double wages_end) {
		this.wages_end = wages_end;
	}

	public double getWages_other() {
		return wages_other;
	}

	public String getWages_otherTemp() {
		return decimalFormat.format(wages_other);
	}

	public void setWages_other(double wages_other) {
		this.wages_other = wages_other;
	}

	public String getCreate_pername() {
		return create_pername;
	}

	public void setCreate_pername(String create_pername) {
		this.create_pername = create_pername;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUser_dept_level4() {
		return user_dept_level4;
	}

	public void setUser_dept_level4(String user_dept_level4) {
		this.user_dept_level4 = user_dept_level4;
	}

	public String getUser_dept_level5() {
		return user_dept_level5;
	}

	public void setUser_dept_level5(String user_dept_level5) {
		this.user_dept_level5 = user_dept_level5;
	}

	public String getWorkcode() {
		return workcode;
	}

	public void setWorkcode(String workcode) {
		this.workcode = workcode;
	}

}
