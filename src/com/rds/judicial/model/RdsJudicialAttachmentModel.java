package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialAttachmentModel {
	
    private String uuid;
    private String attachment_path;
    private String attachment_date;
    private String upload_username;
    private String download_time;
    private String download_username;
    private String down_flag;
    private String sample_code;
}
