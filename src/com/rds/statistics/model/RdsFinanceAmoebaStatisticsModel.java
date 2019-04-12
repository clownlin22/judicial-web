package com.rds.statistics.model;

import java.sql.Date;

import lombok.Data;

@Data
public class RdsFinanceAmoebaStatisticsModel {
    private String amoeba_id;
    private String amoeba_program;
    private String amoeba_deptment;
    private String amoeba_deptment1;
    private String amoeba_month;
    private String amoeba_sum;
    private String create_per;
    private String create_pername;
    private Date create_date;
    private int sort;
}
