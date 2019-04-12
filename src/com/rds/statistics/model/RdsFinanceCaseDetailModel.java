package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsFinanceCaseDetailModel {
    private String case_id;
    private String case_code;
    private String accept_time;
    private String case_user;
    private String webchart;
    private String case_agentuser;
    private String case_area;
    private String user_dept_level1;
    private String user_dept_level2;
    private String user_dept_level3;
    private String user_dept_level4;
    private String user_dept_level5 ;
    private String deptcode ;
    private Double stand_sum;
    private Double real_sum;
    private Double return_sum;
    private String paragraphtime;
    private String confirm_date;
    private String update_date;
    private String case_type;
    private String case_subtype;
    private String partner;
    private int sample_count;
    private int special_count1;
    private int special_count2;
    private String client;
    private String type;
    private String remark;
    private String finance_remark;
    private String deductible;
}
