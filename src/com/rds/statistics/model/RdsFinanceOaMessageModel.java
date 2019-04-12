package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsFinanceOaMessageModel {
	
	private boolean success = true;
	private String message = "";
	private boolean result = false;
}
