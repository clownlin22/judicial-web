package com.rds.upc.model;

import lombok.Data;

@Data
public class RdsUpcUserModel {

	private String usercode;
	private String username;
	private String password;
	private String deptcode;
	private String deptname;
	private String deptid;
	private String usertype;
	private String typename;
	private String usertypename;
	private String companyname;
	private String companyid;
	private String telphone;
	private String qq;
	private String webchart;
	private String email;
	private String address;
	private String sex;;
	private String userid;
	private String roletype;
	private String rolename;
	//证书号
	private String certificateno;
	//首字母缩写
	private String initials;
	
	private String sql_str;
	
	private String sql_str2;

    private String laboratory_no;
    
    private String relation_id;
    
    private String parentDeptname;
    
    private String areacode;
    
    private String parnter_name;
    
    private String receiver_area;

}
