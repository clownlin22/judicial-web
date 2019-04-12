package com.rds.appraisal.model;

import lombok.Data;

/**
 * 鉴定模版实例
 * 
 * @author yxb 2015-07-31
 */
@Data
public class RdsAppraisalTemplateModel {
	// 鉴定模版id
	private String template_id;
	// 体格检查
	private String process_check;
	// 分析说明
	private String analysis_text;
	// 鉴定意见
	private String advice_text;
	//模版说明
	private String standard_explain;
	//案情摘要
	private String case_abstract;
	//病例摘要
	private String sickness_abstract;
	//检验方法
	private String process_method;
	//阅片所见
	private String process_read;
	//关键字
	private String keyword;
	//有无阅片所见
	private String read_flag;
}
