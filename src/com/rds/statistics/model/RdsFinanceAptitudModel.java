package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsFinanceAptitudModel {
    private String config_id;
    private String partnername;
    private int aptitude_case;
    private int experiment_sample;
    private int experiment_case;
    private int aptitude_sample;
    private String partner_start;
    private String partner_end;
    private int month_num;
    private int month_num_reduce;
    private int sample_special1;
    private int sample_special2;
    private String remark;
    private String provice;
    private String partner_flag;
}
