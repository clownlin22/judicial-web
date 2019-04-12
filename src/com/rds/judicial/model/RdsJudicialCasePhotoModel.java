package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialCasePhotoModel {
	
	private String case_id;
	private String case_code;
	private String receiver_area;
	private String receiver_id;
	private int print_copies;
	private String accept_time;
	private String attachment_path;
	private String attachment_date;
	private int is_delete;
}
