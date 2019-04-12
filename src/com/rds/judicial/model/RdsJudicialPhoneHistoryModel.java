package com.rds.judicial.model;

import java.util.Date;

import lombok.Data;
@Data
public class RdsJudicialPhoneHistoryModel {
	private String case_code = "";
	private Date date = null;
	private double sum = 0.0;
	private int is_delete = 0;
	private String remark = "";
}
