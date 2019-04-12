package com.rds.trace.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/29
 */
@Data
public class RdsTraceMailModel {
    private String mail_id;
    private String mail_code;
    private String mail_time;
    private String mail_type;
    private String mail_per;
    private String case_id;
    private int is_delete;
}
