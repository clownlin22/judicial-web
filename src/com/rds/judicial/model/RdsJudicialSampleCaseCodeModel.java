package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialSampleCaseCodeModel {
	private String sample_code;
	private String case_code;
	private String accept_time;
	private String verify_state;
	private String sample_in_time;
	private String report_modelname;
	private String process_instance_id;
}
