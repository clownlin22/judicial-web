package com.rds.upc.model;

import lombok.Data;

@Data
public class RdsUpcRoleModel {

	//角色id
	private String roleid;
	//角色类型
	private String roletype;
	//角色名称
	private String rolename;
	//角色权限名称
	private String permitname;
	//角色模块集合
	private String modulecode;
	//是否被选中
	private Boolean checked;
}
