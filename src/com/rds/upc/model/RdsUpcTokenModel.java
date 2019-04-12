package com.rds.upc.model;

import lombok.Data;

@Data
public class RdsUpcTokenModel {

	//token值
	private String token;
	//用户名
	private String usercode;
	
	private String flag;
}
