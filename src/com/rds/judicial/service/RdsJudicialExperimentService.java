package com.rds.judicial.service;

import com.rds.judicial.model.RdsJudicialExperimentModel;
import com.rds.judicial.model.RdsJudicialInstrumentProtocolModel;
import com.rds.judicial.model.RdsJudicialResultsGroupModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author linys
 * @ClassName: RdsJudicialExperimentService
 * @Description: 试验管理service
 * @date 2015年4月7日 上午10:28:24
 */
public interface RdsJudicialExperimentService {
    List<Object> queryAll(Map<String, Object> params);
    
    List<Object> queryAllBySample(Map<String, Object> params);
    int queryAllCountBySample(Map<String, Object> params);
    
    int queryAllCount(Map<String, Object> params);

    int update(Map<String, Object> params);

    void insert(Map<String, Object> params);

    int delete(Map<String, Object> params);

    int updatePlaces(Map<String, Object> params);

    String queryPlaces(Map<String, Object> params);

    String exportTxt(String experiment_no,String laboratory_no) throws Exception;

    //String printTxt(String experiment_no) throws IOException;

    Map<Long,String> getPlaces(String experiment_no,String laboratory_no) throws IOException;

    int updateReagentName(Map<String, Object> params);

    int experimentCount(Map<String, Object> params);

    List<RdsJudicialInstrumentProtocolModel> queryInstrumentProtocol(Map<String, Object> params);

    int queryCountInstrumentProtocol(Map<String, Object> params);

    int updateInstrumentProtocol(Map<String, Object> instrument_protocol);

    int deleteInstrumentProtocol(Map<String, Object> params);

    List<RdsJudicialResultsGroupModel> queryResultsGroup(Map<String, Object> params);

    int queryCountResultsGroup(Map<String, Object> params);

    int updateResultsGroup(Map<String, Object> results_group);

    int deleteResultsGroup(Map<String, Object> params);

    void insertResultsGroup(Map<String, Object> params);

    void insertInstrumentProtocol(Map<String, Object> params);

    int deleteSample(Map<String, Object> params);

    int deleteSampleData(Map<String, Object> params);

}
