package com.rds.children.model;

import lombok.Data;

@Data
public class RdsChildrenCaseInfoModel {
	private String case_id;
	private String case_code;
	private String sample_code;
	private String case_areacode;
	private String case_areaname;
	private String case_userid;
	private String case_username;
	private String address;
	private String child_name;
	private String birth_date;
	private int child_sex = 0;
	private String id_number;
	private String birth_hospital;
	private String house_area;
	private String life_area;
	private String gather_time;
	private String gather_id;
	private String case_in_per;
	private String case_in_pername;
	private String case_in_time;
	private int is_delete = 0;
	private String agentia_id;
	private String agentia_name;
	private String invoice;
	private String mail_area;
	private String tariff_id;
	private String tariff_name;
	private String mail_name;
	private String mail_code;
	private String remark;
	private String print_time;

	private String father_name;
	private String father_id_number;
	private String father_phone;
	private String mother_name;
	private String mother_id_number;
	private String mother_phone;

	private int verify_state;
	private String process_instance_id;
	private String task_id;
	private String task_def_key;
	private String task_name;
	private int suspension_state;
	private String last_task_id;
	/* 0代表没有意见，大于0代表有意见 */
	private int has_comment;

	private Double stand_sum;
	private Double return_sum;
	private Double real_sum;
	private String paragraphtime;
	private String confirm_date;
	private String finance_remark;
	private String confirm_state;
	
}
