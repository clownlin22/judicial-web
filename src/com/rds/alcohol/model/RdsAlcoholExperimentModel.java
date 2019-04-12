package com.rds.alcohol.model;

import lombok.Data;

@Data
public class RdsAlcoholExperimentModel {
	private String exper_id;
	private String exper_code;
	private String case_id;
	private String reg_id;
	private String exper_time;
	private int exper_isdelete;
	private String remark;
}
