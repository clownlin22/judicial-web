package com.rds.judicial.service.impl;

import com.rds.judicial.mapper.RdsJudicialIdentifyPerMapper;
import com.rds.judicial.model.RdsJudicialIdentifyModel;
import com.rds.judicial.service.RdsJudicialIdentifyPerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/10/26
 */
@Service("RdsJudicialIdentifyPerService")
public class RdsJudicialIdentifyPerServiceImpl implements
        RdsJudicialIdentifyPerService {
    @Autowired
    RdsJudicialIdentifyPerMapper rdsJudicialIdentifyPerMapper;

    @Override
    public List<RdsJudicialIdentifyModel> queryIdentify(String laboratory_no){
        return rdsJudicialIdentifyPerMapper.queryIdentify(laboratory_no);
    }

    @Override
    public int insertCaseToIdentify(Map<String, Object> params){
        return rdsJudicialIdentifyPerMapper.insertCaseToIdentify(params);
    }

    @Override
    public List<RdsJudicialIdentifyModel> queryIdentifyByCaseId(String case_id) {
        return rdsJudicialIdentifyPerMapper.queryIdentifyByCaseId(case_id);
    }

	@Override
	public int queryIdentifynameByCaseId(String case_id) {
		return rdsJudicialIdentifyPerMapper.queryIdentifynameByCaseId(case_id);
	}
}
