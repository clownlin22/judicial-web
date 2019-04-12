package com.rds.narcotics.mapper;

import com.rds.narcotics.model.RdsNarcoticsCaseIdentify;
import com.rds.narcotics.model.RdsNarcoticsDicValueModel;
import com.rds.narcotics.model.RdsNarcoticsIdentifyModel;
import com.rds.narcotics.model.RdsNarcoticsModel;

import java.util.List;
import java.util.Map;

/**
 * @Author: lxy
 * @Date: 2019/3/20 10:46
 */
public interface RdsNarcoticsMapper {

    List<RdsNarcoticsDicValueModel> getIdentificationPer();

    boolean addCaseInfo(RdsNarcoticsModel rdsNarcoticsModel);

    List<RdsNarcoticsModel> getCaseInfo(Map<String, Object> params);

    int countCaseInfo(Map<String, Object> params);

    boolean addCaseIdentify(RdsNarcoticsCaseIdentify rdsNarcoticsCaseIdentify);

    Integer exsitCaseNum(String case_num);

    boolean updateCaseInfo(RdsNarcoticsModel rdsNarcoticsModel);

    boolean deletecaseInfo(Map<String, Object> params);

    List<RdsNarcoticsIdentifyModel> getIdentificationPerByCaseId(Map<String, Object> params);

    RdsNarcoticsModel getCaseInfoByCaseId(Map<String, Object> params);

    boolean deleteCaseIdentifyByCaseId(String case_id);
}
