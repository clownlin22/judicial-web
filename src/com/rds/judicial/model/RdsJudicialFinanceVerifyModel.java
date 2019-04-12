package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialFinanceVerifyModel {
	private String id;
	private String verify_id;
	private String receiver_id;
	private String username;
	private String areacode;
	private String areaname;
	private String remark;
	private int status;
	private String date;
	private double stand_sum;
	private double real_sum;
	private double return_sum;
	private String case_code;
	private int type;
}
