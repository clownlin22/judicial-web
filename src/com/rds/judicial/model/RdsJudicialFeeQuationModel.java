/**
 * @author fushaoming
 * 20150701
 * 收费公式
 */
package com.rds.judicial.model;

import lombok.Data;

@Data
public class RdsJudicialFeeQuationModel {
	private String id;
	private String type_id;
	private String equation;
	private String discountrate;
}
