package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialConfirmReturnModel {
	private String case_code;
	private Double return_sum;
	private Double real_sum;
	private Double standFee;
	private Double discountrate;
	private int status;
	private String remark;
}
