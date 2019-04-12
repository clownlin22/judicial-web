package com.rds.bacera.model;

import lombok.Data;

@Data
public class RdsBaceraProgramModel {
	private String id;
	private String program_name;
	private String program_code;
	private String program_type;
	private String create_per;
	private String create_pername;
	private String create_time;
	private String is_delete;
	private String remark;
}
