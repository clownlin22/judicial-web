package com.rds.finance.model;

import lombok.Data;

@Data
public class RdsRemittanceInfoModel {
	private String remittance_id;
	private String remittance;
	private String remittance_date;
	private String remittance_account;
	private String remittance_num;
	private String arrival_account;
	private String remittance_per;
	private String remittance_per_name;
	private String remittance_remark;
	private String create_date;
	private String create_per;
	private String create_per_name;
	private String confirm_date;
	private String confirm_per;
	private String confirm_per_name;
	private String confirm_remark;
	private String daily_type;
	private String confirm_state;
	private int urgent_state;
	private String deductible;
	private String remittance_att;
}
