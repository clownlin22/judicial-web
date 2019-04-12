package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rds.judicial.model.RdsJudicialCaseProcessModel;

@Component("JudicialOther")
public interface RdsJudicialOtherMapper {

    int updateCaseInfoVerifyState(Map<String,Object> params);
    
    List<RdsJudicialCaseProcessModel> queryExport(Map<String,Object> params);
}
