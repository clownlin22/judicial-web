package com.rds.judicial.mapper;

import com.rds.judicial.model.RdsJudicialCaseExceptionModel;
import com.rds.judicial.model.RdsJudicialExperimentModel;
import com.rds.judicial.model.RdsJudicialInstrumentProtocolModel;
import com.rds.judicial.model.RdsJudicialResultsGroupModel;

import java.util.List;
import java.util.Map;

/**
 * Created by linys on 2015/4/7.
 */
public interface RdsJudicialExperimentMapper extends RdsJudicialExperimentBaseMapper{

    int updatePlaces(Map<String, Object> params);

    String queryPlaces(Map<String, Object> params);

    int isexperimented(Map<String, Object> params);

    int experimentCount(Map<String, Object> params);

    int updateReagentName(Map<String, Object> params);

    List<RdsJudicialInstrumentProtocolModel> queryInstrumentProtocol(Map<String, Object> params);

    int queryCountInstrumentProtocol(Map<String, Object> params);

    int updateInstrumentProtocol(Map<String, Object> params);

    int deleteInstrumentProtocol(Map<String, Object> param);

    void insertInstrumentProtocol(Map<String, Object> params);

    List<RdsJudicialResultsGroupModel> queryResultsGroup(Map<String, Object> params);

    int queryCountResultsGroup(Map<String, Object> params);

    int updateResultsGroup(Map<String, Object> params);

    int deleteResultsGroup(Map<String, Object> param);

    void insertResultsGroup(Map<String, Object> params);

    int deleteSample(Map<String, Object> params);

    int deleteSampleData(Map<String, Object> params);
    
    
	List<RdsJudicialCaseExceptionModel> getExceptionCaseId(Map<String, Object> params);

}
