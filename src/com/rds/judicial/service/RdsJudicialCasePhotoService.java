package com.rds.judicial.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.rds.judicial.model.RdsJudicialResponse;

public interface RdsJudicialCasePhotoService {

	RdsJudicialResponse getPrintCaseInfo(Map<String, Object> params);

	Map<String, Object> uploadTempPhoto(MultipartFile[] casephoto, String case_id,
			HttpSession session);
	
	public void getImg(HttpServletResponse response, String filename);

	Map<String, Object> turnImg(Map<String, Object> params);

	Map<String, Object> getCaseReceiver(Map<String, Object> params,HttpSession session);

	Map<String, Object> savephoto(Map<String, Object> params,
			HttpSession session);

	Map<String, Object> getAllCasePhoto(Map<String, Object> params);
}
