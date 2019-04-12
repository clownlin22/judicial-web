package com.rds.judicial.model;

import lombok.Data;

/**
 * 异常描述
 * @author 少明
 *
 */
@Data
public class RdsJudicialExceptionTypeModel {

	private String type_id;
	private String type_desc;
	private int is_delete;
}
