package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsFinanceConfigModel {
    private String config_id;
    private String program;
    private String finance_program;
    private String finance_program_type;
    private String front_settlement;
    private String finance_manage;
    private String back_settlement;
    private String back_remark;
    private String create_time;
    private String create_per;
    private String finance_manage_dept;
    private String back_settlement_dept;
    private String back_settlement_dept1;
    private String remark;
    private String price;
    private String experiment_price;
    private String agency_price;
    private String business_support;
}
