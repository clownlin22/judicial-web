package com.rds.finance.model;

import lombok.Data;

@Data
public class RdsFinanceChargeStandardModel {
	private String id;
	private String areacode;
	private String areaname;
	private String type;
	private String source_type;
	private String program_type;
	private String equation;
	//归属人
	private String userid;
	private String username;
	private String createuserid;
	private String create_time;
	//归属人为代理商时的被代理市场人员
	private String agentid;
	private String agentname;
	private String singlePrice;
	private String doublePrice;
	private String samplePrice;
	private String gapPrice;
	private Double specialPirce;
	private Double specialPirce1;
	private Double specialPirce2;
	private Double urgentPrice;
	private Double urgentPrice1; 
	private Double urgentPrice2; 
	private String hospital;
}
