package com.rds.judicial.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/6/2
 */
@Data
public class RdsJudicialMissingDataModel {
    private String sample_code;
    private String case_code;
    private int sample_record_count;
    private int sample_in_experiment;
    private String sample_in_time;


}
