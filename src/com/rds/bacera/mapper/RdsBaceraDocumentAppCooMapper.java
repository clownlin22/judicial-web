package com.rds.bacera.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rds.bacera.model.RdsBaceraCaseAttachmentModel;

@Component("RdsBaceraDocumentAppCooMapper")
public interface RdsBaceraDocumentAppCooMapper extends RdsBaceraBaseMapper {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);

	public int insertDocumentAppCooAttachment(RdsBaceraCaseAttachmentModel model);

	public String queryDocumentAppCooPathById(String attachment_id);

	public List<RdsBaceraCaseAttachmentModel> queryAttacmByDocumentAppCoo(
			Object params);

	public int delAttachment(Object params);
}
