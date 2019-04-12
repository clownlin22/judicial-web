package com.rds.upc.model;

import lombok.Data;

@Data
public class RdsUpcCompanyModel {
	private String companyid;
	private String companyname;
	private String companycode;
	private String status;
	private String address;
	private String telphone;
	private String contact;
	private String cratetime;
    private int laboratory_no;
    private String laboratory_name;

}
