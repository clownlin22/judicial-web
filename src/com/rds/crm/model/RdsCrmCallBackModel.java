package com.rds.crm.model;

import lombok.Data;

@Data
public class RdsCrmCallBackModel {
	private String cbId;
	private String cbInPer;
	private String cbContent;
	private String cbTime;
	private String orderId;
}
