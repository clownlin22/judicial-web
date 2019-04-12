package com.rds.judicial.service.impl;

import com.rds.code.utils.StringUtils;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialExceptionMapper;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialExceptionModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialExceptionService;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/16
 */
@Service("RdsJudicialExceptionService")
public class RdsJudicialExceptionServiceImpl implements RdsJudicialExceptionService {
    @Autowired
    RdsJudicialExceptionMapper rdsJudicialExceptionMapper;

    @Override
    public int queryAllCount(Map<String, Object> params) {
        return rdsJudicialExceptionMapper.queryAllCount(params);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int update(Map<String, Object> params) {
        return rdsJudicialExceptionMapper.update(params);
    }

    @Override
    public List<Object> queryAll(Map<String, Object> params) {
        return rdsJudicialExceptionMapper.queryAll(params);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void insert(String exception_id,String case_code,
                             String sample_code1,String sample_code2,String laboratory_no){
        Map<String, Object> exceptionParams = new HashMap<String, Object>();
        exceptionParams.put("uuid", UUIDUtil.getUUID());
        exceptionParams.put("exception_id",exception_id);
        exceptionParams.put("case_code", case_code);
        exceptionParams.put("sample_code1", sample_code1);
        exceptionParams.put("sample_code2", sample_code2);
        exceptionParams.put("trans_date", StringUtils.dateToSecond(new Date()));
        exceptionParams.put("choose_flag", 1);
        exceptionParams.put("laboratory_no", laboratory_no);
        rdsJudicialExceptionMapper.insert(exceptionParams);
    }

    @Override
    public int queryCountCrossCompare(Map<String, Object> params) {
        return rdsJudicialExceptionMapper.queryCountCrossCompare(params);
    }

    @Override
    public List<RdsJudicialExceptionModel> queryCrossCompare(Map<String, Object> params) {
        return rdsJudicialExceptionMapper.queryCrossCompare(params);
    }
}
