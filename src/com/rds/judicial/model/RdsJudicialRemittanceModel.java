package com.rds.judicial.model;

import lombok.Data;
/**
 * @description 汇款账号管理
 * @author yxb
 *	2016-05-10
 */
@Data
public class RdsJudicialRemittanceModel {
	private String id;
	private String remittanceAccount;
	private String accountName;
	private String remark;
}
