package com.rds.finance.model;

/**
 * 
 * @class RdsJudicialCaseAttachmentModel
 * @discription 附件model
 * @author fushaoming
 * @date 2015年4月7日
 */
import lombok.Data;

@Data
public class RdsFinanceContractAttachmentModel {
	private String id;
	private String contract_id;
	private String contract_num;
	private String attachment_path;
	private String attachment_date;
	private int attachment_type;
	private String create_per;
	private String create_pername;
}
