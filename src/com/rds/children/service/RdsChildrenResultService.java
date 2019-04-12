package com.rds.children.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.rds.children.model.RdsChildrenResponse;

public interface RdsChildrenResultService {

	RdsChildrenResponse getResultInfo(Map<String, Object> params);

	Map<String, Object> addCaseResult(MultipartFile[] files,Map<String, Object> params) throws Exception;

	Map<String, Object> getLoucsInfo(Map<String, Object> params);

	Map<String, Object> uploadSampleByIdentify(Map<String, Object> params) throws Exception;

	Map<String, Object> addCaseResultByHand(Map<String, Object> params) throws Exception;
}
