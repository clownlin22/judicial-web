package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsRemittanceConsumptionTimeModel {
	private String fee_id;
	private String case_id;
	private String case_code;
	private String date;
	private String case_receiver;
	private int status;
	private String remittance_num;
	private int case_state;
	private int confirm_state;
	private String remittance_id;
	private String finance_remark;
	private String create_date;
	private String create_per;
	private String confirm_date;
	private String confirm_per;
	private int time_spent;
	
	
}
