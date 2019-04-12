package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.rds.judicial.model.RdsJudicialKeyValueModel;


public interface RdsJudicialAgentService extends RdsJudicialBaseService {
	/**
	 * 根据usertype查询所有用户信息
	 * @param params
	 * @return
	 */
	public List<RdsJudicialKeyValueModel> queryUserByType(Map<String, Object> params);
	
	public Object queryAgent(Map<String, Object> params);
	
	public int queryAgentCount(Map<String, Object> params);
	
	public Object queryAttachment(Map<String, Object> params);
	
	public boolean deleteAttachment(Map<String, Object> params);
	
	public Map<String, Object> uploadAttachment(String case_id, MultipartFile[] files,
			int[] filetype, String userid) throws Exception;
}
