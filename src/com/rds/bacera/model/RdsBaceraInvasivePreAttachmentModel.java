package com.rds.bacera.model;

import lombok.Data;

@Data
public class RdsBaceraInvasivePreAttachmentModel {
	private String id;
	private String cancelif;
	private String uuid;
    private String num;
    private String name;
    private String verify_state;
    private String attachment_path;
    private String attachment_date;
    private String upload_username;
    private String download_time;
    private String download_username;
    private String down_flag;
    private String sample_code;
    private String process_instance_id;
    private String task_id;
    private String task_def_key;
    private String task_name;
    private String suspension_state;
    private String emailflag;
    private String confirm_state;
    private String type;//案例状态
    private String receiveAddress;
    private String ownpersonname;
}
