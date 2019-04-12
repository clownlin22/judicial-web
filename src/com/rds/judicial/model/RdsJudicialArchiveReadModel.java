package com.rds.judicial.model;

import lombok.Data;

/**
 * 2015年4月27日
 * @author fushaoming
 * @description 查看归档model
 *
 */
@Data
public class RdsJudicialArchiveReadModel {
	private String id;
	private String archive_id;
	private String read_per;
	private String read_date;
}
