package com.rds.crm.model;

import lombok.Data;

@Data
public class RdsCrmFeeModel {
	private String feeId;
	private String orderId;
	private Double realFee;
	private String feeTime;
}
