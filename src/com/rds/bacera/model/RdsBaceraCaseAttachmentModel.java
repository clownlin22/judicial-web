package com.rds.bacera.model;

/**
 * 
 * @class RdsJudicialCaseAttachmentModel
 * @discription 附件model
 * @author fushaoming
 * @date 2015年4月7日
 */
import lombok.Data;

@Data
public class RdsBaceraCaseAttachmentModel {
	private String attachment_id;
	private String ability_id;
	private String attachment_path;
	private int attachment_type;
	private String create_per;
	private String create_date;
	private String appraisal_cpp_id;
}
