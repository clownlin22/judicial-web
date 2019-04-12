package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsFinanceProgramModel {
    private String price_id;
    private String program_name;
    private int program_case_num;
    private Double program_real_sum;
    private String program_month;
    private String user_dept_level1;
    private Double finance_dept;
    private int sample_count;
    private int special_count1;
    private int special_count2;
}
