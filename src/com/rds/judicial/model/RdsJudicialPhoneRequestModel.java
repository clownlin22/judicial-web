package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialPhoneRequestModel {
	private String userid;
	private String usercode;
	private String case_code;
	private String token;
	private double charge;
	private String remark;
	private int filenum;
	private String filetype;
	private String areaid;
	private String case_areacode;
	private Double standFee;
	private String accept_time;
	private Double discount;
	/**
	 * 0为正常 1为先出报告后付款 2为免单
	 */
	private Integer isfree;
	private String isdeletecase_code;
}
