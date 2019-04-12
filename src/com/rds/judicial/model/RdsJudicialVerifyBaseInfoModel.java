package com.rds.judicial.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/30
 */
@Data
public class RdsJudicialVerifyBaseInfoModel {
    private String uuid;
    private String case_code;
    private String verify_baseinfo_time;
    private int verify_baseinfo_state;
    private int verify_baseinfo_count;
    private String verify_baseinfo_person;
    private String verify_baseinfo_remark;
}
