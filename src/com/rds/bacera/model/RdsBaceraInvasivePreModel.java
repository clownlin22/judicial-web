package com.rds.bacera.model;

import java.util.Date;

import lombok.Data;

@Data
public class RdsBaceraInvasivePreModel {
	private String id;
	private String num;
	private String date;
	private String name;
	private String code;
	private String sampledate;
	private String inspectionunit;
	private String hospital;
	private String confirm_code;//优惠码
	
	private String ownperson;
	private String agentid;
	private String agentname;
	private String ownpersonname;
	
	private String reportif;
	private String remark;
	private String remarks;
    private String verify_remark;//审核备注
	private String receivables;
	private String payment;
	private String paid;
	private String paragraphtime;
	private String account_type;
	
	private String expresstype;
	private String expressnum;
	private String recive;
	private String expressremark;
	
	private String cancelif;
	private String oa_num;
	private String expresstime;
	private String areaname;
	private String remittanceName;
	private String remittanceDate;
	private String verify_state;
	private String discountPrice;
	private String processInstanceId;
	private String areacode;
	private String type;
	
	private String task_id;
	private String task_def_key;
	private String task_name;
	private int suspension_state;
	private String last_task_id;
	
	private String uuid;
	private String attachment_path;
    private String attachment_date;
    private String upload_userid;
    private String down_flag;
    private String download_time;
    private String down_userid;
    private String confirm_state;
    private Date emaildate;
	
}
