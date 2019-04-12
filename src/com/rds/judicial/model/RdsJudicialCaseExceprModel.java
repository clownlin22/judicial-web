package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialCaseExceprModel {
	private String case_id;
	private int status;
	private String statusmessage;
	private String date;
}
