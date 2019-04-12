package com.rds.finance.model;

import lombok.Data;

@Data
public class RdsCaseFinanceModel {
	private String id;
	private String fee_id;
	private String case_id;
	private String case_code;
	private double stand_sum;
	private String confirm_date;
	private double real_sum;
	private double return_sum;
	private String areaname;
	private Double discount;
	private String date;
	private String sample_str;
	private String case_remark;
	private String receive_name;
	private String client;
	private String account;
	private String paragraphtime;
	private String remittanceName;
	private String remittanceDate;
	
	private String is_delete;
	/**
	 * 财务审核状态 0 正常 1 异常 2日报已结算 3登记状态
	 */

	private int status;
	
	private double discountPrice;
	
	private String finance_remark;
	private String dailyid;
	/**
	 * 0为正常 1为先出报告后付款 2为免单
	 */
	private int type;
}
