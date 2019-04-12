package com.rds.judicial.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.judicial.model.RdsJudicialResponse;

public interface RdsJudicialCaseAttachmentService {

	List<RdsJudicialCaseAttachmentModel> getAttachMent(
			Map<String, Object> params);
	
	List<RdsJudicialCaseAttachmentModel> getAttachMentOne(
			Map<String, Object> params);

	void getImg(HttpServletResponse response, String filename);

	Map<String, Object> uploadAttachment(String case_id, String case_code,
			MultipartFile[] file, String filetype) throws IOException;

	Map<String, Object> queryAllPage(Map<String, Object> params);

	List<RdsJudicialCaseAttachmentModel> queryAll(Map<String, Object> params);

	Map<String, Object> upload(String case_code, MultipartFile[] files,int[] filetype,String userid)
			throws Exception;

	void download(HttpServletResponse response, String filename);

	void sampleAttachmentUpload(String case_code, List<File> files)
			throws Exception;

    void pdfAttachmentUpload(String case_code, List<File> files)
            throws Exception;

	RdsJudicialResponse getPrintAttachment(Map<String, Object> params);

	boolean printAttachment(Map<String, Object> params);

	Map<String, Object> turnImg(Map<String, Object> params);

	Map<String, Object> getCaseInfo(Map<String, Object> params);

	boolean updateAllAttachment(Map<String, Object> params);

	void printAllCaseAttachment(String case_code, String count, HttpServletRequest request);
	
	boolean deleteFile(Map<String, Object> params);
	
	int queryCount(Map<String, Object> params);

}
