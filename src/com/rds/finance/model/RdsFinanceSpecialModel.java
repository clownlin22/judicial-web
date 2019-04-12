package com.rds.finance.model;

import lombok.Data;

@Data
public class RdsFinanceSpecialModel {
	private String id;
	private String case_id;
	private String confirm_code;
	private String oa_code;
	private String discount_amount;
	private String apply_date;
	private String apply_per;
	private String apply_pername;
	private String estimate_date;
	private String monthly_per;
	private String monthly_pername;
	private String monthly_area;
	private String monthly_areaname;
	private String confirm_date;
	private String confirm_per;
	private String case_state;
	private String activation_state;
	private String settlement_state;
	private String user_state;
	private String create_date;
	private String remark;
	private String hospital;
	private String case_type;
}
