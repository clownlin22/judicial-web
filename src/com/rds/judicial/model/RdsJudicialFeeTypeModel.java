/**
 * @author fushaoming
 * 20150701
 * 地区及收费类型
 */
package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialFeeTypeModel {
	private String type_id;
	private String area_id;
	private Integer feetype;
	private Integer is_delete;
}
