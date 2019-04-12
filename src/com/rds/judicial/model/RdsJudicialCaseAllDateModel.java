package com.rds.judicial.model;

import java.util.Date;

import lombok.Data;

@Data
public class RdsJudicialCaseAllDateModel {
	private String case_id;
	private Date accept_time;
	private Date mail_time;
	private Date confrim_date;
	private Date compare_date;
	private Date attachment_date;
	private Date handle_time;
}