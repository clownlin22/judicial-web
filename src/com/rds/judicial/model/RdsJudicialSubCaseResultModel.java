package com.rds.judicial.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/23
 */
@Data
public class RdsJudicialSubCaseResultModel {
    private String case_code;
    private String sub_case_code;
    private String need_ext;
    private String result;
    private String username1;
    private String username2;
    private String username3;
    private String unmatched_count;
    private String unmatched_node;
    private String last_compare_date;
    private int experiment_count;
    private String pi;
    private String parent1_pi;
    private String parent2_pi;
    
}
