package com.rds.trace.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/30
 */
@Data
public class RdsTraceArchiveModel {
    private String archive_id;
    private String archive_code;
    private String case_id;
    private String archive_address;
    private String archive_date;
    private String archive_per;
    private String archive_path;
}
