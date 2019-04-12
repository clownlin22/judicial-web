package com.rds.mail.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.mail.mapper.RdsMailDicValuesMapper;
import com.rds.mail.model.RdsMailKeyValuesModel;
import com.rds.mail.service.RdsMailDicValuesService;

@Service
public class RdsMailDicValuesServiceImple implements RdsMailDicValuesService{

	@Autowired
	private RdsMailDicValuesMapper RdsMailDicValuesMapper;
	
	@Override
	public List<RdsMailKeyValuesModel> getMailTypes() {
		return RdsMailDicValuesMapper.getMailTypes();
	}
	
}
