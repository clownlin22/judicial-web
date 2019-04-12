package com.rds.bacera.model;

import lombok.Data;

@Data
public class RdsBaceraMessageModel {
	
	private boolean success = true;
	private String message = "";
	private boolean result = false;

}
