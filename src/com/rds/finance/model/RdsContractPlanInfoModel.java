package com.rds.finance.model;

import lombok.Data;

@Data
public class RdsContractPlanInfoModel {
	private String contract_id;
	private String accept_date;
	private String contract_price;
	private String contract_unit;
	private String contract_num;
	private String contract_program;
	private String contract_userid;
	private String contract_username;
	private String contract_areacode;
	private String contract_areaname;
	private String create_per;
	private String create_per_name;
	private String create_date;
	private String status;
	private String oa_num;
	private String customer_name;
	private String remark;
}
