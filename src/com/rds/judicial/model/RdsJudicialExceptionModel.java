package com.rds.judicial.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/16
 */
@Data
public class RdsJudicialExceptionModel {
    private String uuid;
    private String case_code;
    private String case_id;
    private String sample_code1;
    private String sample_code2;
    private String exception_id;
    private String exception_desc;
    private String trans_date;
    private int state;
    //1未处理 0已处理
    private int choose_flag;
    //交叉比对相关
    private String case_code1;
    private String case_code2;
}
