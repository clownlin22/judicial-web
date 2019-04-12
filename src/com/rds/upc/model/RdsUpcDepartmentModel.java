package com.rds.upc.model;

import lombok.Data;

@Data
public class RdsUpcDepartmentModel {
	private String deptid;
	private String deptname;
	private String parentname;
	private String parentdeptcode;
	private String parentid;
	private String deptcode;
	private boolean leaf;
	private String companyname;
	private String companyid;
    private String islaboratory;

}
