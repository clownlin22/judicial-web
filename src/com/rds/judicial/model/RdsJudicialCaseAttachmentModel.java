package com.rds.judicial.model;

/**
 * 
 * @class RdsJudicialCaseAttachmentModel
 * @discription 附件model
 * @author fushaoming
 * @date 2015年4月7日
 */
import lombok.Data;

@Data
public class RdsJudicialCaseAttachmentModel {
	private String id;
	private String case_id;
	private String case_code;
	private String attachment_path;
	private String attachment_date;
	private int attachment_type;
	private String case_receiver;
	/**
	 * 是否需要副本
	 */
	private String count;
	
	private String create_per;
	
	private String create_date;
	
	private String username;
	
	private String verify_state;
}
