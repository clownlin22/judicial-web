package com.rds.trace.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/30
 */
@Data
public class RdsTraceArchiveReadModel {
    private String id;
    private String archive_id;
    private String read_per;
    private String read_date;
}
