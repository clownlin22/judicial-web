package com.rds.judicial.mapper;

import com.rds.judicial.model.RdsJudicialReagentsModel;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/9/7
 */
public interface RdsJudicialReagentsMapper extends RdsJudicialExperimentBaseMapper{

    RdsJudicialReagentsModel queryOneReagent(Map<String,Object> params);

    String queryExtFlag(Map<String,Object> params);

    String queryReagentAtelier(String reagent_name);

    Map<String,Object> queryReagentByCaseCode(String case_code);

    String[] queryOrder(Map<String,Object> params);
    
	List<Map<String, String>>  newqueryOrder(Map<String,Object> params);

}
