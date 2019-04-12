package com.rds.bacera.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rds.bacera.model.RdsBaceraCaseAttachmentModel;

@Component("RdsBaceraAbilityMapper")
public interface RdsBaceraAbilityMapper extends RdsBaceraBaseMapper{
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	
	public int insertAbilityAttachment(RdsBaceraCaseAttachmentModel model);
	
	public String queryAbilityPathById(String attachment_id);
	
	public int insertAbilityDepartment(Object params);
	
	public int updateAbilityDepartment(Object params);
	
	public List<RdsBaceraCaseAttachmentModel> queryAttacmByAbility(Object params);
	
	public int delAttachment(Object params);
}

