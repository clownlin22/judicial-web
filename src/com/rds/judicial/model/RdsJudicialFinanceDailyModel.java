package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialFinanceDailyModel {
	private String id;
	private String daily_id;
	private String userid;
	private String username;
	private Double sum;
	private int type;
	private String daily_time;
	private int status;
	private int balancetype;
	private String confirm_time;
	private String remark;
}
