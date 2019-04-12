package com.rds.judicial.model;

import lombok.Data;
@Data
public class RdsJudicialReturnFeeModel {
	private String id;
	private String userid;
	private Double sum;
	private String bankaccount;
	private String return_time;
	private String remark;
}
