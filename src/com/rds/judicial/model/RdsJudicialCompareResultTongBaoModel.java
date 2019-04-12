package com.rds.judicial.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/20
 */
@Data
public class RdsJudicialCompareResultTongBaoModel {
    private String case_code;
    private String username_1;
    private String username_2;
    private String sample_code1;
    private String sample_code2;
    private int ibs_count;
    private int unmatched_ystr_count;
    private String final_result_flag;
    private String unmatched_ystr_node;
    private String compare_date;
    private String laboratory_no;
    private String reagent_name;
}
