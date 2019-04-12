package com.rds.finance.model;

import lombok.Data;

@Data
public class RdsRemittanceLogInfoModel {
	private String remittance_id;
	private String confirm_state;
	private String confirm_per;
	private String confirm_per_name;
	private String confirm_date;
	private String confirm_remark;
}
