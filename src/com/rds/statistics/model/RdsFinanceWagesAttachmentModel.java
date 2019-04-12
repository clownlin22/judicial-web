package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsFinanceWagesAttachmentModel {
	private String attachment_id;
	private String attachment_path;
	private String attachment_date;
	private String wages_month;
	private String create_pername;
}
