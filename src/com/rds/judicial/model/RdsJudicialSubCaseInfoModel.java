package com.rds.judicial.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/22
 */
@Data
public class RdsJudicialSubCaseInfoModel {
    private String case_code;
    private String sub_case_code;
    private String sample_code1;
    private String sample_code2;
    private String sample_code3;
    private String result;
    private String up_case_code;
    private String pi;
    private String rcp;
    private String parent1_pi;
    private String parent2_pi;
    private String con_pi;
    private String reg_pi;
    
}
