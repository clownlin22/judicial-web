package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialFinanceReturnModel {
	private String userid;
	private String username;
	private double return_all_sum;
	private double all_sum;
	private double balance;
}
