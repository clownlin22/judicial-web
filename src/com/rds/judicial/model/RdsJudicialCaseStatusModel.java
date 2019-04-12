package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialCaseStatusModel {
	private int print_count;
	private Integer verify_sampleinfo_state;
	private String mail_code;
	private String accept_time;
	private String sample_in_time;
	private String verify_baseinfo_time;
	private String verify_sampleinfo_time;
	private String trans_date;
	private String close_time;
	private String mail_time;
	private String client;
	private String final_result_flag;
}
