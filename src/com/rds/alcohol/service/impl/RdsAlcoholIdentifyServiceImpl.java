package com.rds.alcohol.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.rds.alcohol.mapper.RdsAlcoholIdentifyMapper;
import com.rds.alcohol.model.RdsAlcoholIdentifyCaseinfo;
import com.rds.alcohol.model.RdsAlcoholIdentifyModel;
import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.service.RdsAlcoholIdentifyService;
import com.rds.code.utils.uuid.UUIDUtil;

@Service
@Transactional
public class RdsAlcoholIdentifyServiceImpl implements  RdsAlcoholIdentifyService{


	@Autowired
	private RdsAlcoholIdentifyMapper RdsAlcoholIdentifyMapper;

	@Override
	public RdsAlcoholResponse getIdentifyInfo(Map<String, Object> params) {
		RdsAlcoholResponse response=new RdsAlcoholResponse();
		List<RdsAlcoholIdentifyModel> list=RdsAlcoholIdentifyMapper.getIdentifyInfo(params);
		int count=RdsAlcoholIdentifyMapper.getcount(params);
		response.setCount(count);
		response.setItems(list);
		return response;
	}

	@Override
	public Integer insert(Map<String, Object> params) {

		String uuid = UUIDUtil.getUUID();
		params.put("per_id", uuid);

		DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    

		params.put("per_sys",  dFormat.format(System.currentTimeMillis()));

		return RdsAlcoholIdentifyMapper.insertinfo(params);
	}

	@Override
	public int delete(Map<String, Object> params) {
		 
		return RdsAlcoholIdentifyMapper.delete(params);
	}

	@Override
	public int update(Map<String, Object> params) {
		return RdsAlcoholIdentifyMapper.update(params);
	}

	@Override
	public boolean exsitper_code(Object per_code) {
		int count = RdsAlcoholIdentifyMapper.exsitper_code(per_code);
		if (count > 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean exsitper_name(Object per_name) {
		int count = RdsAlcoholIdentifyMapper.exsitper_name(per_name);
		if (count > 0) {
			return false;
		}
		return true;
	}

	@Override
	public String selectper_id(String case_checkper) {
		String per_id=RdsAlcoholIdentifyMapper.selectper_id(case_checkper);
		return per_id;
	}

	@Override
	public boolean add(RdsAlcoholIdentifyCaseinfo ps) {
		int count=RdsAlcoholIdentifyMapper.add(ps);
		if (count > 0) {
			return false;
		}
		return true;
	}

	@Override
	public void deleteCaseIdetity(String case_id) {
		RdsAlcoholIdentifyMapper.deleteCaseIdetity(case_id);
		
	}

	 

}
