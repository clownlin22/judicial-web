package com.rds.alcohol.mapper;

import java.util.List;
import java.util.Map;

import com.rds.alcohol.model.RdsAlcoholAttachmentModel;
import com.rds.children.model.RdsChildrenCasePhotoModel;

public interface RdsAlcoholAttachmentMapper {

	List<Map<String, Object>> getAttachment(Map<String, Object> params);

	int getAttachmentCount(Map<String, Object> params);

	String getCaseID(String case_code);

	int insertAttachment(Map<String, Object> params);

	Map<String, Object> getPrintAtt(String att_id);

	int deleteCasePhoto(RdsAlcoholAttachmentModel pmodel);

	List<RdsChildrenCasePhotoModel> getAtt(Map<String, Object> params);

	int insertHeadPhoto(RdsAlcoholAttachmentModel pmodel);

	int updatephoto(RdsAlcoholAttachmentModel pmodel);

	int deletAttInfo(Map<String, Object> params);

}
