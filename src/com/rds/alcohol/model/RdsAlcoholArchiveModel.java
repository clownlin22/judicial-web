package com.rds.alcohol.model;

import lombok.Data;

/**
 * @description 归档表model
 * @author ThinK
 * 2015年4月27日
 */
@Data
public class RdsAlcoholArchiveModel {
	private String archive_id;
	private String case_code;
	private String archive_code;
	private String archive_address;
	private String archive_date;
	/**
	 * 归档人
	 */
	private String archive_per;
	private String archive_path;
	private String archive_pername;
}
