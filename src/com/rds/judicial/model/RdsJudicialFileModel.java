package com.rds.judicial.model;

import java.util.Date;

import lombok.Data;

/**
 * @class RdsJudicialFileModel
 * @description 页面附件展示model
 * @author ThinK
 * 2015年4月8日
 */
@Data
public class RdsJudicialFileModel {
	private String case_code;
	private String file_name;
	private Date date;
}
