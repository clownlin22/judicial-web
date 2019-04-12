package com.rds.judicial.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/20
 */
@Data
public class RdsJudicialCompareResultModel {
    private String case_code;
    private String parent1;
    private String parent2;
    private String child;
    private String sample_code1;
    private String sample_code2;
    private String sample_code3;
    private int record_count;
    private String ext_flag;
    private int unmatched_count;
    private String final_result_flag;
    private String unmatched_node;
    private String compare_date;
    private String unmatched_node_all;
    private String reagent_name;
    private String unmatched_count_str;
    private String unmatched_node_part1;
    private String unmatched_node_part2;
}
