package com.rds.upc.model;

import lombok.Data;

@Data
public class RdsUpcPermitNodeModel {
	private String moduleparentcode;
	private String id;
	private String text;
	private String url;
	private String type;
	private boolean leaf =true;
	private Boolean checked;
	private String sort;

}
