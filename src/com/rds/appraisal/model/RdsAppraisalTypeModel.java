package com.rds.appraisal.model;

import lombok.Data;

/**
 * 鉴定类型实例
 * 
 * @author yxb 2015-07-22
 */
@Data
public class RdsAppraisalTypeModel {
	// 鉴定类型id
	private String type_id;
	// 鉴定类型名称
	private String standard_name;
	// 创建时间
	private String create_time;
	// 附录说明
	private String appendix_desc;
	// 附录标识：0，无附录；1，有附录
	private String appendix_status;
	//鉴定类型说明
	private String standard_desc;
	//是否选中
	private boolean checked = false;
}
