package com.rds.upc.model;

import lombok.Data;

@Data
public class RdsUpcMessageModel {
	
	private boolean success = true;
	private String message = "";
	private boolean result = false;

}
