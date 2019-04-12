package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialMessageModel {
	
	private boolean success = true;
	private String message = "";
	private boolean result = false;

	public RdsJudicialMessageModel(boolean success, String message, boolean result){
		this.success = success;
		this.message = message;
		this.result = result;
	}
}
