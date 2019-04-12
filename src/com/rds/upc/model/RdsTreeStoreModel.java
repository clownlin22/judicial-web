package com.rds.upc.model;

/**
 * @author dell
 */
import lombok.Data;

@Data
public class RdsTreeStoreModel {
	
	private String id;
	private String text;
	private boolean leaf =false;
	private boolean checked=false;
	
}
