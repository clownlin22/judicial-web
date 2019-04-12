package com.rds.judicial.model;

import lombok.Data;
/**
 * @description 银行账号管理
 * @author ThinK
 *	2015年4月14日
 */
@Data
public class RdsJudicialBankModel {
	private String id;
	private String bankaccount;
	private String bankname;
	private String remark;
	private String companyid;
	private String companyname;
}
