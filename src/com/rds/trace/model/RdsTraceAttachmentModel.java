package com.rds.trace.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/21
 */
@Data
public class RdsTraceAttachmentModel {
    private String uuid;
    private String case_id;
    private String attachment_path;
    private String attachment_date;
    //0 word文档
    private int attachment_type;
}
