package com.rds.upc.service.impl;

import com.rds.upc.mapper.RdsUpcLaboratoryMapper;
import com.rds.upc.model.RdsUpcLaboratoryModel;
import com.rds.upc.service.RdsUpcLaboratoryService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/8/31
 */
@Service("RdsUpcLaboratoryService")
public class RdsUpcLaboratoryServiceImpl implements RdsUpcLaboratoryService{

    @Autowired
    @Setter
    RdsUpcLaboratoryMapper rdsUpcLaboratoryMapper;

    @Override
    public int queryCountLaboratory(Map<String, Object> params) {
        return rdsUpcLaboratoryMapper.queryCountLaboratory(params);
    }

    @Override
    public List<RdsUpcLaboratoryModel> queryLaboratory(Map<String, Object> params) {
        return rdsUpcLaboratoryMapper.queryLaboratory(params);
    }

    @Override
    public void insertLaboratory(Map<String, Object> params) {
        rdsUpcLaboratoryMapper.insertLaboratory(params);
    }

    @Override
    public int deleteLaboratory(int laboratory_no) {
        return rdsUpcLaboratoryMapper.deleteLaboratory(laboratory_no);
    }

    @Override
    public int updateLaboratory(Map<String,Object> params) {
        return rdsUpcLaboratoryMapper.updateLaboratory(params);
    }
}
