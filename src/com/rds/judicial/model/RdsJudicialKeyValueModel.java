package com.rds.judicial.model;

/**
 * @author dell
 */
import lombok.Data;

@Data
public class RdsJudicialKeyValueModel {
	
	private String key;
	private String value;
	private String status;
	private String callstatus;
	private Boolean checked;
}
