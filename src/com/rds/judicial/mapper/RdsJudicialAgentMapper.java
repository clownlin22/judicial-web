package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.judicial.model.RdsJudicialKeyValueModel;

@Component("RdsJudicialAgentMapper")
public interface RdsJudicialAgentMapper extends RdsJudicialBaseMapper{
	public List<RdsJudicialKeyValueModel> queryUserByType(Map<String, Object> params);
	
	public List<Object> queryAgent(Map<String, Object> params);
	
	public int queryAgentCount(Map<String, Object> params);
	
	public List<Object> queryAttachment(Map<String, Object> params);
	
	public int deleteAttachment(Map<String, Object> params);
	
	public int uploadAttachment(RdsJudicialCaseAttachmentModel model);

}
