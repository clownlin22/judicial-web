package com.rds.judicial.mapper;

import java.util.List;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/12/14
 */
public interface RdsJudicialExperimentBaseMapper {
    List<Object> queryAll(Object params);

    int queryAllCount(Object params);
    
    int queryAllCountBySample(Object params);
    
    List<Object> queryAllBySample(Object params);

    int update(Object params);

    void insert(Object params);

    int delete(Object params);
}
