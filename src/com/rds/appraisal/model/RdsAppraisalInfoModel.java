package com.rds.appraisal.model;

import lombok.Data;

/**
 * 鉴定报告实例
 * 
 * @author yxb 2015-07-27
 */
@Data
public class RdsAppraisalInfoModel {
	// 伤残鉴定基础信息表唯一id
	private String case_id;
	// 委托人
	private String entrust_per;
	// 委托函号
	private String entrust_num;
	// 委托事项
	private String entrust_matter;
	// 鉴定材料
	private String identify_stuff;
	// 受理日期
	private String accept_date;
	// 鉴定日期开始时间
	private String identify_date_start;
	// 鉴定日期结束时间
	private String identify_date_end;
	// 鉴定地点
	private String identify_place;
	// 被鉴定人姓名
	private String identify_per_name;
	// 被鉴定人性别
	private String identify_per_sex;
	// 被鉴定人出生日期
	private String identify_per_both;
	// 被鉴定人地址
	private String identify_per_address;
	// 被鉴定人身份证号
	private String identify_per_idcard;
	//案例创建时间
	private String create_time;
	//状态：0，未回退；1，已回退；2，未审核；3，已审核
	private String flag_status;
	//案例归属人
	private String case_in_person;
	//案例归属地
	private String case_in_area;
	//案例归属人id
	private String recrive_id;
	//鉴定号
	private String case_number;
	//鉴定机构
	private String judgename;
	
}
