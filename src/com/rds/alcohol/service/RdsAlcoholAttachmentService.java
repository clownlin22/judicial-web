package com.rds.alcohol.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.rds.alcohol.model.RdsAlcoholAttachmentModel;
import com.rds.children.model.RdsChildrenCasePhotoModel;

public interface RdsAlcoholAttachmentService {

	Map<String, Object> getAttachment(Map<String, Object> params);

	Map<String, Object> upload(String case_code, MultipartFile[] files);

	Map<String, Object> getPrintAttachment(String att_id);

	List<RdsChildrenCasePhotoModel> getAtt(Map<String, Object> params);

	Map<String, Object> photoUpload(RdsAlcoholAttachmentModel pmodel, MultipartFile[] headPhoto);

	Map<String, Object> photoUploadt(RdsAlcoholAttachmentModel pmodel, MultipartFile[] headPhoto);

	boolean deletAttInfo(Map<String, Object> params);

	Map<String, Object> photoUpload(RdsAlcoholAttachmentModel pmodel);

	Map photoUploadtd(RdsAlcoholAttachmentModel pmodel );

}
