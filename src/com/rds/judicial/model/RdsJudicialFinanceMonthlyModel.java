package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialFinanceMonthlyModel {
	private String id;
	private String userid;
	private Double sum;
	private int type;
	private Double discountsum;
	private String monthly_time;
	private int status;
	private String remark;
}
