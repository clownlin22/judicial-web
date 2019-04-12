package com.rds.judicial.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rds.judicial.model.RdsJudicialVerifyCaseInfoModel;

public interface RdsJudicialArchiveService {
	public Map<String, Object> doArchive(Map<String, Object> params, HttpServletRequest request) throws ParseException;

	public Map<String, Object> queryAll(Map<String, Object> params);

	public Map<String, Object> readSave(Map<String, Object> params) throws ParseException;

	public Map<String, Object> queryRead(Map<String, Object> pageSet);
	
	public Map<String, Object> queryMailCount(Map<String, Object> params);

	public List<RdsJudicialVerifyCaseInfoModel> queryAllCase(Map<String, Object> params);

	public int queryALLCasseCount(Map<String, Object> params);
}
