package com.rds.trace.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/23
 */
@Data
public class RdsTraceBaseinfoVerifyModel {
    private String uuid;
    private String case_id;
    private String verify_baseinfo_time;
    //审核状态 0未审核 1已审核 2审核不通过
    private String verify_baseinfo_state;
    private String verify_baseinfo_person;
    private String verify_baseinfo_remark;
}
