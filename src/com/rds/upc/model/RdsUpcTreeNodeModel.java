package com.rds.upc.model;

import lombok.Data;

@Data
public class RdsUpcTreeNodeModel {
	
	private String id;
	private String text;
	private boolean leaf;
	private String parentid;

}
