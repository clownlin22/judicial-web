package com.rds.appraisal.model;

import lombok.Data;

/**
 * 附件实例
 * 
 * @author yxb 2015-08-05
 */
@Data
public class RdsAppraisalAttachmentModel {
	// 鉴定id
	private String case_id;
	// 附件id
	private String attachment_id;
	// 附件类型
	private String attachment_type;
	// 附件地址
	private String attachment_filename;
	// 附件名称
	private String attachment_name;
	// 附件次序
	private String attachment_order;
}
