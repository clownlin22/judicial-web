package com.rds.alcohol.service.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.alcohol.mapper.RdsAlcoholArchiveMapper;
import com.rds.alcohol.model.RdsAlcoholArchiveModel;
import com.rds.alcohol.model.RdsAlcoholArchiveReadModel;
import com.rds.alcohol.model.RdsAlcoholCaseInfoModel;
import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.service.RdsAlcoholArchiveService;
import com.rds.code.utils.uuid.UUIDUtil;

@Transactional
@Service
public class RdsAlcoholArchiveServiceImpl implements RdsAlcoholArchiveService{

	@Autowired
	private RdsAlcoholArchiveMapper RdsAlcoholArchiveMapper;
	
	@Override
	public RdsAlcoholResponse getCaseInfo(Map<String, Object> params) {
		RdsAlcoholResponse response=new RdsAlcoholResponse();
		List<RdsAlcoholCaseInfoModel> caseInfoModels=RdsAlcoholArchiveMapper.getCaseInfo(params);
		int count=RdsAlcoholArchiveMapper.countCaseInfo(params);
		response.setCount(count);
		response.setItems(caseInfoModels);
		return response;
	}

	@Override
	public boolean addArchiveInfo(Map<String, Object> params) {
		try {
			params.put("archive_id", UUIDUtil.getUUID());
			params.put("archive_date", new SimpleDateFormat("yyyy-MM-dd")
					.parse((String) params.get("archive_date")));
			params.put("archive_code",
					new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
			params.put("archive_path",params.get("case_code").toString() + File.separatorChar);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int result=RdsAlcoholArchiveMapper.addArchiveInfo(params);
		if(result>0){
			RdsAlcoholArchiveMapper.updateCaseState(params);
			return true;
		}
		return false;
	}

	@Override
	public RdsAlcoholResponse getArchiveInfo(Map<String, Object> params) {
		RdsAlcoholResponse response=new RdsAlcoholResponse();
		List<RdsAlcoholArchiveModel> rdsAlcoholArchiveModels=RdsAlcoholArchiveMapper.getArchiveInfo(params);
		int count=RdsAlcoholArchiveMapper.countArchiveInfo(params);
		response.setCount(count);
		response.setItems(rdsAlcoholArchiveModels);
		return response;
	}

	@Override
	public List<RdsAlcoholArchiveReadModel>  getReadInfo(Map<String, Object> params) {
		List<RdsAlcoholArchiveReadModel>  rdsAlcoholArchiveReadModels=RdsAlcoholArchiveMapper.getReadInfo(params);
		return rdsAlcoholArchiveReadModels;
	}

	@Override
	public boolean addReadInfo(Map<String, Object> params) {
		params.put("id", UUIDUtil.getUUID());
		int result=RdsAlcoholArchiveMapper.addReadInfo(params);
		if(result>0){
			return true;
		}
		return false;
	}

}
