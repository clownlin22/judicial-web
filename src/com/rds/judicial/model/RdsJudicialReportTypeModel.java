package com.rds.judicial.model;

/**
 * 
 */
import lombok.Data;

@Data
public class RdsJudicialReportTypeModel {
	
	private String typeid;
	private String id;
	private String typename;
	private String inputform;
	private String displaygrid;
	private String identify;
	private String status;
	private String sort;
	private String parentid;
	private Boolean leaf;
	private Boolean checked;

}
