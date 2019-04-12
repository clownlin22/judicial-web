package com.rds.upc.mapper;

import com.rds.upc.model.RdsUpcLaboratoryModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/8/31
 */
public interface RdsUpcLaboratoryMapper {
    void insertLaboratory(Map<String,Object> params);

    List<RdsUpcLaboratoryModel> queryLaboratory(Map<String,Object> params);

    int queryCountLaboratory(Map<String,Object> params);

    int updateLaboratory(Map<String,Object> params);

    int deleteLaboratory(int laboratory_no);
}
