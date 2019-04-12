package com.rds.judicial.model;

import lombok.Data;
@Data
public class RdsJudicialPhoneCaseListModel {
	private String client;
	private String mail_code = "";
	private String sum = "";
	private int is_delete;
	private String case_code = "";
	private String date = "";
	private String case_id;
	private int status;
	private String statusmessage;
}
