package com.rds.judicial.service.impl;

import com.rds.judicial.mapper.RdsJudicialSampleMapper;
import com.rds.judicial.model.RdsJudicialPhoneHistoryModel;
import com.rds.judicial.model.RdsJudicialSampleResultModel;
import com.rds.judicial.service.RdsJudicialSampleService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/9/2
 */
@Service("RdsJudicialSampleService")
public class RdsJudicialSampleServiceImpl implements RdsJudicialSampleService {
    @Setter
    @Autowired
    private RdsJudicialSampleMapper rdsJudicialSampleMapper;
    @Override
    public List<Object> queryAll(Map<String, Object> params) {
        List<Object> list =
                rdsJudicialSampleMapper.queryAll(params);
        completeModel(list,params);
        return list;
    }

    @Override
    public int queryAllCount(Map<String, Object> params) {
        return rdsJudicialSampleMapper.queryAllCount(params);
    }


    @Override
    public RdsJudicialSampleResultModel queryOneRecord(Map<String, Object> params) {
        RdsJudicialSampleResultModel rdsJudicialSampleResultModel =
                rdsJudicialSampleMapper.queryOneRecord(params);
        rdsJudicialSampleResultModel.setRecord(queryOneRecordData(params));
        return rdsJudicialSampleResultModel;
    }

    @Override
    public Map<String,Object> queryOneRecordData(Map<String,Object> params){
        List<Map<String,Object>> one_record = rdsJudicialSampleMapper.queryRecordData(params);
        Map<String,Object> record = new HashMap<>();
        for(Map<String,Object> map:one_record){
            record.put((String)map.get("name"),map.get("value"));
        }
        return record;
    }

    @Override
    public List<RdsJudicialSampleResultModel> queryByExperimentNo(Map<String, Object> params) {
        List<RdsJudicialSampleResultModel> list =
                rdsJudicialSampleMapper.queryByExperimentNo(params);
        completeModel(list,params);
        return list;
    }

    private List<? extends Object> completeModel(
            List<? extends Object> list,Map<String, Object> params){
        for(Object obj:list){
            RdsJudicialSampleResultModel rdsJudicialSampleResultModel = (RdsJudicialSampleResultModel)obj;
            params.put("sample_code",rdsJudicialSampleResultModel.getSample_code());
            List<Map<String,Object>> one_record = rdsJudicialSampleMapper.queryRecordData(params);
            Map<String,Object> record = new HashMap<>();
            for(Map<String,Object> map:one_record){
                record.put((String)map.get("name"),map.get("value"));
            }
            rdsJudicialSampleResultModel.setRecord(record);
        }
        return list;
    }

    @Override
    public List<RdsJudicialSampleResultModel> queryRecordsByCaseCode(Map<String,Object> paramsout) {
        List<RdsJudicialSampleResultModel> rdsJudicialSampleResultModels =
                rdsJudicialSampleMapper.queryByCaseCode(paramsout);
        for(RdsJudicialSampleResultModel
                rdsJudicialSampleResultModel:rdsJudicialSampleResultModels) {
            Map<String,Object> params = new HashMap<>();
            params.put("sample_code",rdsJudicialSampleResultModel.getSample_code());
            params.put("experiment_no",rdsJudicialSampleResultModel.getExperiment_no());
            rdsJudicialSampleResultModel.setRecord(queryOneRecordData(params));
        }
        return rdsJudicialSampleResultModels;
    }

    @Override
    public int queryHistoryCount(Map<String, Object> params) {
        return rdsJudicialSampleMapper.queryHistoryCount(params);
    }

    @Override
    public List<RdsJudicialPhoneHistoryModel> queryHistory(Map<String, Object> params) {
        return rdsJudicialSampleMapper.queryHistory(params);
    }
}
