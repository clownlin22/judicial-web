package com.rds.bacera.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.rds.bacera.model.RdsBaceraCaseAttachmentModel;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * service
 * 
 * @author yxb
 *
 */
public interface RdsBaceraAbilityService extends RdsBaceraBaseService {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);

	void exportAbility(Map<String, Object> params, HttpServletResponse response)
			throws Exception;

	Map<String, Object> saveCaseInfo(Map<String, Object> params, RdsUpcUserModel user);

	Map<String, Object> updateCaseInfo(Map<String, Object> params, RdsUpcUserModel user);
	
	void download(HttpServletResponse response, String attachment_id);
	
	public List<RdsBaceraCaseAttachmentModel> queryAttacmByAbility(Object params);
	
	public int delAttachment(Object param);
	
	public void uploadAbilityAttachment(String ability_id, String ability_num,
			MultipartFile[] files, int[] filetype, String userid) throws Exception;

}
