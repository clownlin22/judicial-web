package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialAgentModel {
	private String id;
	private String userid;
	private String peruserid;
	private String remark;
	private String username;
	private String case_code;
	private String perusername;
	private String case_areaname;
	private String case_reciver;
	private String idAdress;
	private String client;
	private String phone;
	private int urgent_state;
	private int verify_state = 0;
	private String accept_time;
	private String close_time;
	private String text;
	private int is_delete;
	private String createper;
	private String createtime;
}
