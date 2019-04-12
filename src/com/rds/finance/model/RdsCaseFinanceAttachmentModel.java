package com.rds.finance.model;

import lombok.Data;

@Data
public class RdsCaseFinanceAttachmentModel {
	private String id;
	private String remittance_id;
	private String attachment_path;
	private String create_per;
	private String create_date;
	private String create_pername;
}
