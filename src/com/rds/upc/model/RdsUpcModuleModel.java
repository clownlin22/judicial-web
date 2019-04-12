package com.rds.upc.model;

import lombok.Data;

@Data
public class RdsUpcModuleModel {
	
	private String id;
	private String modulecode;
	private String modulename;
	private String moduleico;  
	private String moduleurl;    
	private String moduletype;
	private String modulesqe;
	private String moduleparentcode;
	private String moduledesc;
	private Boolean leaf;
	private Boolean checked;

}
