package com.rds.bacera.model;

import lombok.Data;

/**
 * 
 * @author yxb
 *
 */
@Data
public class RdsBaceraAbilityModel {
	
	private String ability_id;
	//项目编号
	private String ability_num;
	//项目名称
	private String ability_name;
	//参加编号
	private String participate_num;
	//所属单位
	private String ability_company;
	//样本类型
	private String sample_type;
	//鉴定开始时间
	private String identify_starttime;
	//鉴定结束时间
	private String identify_endtime;
	//联系人
	private String contacts_per;
	//联系电话
	private String contacts_phone;
	//联系邮箱
	private String contacts_mail;
	//归属人
	private String ownperson;
	//接样人
	private String sample_per;
	//接样日期
	private String sample_date;
	//接收快递人
	private String sample_express_per;
	//结果
	private String ability_result;
	//备注
	private String ability_remark;
	//科室id
	private String department_concatid;
	//科室名称
	private String department_name;
	//科室负责人
	private String department_chargename;
	//实验负责人
	private String experiment_chargename;
	//报告负责人
	private String report_chargename;
	//项目完成日期
	private String finished_date;
	//报告发送人
	private String report_sendname;
	//发送人联系方式
	private String report_concat;
	//发送日期
	private String report_senddate;
	//发件信息
	private String report_sendinfo;
	//收件信息
	private String report_reciveinfo;
	//报告快递类型
	private String report_type;
	//报告快递编号
	private String report_num;
	//附件信息
	private String attachment_id;
	//应收
	private String receivables;
	//所付
	private String payment;
	//回款
	private String paid;
	//到账日期
	private String paragraphtime;
	//到账类型
	private String account_type;
	//优惠价格
	private String discountPrice;
	//财务备注
	private String finance_remark;
	//汇款账户
	private String remittanceName;
	//汇款日期
	private String remittanceDate;
	private String delete;
}
