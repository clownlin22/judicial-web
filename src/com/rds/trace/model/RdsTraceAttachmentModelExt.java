package com.rds.trace.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/8/17
 */
@Data
public class RdsTraceAttachmentModelExt extends RdsTraceAttachmentModel{
    private int year;
    private int case_no;
    private String receive_time;
}
