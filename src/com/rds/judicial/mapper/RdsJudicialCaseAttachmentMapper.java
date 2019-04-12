package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;



/**
 * 
 * @author fushaoming
 * 2015年4月8日
 */
import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;

public interface RdsJudicialCaseAttachmentMapper extends RdsJudicialBaseMapper {

	public List<RdsJudicialCaseAttachmentModel> getAttachMent(
			Map<String, Object> params);
	public List<RdsJudicialCaseAttachmentModel> getAttachMentOne(
			Map<String, Object> params);

	int insertAttachment(
			RdsJudicialCaseAttachmentModel rdsJudicialCaseAttachmentModel);

	int queryCount(Map<String, Object> params);

	List<RdsJudicialCaseAttachmentModel> queryAllPage(Map<String, Object> params);
	
	List<RdsJudicialCaseAttachmentModel> queryAll(Map<String, Object> params);
	
	public String queryPathById(String id);

	public RdsJudicialCaseAttachmentModel queryAttachmentByType(
			Map<String, Object> params);

	List<String> isFileExist(RdsJudicialCaseAttachmentModel attachmentModel);

	int updateFile(RdsJudicialCaseAttachmentModel attachmentModel);

	public int updateAllAttachment(List<String> params);

	public List<RdsJudicialCaseAttachmentModel> getAllCaseAttachment(
			List<String> case_code);
	
	int deleteFile(Map<String, Object> params);
}
