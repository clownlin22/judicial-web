package com.rds.alcohol.model;

import java.util.Date;

import lombok.Data;
@Data
public class RdsAlcoholRegressionModel {
	private String reg_id;
	private String reg_code;
	private Date reg_time;
	private Double reg_A;
	private Double reg_B;
	private Double reg_R2;
	private int reg_qualify;
}
