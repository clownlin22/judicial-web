package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsFinanceCaseDetailStatisticsModel {
    private String id;
    private String case_id;
    private String case_code;
    private String case_user;
    private String webchart;
    private String case_area;
    private String accept_time;
    private String confirm_date;
    private Double real_sum;
    private Double return_sum;
    private String user_dept_level1;
    private String user_dept_level2;
    private String user_dept_level3;
    private String user_dept_level4;
    private String user_dept_level5;
    private Double insideCost;
    private String insideCostUnit;
    private Double manageCost;
    private String manageCostUnit;
    private Double externalCost;
    private String partner;
    private Double aptitudeCost;
    private Double experimentCost;
    private String update_date;
    private String case_type;
    private String case_subtype;
    private String type;
    private String client;
    private String remark;
    private String finance_remark;
    private String deductible;
}
