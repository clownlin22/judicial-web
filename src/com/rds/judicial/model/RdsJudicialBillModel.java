package com.rds.judicial.model;

import java.util.Date;

import lombok.Data;

/**
 * 发票model
 * @author fushaoming
 * 2015.4.2
 */
@Data
public class RdsJudicialBillModel {
	private String bill_id;
	private String case_id;
	private String bill_code;
	private double bill_charge;
	private String bill_per;
	private Date date;
	private String remark;
}
