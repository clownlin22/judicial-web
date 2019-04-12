package com.rds.judicial.service;

import com.rds.judicial.model.RdsJudicialReagentsModel;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/9/7
 */
public interface RdsJudicialReagentsService {
    void insert(Map<String,Object> params);

    int update(Map<String,Object> params);

    int delete(Map<String,Object> params);

    List<Object> queryAll(Map<String,Object> params);

    RdsJudicialReagentsModel queryOneReagent(Map<String,Object> params);

    int  queryAllCount(Map<String,Object> params);

    String queryExtFlag(Map<String,Object> params);

    TreeSet<String> queryReagentAtelier(String reagent_name);

    String queryOriginalReagentAtelier(String reagent_name);

    Map<String,Object> queryReagentByCaseCode(String case_code);

    String[] queryOrder(Map<String,Object> params);
    
	List<Map<String, String>>  newqueryOrder(Map<String,Object> params);
}
