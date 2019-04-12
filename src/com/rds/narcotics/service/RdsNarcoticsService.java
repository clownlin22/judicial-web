package com.rds.narcotics.service;

import com.rds.narcotics.model.RdsNarcoticsModel;
import com.rds.narcotics.model.RdsNarcoticsResponse;

import java.util.Map;

/**
 * @Author: lxy
 * @Date: 2019/3/20 10:46
 */
public interface RdsNarcoticsService {

    Map<String,Object> getIdentificationPer();

    boolean addCaseInfo(RdsNarcoticsModel rdsNarcoticsModel);

    RdsNarcoticsResponse getCaseInfo(Map<String, Object> params);

    boolean exsitCaseNum(String case_num);

    boolean updateCaseInfo(RdsNarcoticsModel rdsNarcoticsModel);

    boolean deletecaseInfo(Map<String, Object> params);

    void createNarcoticsDocByCaseId(Map<String, Object> params) throws Exception;


}
