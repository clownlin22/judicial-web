package com.rds.trace.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/30
 */
@Data
public class RdsTraceArchiveModelExt extends RdsTraceArchiveModel {
    private String archive_pername;
    private int case_no;
}
