package com.rds.trace.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/31
 */
@Data
public class RdsTracePersonInfoModel {
    private String uuid;
    private String case_id;
    private String person_name;
    private String id_number;
    private String address;
}
