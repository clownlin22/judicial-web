package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialAllCaseInfoModel {
	private String case_id;
	private String case_code;
	private String case_area;
	private String client;
	private String case_receiver;
	private String receiver_area;
	private String accept_time;
	private String sample_info;
	private String mail_info;
	private String fee_info;
	private String sample_in_time;
	private String remark;
	private String agent;
	private String real_sum;
	private String sample_count;
	private String finance_remark;
	private String parnter_name;
	private String case_in_per;
	private String update_date;
}
