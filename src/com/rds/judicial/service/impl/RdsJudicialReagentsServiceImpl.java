package com.rds.judicial.service.impl;

import com.rds.judicial.mapper.RdsJudicialReagentsMapper;
import com.rds.judicial.model.RdsJudicialReagentsModel;
import com.rds.judicial.service.RdsJudicialReagentsService;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/9/7
 */
@Service("RdsJudicialReagentsService")
public class RdsJudicialReagentsServiceImpl
        implements RdsJudicialReagentsService{

    @Autowired
    @Setter
    private RdsJudicialReagentsMapper rdsJudicialReagentsMapper;

    @Override
    public void insert(Map<String, Object> params) {
        rdsJudicialReagentsMapper.insert(params);
    }

    @Override
    public int queryAllCount(Map<String, Object> params) {
        return rdsJudicialReagentsMapper.queryAllCount(params);
    }

    @Override
    public List<Object> queryAll(Map<String, Object> params) {
        return rdsJudicialReagentsMapper.queryAll(params);
    }

    @Override
    public int delete(Map<String, Object> params) {
        return rdsJudicialReagentsMapper.delete(params);
    }

    @Override
    public int update(Map<String, Object> params) {
        return rdsJudicialReagentsMapper.update(params);
    }

    @Override
    public RdsJudicialReagentsModel queryOneReagent(Map<String, Object> params) {
        return rdsJudicialReagentsMapper.queryOneReagent(params);
    }

    @Override
    public String queryExtFlag(Map<String, Object> params) {
        return rdsJudicialReagentsMapper.queryExtFlag(params);
    }

    @Override
    public TreeSet<String> queryReagentAtelier(String reagent_name) {
        String atelier = rdsJudicialReagentsMapper.queryReagentAtelier(reagent_name);
        String[] array = atelier.split(",");
        TreeSet<String> set = new TreeSet<String>();
        for(int i=0;i<array.length;i++){
            set.add(array[i]);
        }
        return set;
    }

    @Override
    public String queryOriginalReagentAtelier(String reagent_name) {
        String atelier = rdsJudicialReagentsMapper.queryReagentAtelier(reagent_name);
        return atelier;
    }

    @Override
    public Map<String, Object> queryReagentByCaseCode(String case_code) {
        return rdsJudicialReagentsMapper.queryReagentByCaseCode(case_code);
    }

    @Override
    public String[] queryOrder(Map<String,Object> params) {
        return rdsJudicialReagentsMapper.queryOrder(params);
    }
    @Override
    public  	List<Map<String, String>> newqueryOrder(Map<String,Object> params) {
        return rdsJudicialReagentsMapper.newqueryOrder(params);
    }
}
