package com.rds.judicial.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialInstrumentProtocolModel;
import com.rds.judicial.model.RdsJudicialResultsGroupModel;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialExperimentMapper;
import com.rds.judicial.model.RdsJudicialExperimentModel;
import com.rds.judicial.service.RdsJudicialExperimentService;

/**
 * @author linys
 */
@Service("RdsJudicialExperimentService")
public class RdsJudicialExperimentServiceImpl implements RdsJudicialExperimentService {
    @Setter
    @Autowired
    RdsJudicialExperimentMapper rdsJudicialExperimentMapper;

    private String results_Group_1;
    private String instrument_Protocol_1;

    @Override
    public List<Object> queryAll(Map<String, Object> params) {
        params.put("end", (Integer) params.get("page") * (Integer) params.get("limit"));
        List<Object> model = rdsJudicialExperimentMapper.queryAll(params);
        return model;
    }

    @Override
    public List<Object> queryAllBySample(Map<String, Object> params) {
        params.put("end", (Integer) params.get("page") * (Integer) params.get("limit"));
        List<Object> model = rdsJudicialExperimentMapper.queryAll(params);
        return model;
    }  
    
    @Override
    public int queryAllCountBySample(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.queryAllCount(params);
    }
    
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void insert(Map<String, Object> params) {
        params.put("uuid", UUIDUtil.getUUID());
        rdsJudicialExperimentMapper.insert(params);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int update(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.update(params);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int delete(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.delete(params);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int updatePlaces(Map<String, Object> params) {

        return rdsJudicialExperimentMapper.updatePlaces(params);
    }

    @Override
    public int queryAllCount(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.queryAllCount(params);
    }

    @Override
    public int experimentCount(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.experimentCount(params);
    }

    @Override
    public int updateReagentName(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.updateReagentName(params);
    }

    @Override
    public int queryCountResultsGroup(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.queryCountResultsGroup(params);
    }

    @Override
    public List<RdsJudicialResultsGroupModel> queryResultsGroup(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.queryResultsGroup(params);
    }

    @Override
    public int updateResultsGroup(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.updateResultsGroup(params);
    }

    @Override
    public int deleteResultsGroup(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.deleteResultsGroup(params);
    }

    @Override
    public int queryCountInstrumentProtocol(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.queryCountInstrumentProtocol(params);
    }

    @Override
    public List<RdsJudicialInstrumentProtocolModel> queryInstrumentProtocol(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.queryInstrumentProtocol(params);
    }

    @Override
    public int updateInstrumentProtocol(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.updateInstrumentProtocol(params);
    }


    @Override
    public int deleteInstrumentProtocol(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.deleteInstrumentProtocol(params);
    }


    @Override
    public void insertResultsGroup(Map<String, Object> params) {
        rdsJudicialExperimentMapper.insertResultsGroup(params);
    }

    @Override
    public void insertInstrumentProtocol(Map<String, Object> params) {
        rdsJudicialExperimentMapper.insertInstrumentProtocol(params);
    }

    @Override
    public int deleteSample(Map<String, Object> params) {
         rdsJudicialExperimentMapper.deleteSample(params);
        return rdsJudicialExperimentMapper.deleteSampleData(params);
    }

    @Override
    public int deleteSampleData(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.deleteSample(params);
    }

    @Override
    public String queryPlaces(Map<String, Object> params) {
        return rdsJudicialExperimentMapper.queryPlaces(params);
    }



    @Override
    public String exportTxt(String experiment_no,String laboratory_no) throws Exception {
        Map<String,Object> results_Group_1_param = new HashMap<String, Object>();
        results_Group_1_param.put("enable_flag","Y");
        results_Group_1_param.put("laboratory_no",laboratory_no);
        Map<String,Object> instrument_Protocol_1_param = new HashMap<String, Object>();
        instrument_Protocol_1_param.put("enable_flag","Y");
        instrument_Protocol_1_param.put("laboratory_no",laboratory_no);
        if(rdsJudicialExperimentMapper.queryCountInstrumentProtocol(instrument_Protocol_1_param)!=1
                ||rdsJudicialExperimentMapper.queryCountResultsGroup(results_Group_1_param)!=1){
            return "请检查InstrumentProtocol以及ResultsGroup配置信息";
        }
        results_Group_1 = rdsJudicialExperimentMapper.
                queryResultsGroup(results_Group_1_param).get(0).getResults_group();
        instrument_Protocol_1 = rdsJudicialExperimentMapper.
                queryInstrumentProtocol(instrument_Protocol_1_param).get(0).getInstrument_protocol();
        StringBuffer sb = new StringBuffer();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("experiment_no", experiment_no);
        map.put("laboratory_no",laboratory_no);
        List<Object> model = rdsJudicialExperimentMapper.queryAll(map);
        String places = ((RdsJudicialExperimentModel)model.get(0)).getPlaces();
        String[] place = places.split(",");
        sb.append("Container Name\tDescription\tContainerType\tAppType\tOwner\tOperator\t\n");
        sb.append(((RdsJudicialExperimentModel)model.get(0)).getExperiment_no() + "\t\t96-Well\tRegular\t1\t1\t\n");
        sb.append("AppServer\tAppInstance\t\n");
        sb.append("GeneMapper\tGeneMapper_Generic_Instance\t\n");
        sb.append("Well\tSample Name\tComment\tPriority\tSample Type\tSnp Set\tAnalysis Method" +
                "\tPanel\tUser-Defined 3\tSize Standard\tUser-Defined 2\tUser-Defined 1\tResults Group 1\tInstrument Protocol 1\t\n");
        if (!place[0].endsWith("="))
            sb.append("A01\t" + getPlace(place[0].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[12].endsWith("="))
            sb.append("B01\t" + getPlace(place[12].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[24].endsWith("="))
            sb.append("C01\t" + getPlace(place[24].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[36].endsWith("="))
            sb.append("D01\t" + getPlace(place[36].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[48].endsWith("="))
            sb.append("E01\t" + getPlace(place[48].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[60].endsWith("="))
            sb.append("F01\t" + getPlace(place[60].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[72].endsWith("="))
            sb.append("G01\t" + getPlace(place[72].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[84].endsWith("="))
            sb.append("H01\t" + getPlace(place[84].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[1].endsWith("="))
            sb.append("A02\t" + getPlace(place[1].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[13].endsWith("="))
            sb.append("B02\t" + getPlace(place[13].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[25].endsWith("="))
            sb.append("C02\t" + getPlace(place[25].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[37].endsWith("="))
            sb.append("D02\t" + getPlace(place[37].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[49].endsWith("="))
            sb.append("E02\t" + getPlace(place[49].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[61].endsWith("="))
            sb.append("F02\t" + getPlace(place[61].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[73].endsWith("="))
            sb.append("G02\t" + getPlace(place[73].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[85].endsWith("="))
            sb.append("H02\t" + getPlace(place[85].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[2].endsWith("="))
            sb.append("A03\t" + getPlace(place[2].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[14].endsWith("="))
            sb.append("B03\t" + getPlace(place[14].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[26].endsWith("="))
            sb.append("C03\t" + getPlace(place[26].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[38].endsWith("="))
            sb.append("D03\t" + getPlace(place[38].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[50].endsWith("="))
            sb.append("E03\t" + getPlace(place[50].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[62].endsWith("="))
            sb.append("F03\t" + getPlace(place[62].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[74].endsWith("="))
            sb.append("G03\t" + getPlace(place[74].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[86].endsWith("="))
            sb.append("H03\t" + getPlace(place[86].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[3].endsWith("="))
            sb.append("A04\t" + getPlace(place[3].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[15].endsWith("="))
            sb.append("B04\t" + getPlace(place[15].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[27].endsWith("="))
            sb.append("C04\t" + getPlace(place[27].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[39].endsWith("="))
            sb.append("D04\t" + getPlace(place[39].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[51].endsWith("="))
            sb.append("E04\t" + getPlace(place[51].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[63].endsWith("="))
            sb.append("F04\t" + getPlace(place[63].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[75].endsWith("="))
            sb.append("G04\t" + getPlace(place[75].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[87].endsWith("="))
            sb.append("H04\t" + getPlace(place[87].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[4].endsWith("="))
            sb.append("A05\t" + getPlace(place[4].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[16].endsWith("="))
            sb.append("B05\t" + getPlace(place[16].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[28].endsWith("="))
            sb.append("C05\t" + getPlace(place[28].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[40].endsWith("="))
            sb.append("D05\t" + getPlace(place[40].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[52].endsWith("="))
            sb.append("E05\t" + getPlace(place[52].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[64].endsWith("="))
            sb.append("F05\t" + getPlace(place[64].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[76].endsWith("="))
            sb.append("G05\t" + getPlace(place[76].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[88].endsWith("="))
            sb.append("H05\t" + getPlace(place[88].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[5].endsWith("="))
            sb.append("A06\t" + getPlace(place[5].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[17].endsWith("="))
            sb.append("B06\t" + getPlace(place[17].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[29].endsWith("="))
            sb.append("C06\t" + getPlace(place[29].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[41].endsWith("="))
            sb.append("D06\t" + getPlace(place[41].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[53].endsWith("="))
            sb.append("E06\t" + getPlace(place[53].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[65].endsWith("="))
            sb.append("F06\t" + getPlace(place[65].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[77].endsWith("="))
            sb.append("G06\t" + getPlace(place[77].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[89].endsWith("="))
            sb.append("H06\t" + getPlace(place[89].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[6].endsWith("="))
            sb.append("A07\t" + getPlace(place[6].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[18].endsWith("="))
            sb.append("B07\t" + getPlace(place[18].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[30].endsWith("="))
            sb.append("C07\t" + getPlace(place[30].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[42].endsWith("="))
            sb.append("D07\t" + getPlace(place[42].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[54].endsWith("="))
            sb.append("E07\t" + getPlace(place[54].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[66].endsWith("="))
            sb.append("F07\t" + getPlace(place[66].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[78].endsWith("="))
            sb.append("G07\t" + getPlace(place[78].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[90].endsWith("="))
            sb.append("H07\t" + getPlace(place[90].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[7].endsWith("="))
            sb.append("A08\t" + getPlace(place[7].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[19].endsWith("="))
            sb.append("B08\t" + getPlace(place[19].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[31].endsWith("="))
            sb.append("C08\t" + getPlace(place[31].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[43].endsWith("="))
            sb.append("D08\t" + getPlace(place[43].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[55].endsWith("="))
            sb.append("E08\t" + getPlace(place[55].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[67].endsWith("="))
            sb.append("F08\t" + getPlace(place[67].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[79].endsWith("="))
            sb.append("G08\t" + getPlace(place[79].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[91].endsWith("="))
            sb.append("H08\t" + getPlace(place[91].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[8].endsWith("="))
            sb.append("A09\t" + getPlace(place[8].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[20].endsWith("="))
            sb.append("B09\t" + getPlace(place[20].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[32].endsWith("="))
            sb.append("C09\t" + getPlace(place[32].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[44].endsWith("="))
            sb.append("D09\t" + getPlace(place[44].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[56].endsWith("="))
            sb.append("E09\t" + getPlace(place[56].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[68].endsWith("="))
            sb.append("F09\t" + getPlace(place[68].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[80].endsWith("="))
            sb.append("G09\t" + getPlace(place[80].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[92].endsWith("="))
            sb.append("H09\t" + getPlace(place[92].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[9].endsWith("="))
            sb.append("A10\t" + getPlace(place[9].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[21].endsWith("="))
            sb.append("B10\t" + getPlace(place[21].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[33].endsWith("="))
            sb.append("C10\t" + getPlace(place[33].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[45].endsWith("="))
            sb.append("D10\t" + getPlace(place[45].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[57].endsWith("="))
            sb.append("E10\t" + getPlace(place[57].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[69].endsWith("="))
            sb.append("F10\t" + getPlace(place[69].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[81].endsWith("="))
            sb.append("G10\t" + getPlace(place[81].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[93].endsWith("="))
            sb.append("H10\t" + getPlace(place[93].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[10].endsWith("="))
            sb.append("A11\t" + getPlace(place[10].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[22].endsWith("="))
            sb.append("B11\t" + getPlace(place[22].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[34].endsWith("="))
            sb.append("C11\t" + getPlace(place[34].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[46].endsWith("="))
            sb.append("D11\t" + getPlace(place[46].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[58].endsWith("="))
            sb.append("E11\t" + getPlace(place[58].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[70].endsWith("="))
            sb.append("F11\t" + getPlace(place[70].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[82].endsWith("="))
            sb.append("G11\t" + getPlace(place[82].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[94].endsWith("="))
            sb.append("H11\t" + getPlace(place[94].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[11].endsWith("="))
            sb.append("A12\t" + getPlace(place[11].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[23].endsWith("="))
            sb.append("B12\t" + getPlace(place[23].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[35].endsWith("="))
            sb.append("C12\t" + getPlace(place[35].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[47].endsWith("="))
            sb.append("D12\t" + getPlace(place[47].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[59].endsWith("="))
            sb.append("E12\t" + getPlace(place[59].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[71].endsWith("="))
            sb.append("F12\t" + getPlace(place[71].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[83].endsWith("="))
            sb.append("G12\t" + getPlace(place[83].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        if (!place[95].endsWith("="))
            sb.append("H12\t" + getPlace(place[95].split("=")) + "\t\t100\t\t\t\t\t\t\t\t\t" + results_Group_1 + "\t" + instrument_Protocol_1 + "\t\n");
        return sb.toString();
    }

//    @Override
//    public String printTxt(String experiment_no) throws IOException {
//
//        results_Group_1 = PropertiesUtils.readValue(ConfigPath.getWebInfPath()
//                + File.separatorChar + "spring" + File.separatorChar +
//                "properties" + File.separatorChar + "sampletxt.properties", "results_Group_1");
//        instrument_Protocol_1 = PropertiesUtils.readValue(ConfigPath.getWebInfPath()
//                + File.separatorChar + "spring" + File.separatorChar +
//                "properties" + File.separatorChar + "sampletxt.properties", "instrument_Protocol_1");
//        StringBuffer sb = new StringBuffer();
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("experiment_no", experiment_no);
//        List<RdsJudicialExperimentModel> model = rdsJudicialExperimentMapper.queryExperiment(map);
//        String places = model.get(0).getPlaces();
//        String[] place = places.split(",");
//        sb.append("Container Name\tDescription\tContainerType\tAppType\tOwner\tOperator\t\n");
//        sb.append(model.get(0).getExperiment_no() + "\t\t96-Well\tRegular\t1\t1\t\n");
//        sb.append("AppServer\tAppInstance\t\n");
//        sb.append("GeneMapper\tGeneMapper_Generic_Instance\t\n");
//        sb.append("No\tWell\tSample Name\n");
//        int i = 1;
//        if (!place[0].endsWith("=")) {
//            sb.append(i + "\tA01\t" + getPlace(place[0].split("=")) + "\n");
//            i++;
//        }
//        if (!place[12].endsWith("=")) {
//            sb.append(i + "\tB01\t" + getPlace(place[12].split("=")) + "\n");
//            i++;
//        }
//        if (!place[24].endsWith("=")) {
//            sb.append(i + "\tC01\t" + getPlace(place[24].split("=")) + "\n");
//            i++;
//        }
//        if (!place[36].endsWith("=")) {
//            sb.append(i + "\tD01\t" + getPlace(place[36].split("=")) + "\n");
//            i++;
//        }
//        if (!place[48].endsWith("=")) {
//            sb.append(i + "\tE01\t" + getPlace(place[48].split("=")) + "\n");
//            i++;
//        }
//        if (!place[60].endsWith("=")) {
//            sb.append(i + "\tF01\t" + getPlace(place[60].split("=")) + "\n");
//            i++;
//        }
//        if (!place[72].endsWith("=")) {
//            sb.append(i + "\tG01\t" + getPlace(place[72].split("=")) + "\n");
//            i++;
//        }
//        if (!place[84].endsWith("=")) {
//            sb.append(i + "\tH01\t" + getPlace(place[84].split("=")) + "\n");
//            i++;
//        }
//        if (!place[1].endsWith("=")) {
//            sb.append(i + "\tA02\t" + getPlace(place[1].split("=")) + "\n");
//            i++;
//        }
//        if (!place[13].endsWith("=")) {
//            sb.append(i + "\tB02\t" + getPlace(place[13].split("=")) + "\n");
//            i++;
//        }
//        if (!place[25].endsWith("=")) {
//            sb.append(i + "\tC02\t" + getPlace(place[25].split("=")) + "\n");
//            i++;
//        }
//        if (!place[37].endsWith("=")) {
//            sb.append(i + "\tD02\t" + getPlace(place[37].split("=")) + "\n");
//            i++;
//        }
//        if (!place[49].endsWith("=")) {
//            sb.append(i + "\tE02\t" + getPlace(place[49].split("=")) + "\n");
//            i++;
//        }
//        if (!place[61].endsWith("=")) {
//            sb.append(i + "\tF02\t" + getPlace(place[61].split("=")) + "\n");
//            i++;
//        }
//        if (!place[73].endsWith("=")) {
//            sb.append(i + "\tG02\t" + getPlace(place[73].split("=")) + "\n");
//            i++;
//        }
//        if (!place[85].endsWith("=")) {
//            sb.append(i + "\tH02\t" + getPlace(place[85].split("=")) + "\n");
//            i++;
//        }
//        if (!place[2].endsWith("=")) {
//            sb.append(i + "\tA03\t" + getPlace(place[2].split("=")) + "\n");
//            i++;
//        }
//        if (!place[14].endsWith("=")) {
//            sb.append(i + "\tB03\t" + getPlace(place[14].split("=")) + "\n");
//            i++;
//        }
//        if (!place[26].endsWith("=")) {
//            sb.append(i + "\tC03\t" + getPlace(place[26].split("=")) + "\n");
//            i++;
//        }
//        if (!place[38].endsWith("=")) {
//            sb.append(i + "\tD03\t" + getPlace(place[38].split("=")) + "\n");
//            i++;
//        }
//        if (!place[50].endsWith("=")) {
//            sb.append(i + "\tE03\t" + getPlace(place[50].split("=")) + "\n");
//            i++;
//        }
//        if (!place[62].endsWith("=")) {
//            sb.append(i + "\tF03\t" + getPlace(place[62].split("=")) + "\n");
//            i++;
//        }
//        if (!place[74].endsWith("=")) {
//            sb.append(i + "\tG03\t" + getPlace(place[74].split("=")) + "\n");
//            i++;
//        }
//        if (!place[86].endsWith("=")) {
//            sb.append(i + "\tH03\t" + getPlace(place[86].split("=")) + "\n");
//            i++;
//        }
//        if (!place[3].endsWith("=")) {
//            sb.append(i + "\tA04\t" + getPlace(place[3].split("=")) + "\n");
//            i++;
//        }
//        if (!place[15].endsWith("=")) {
//            sb.append(i + "\tB04\t" + getPlace(place[15].split("=")) + "\n");
//            i++;
//        }
//        if (!place[27].endsWith("=")) {
//            sb.append(i + "\tC04\t" + getPlace(place[27].split("=")) + "\n");
//            i++;
//        }
//        if (!place[39].endsWith("=")) {
//            sb.append(i + "\tD04\t" + getPlace(place[39].split("=")) + "\n");
//            i++;
//        }
//        if (!place[51].endsWith("=")) {
//            sb.append(i + "\tE04\t" + getPlace(place[51].split("=")) + "\n");
//            i++;
//        }
//        if (!place[63].endsWith("=")) {
//            sb.append(i + "\tF04\t" + getPlace(place[63].split("=")) + "\n");
//            i++;
//        }
//        if (!place[75].endsWith("=")) {
//            sb.append(i + "\tG04\t" + getPlace(place[75].split("=")) + "\n");
//            i++;
//        }
//        if (!place[87].endsWith("=")) {
//            sb.append(i + "\tH04\t" + getPlace(place[87].split("=")) + "\n");
//            i++;
//        }
//        if (!place[4].endsWith("=")) {
//            sb.append(i + "\tA05\t" + getPlace(place[4].split("=")) + "\n");
//            i++;
//        }
//        if (!place[16].endsWith("=")) {
//            sb.append(i + "\tB05\t" + getPlace(place[16].split("=")) + "\n");
//            i++;
//        }
//        if (!place[28].endsWith("=")) {
//            sb.append(i + "\tC05\t" + getPlace(place[28].split("=")) + "\n");
//            i++;
//        }
//        if (!place[40].endsWith("=")) {
//            sb.append(i + "\tD05\t" + getPlace(place[40].split("=")) + "\n");
//            i++;
//        }
//        if (!place[52].endsWith("=")) {
//            sb.append(i + "\tE05\t" + getPlace(place[52].split("=")) + "\n");
//            i++;
//        }
//        if (!place[64].endsWith("=")) {
//            sb.append(i + "\tF05\t" + getPlace(place[64].split("=")) + "\n");
//            i++;
//        }
//        if (!place[76].endsWith("=")) {
//            sb.append(i + "\tG05\t" + getPlace(place[76].split("=")) + "\n");
//            i++;
//        }
//        if (!place[88].endsWith("=")) {
//            sb.append(i + "\tH05\t" + getPlace(place[88].split("=")) + "\n");
//            i++;
//        }
//        if (!place[5].endsWith("=")) {
//            sb.append(i + "\tA06\t" + getPlace(place[5].split("=")) + "\n");
//            i++;
//        }
//        if (!place[17].endsWith("=")) {
//            sb.append(i + "\tB06\t" + getPlace(place[17].split("=")) + "\n");
//            i++;
//        }
//        if (!place[29].endsWith("=")) {
//            sb.append(i + "\tC06\t" + getPlace(place[29].split("=")) + "\n");
//            i++;
//        }
//        if (!place[41].endsWith("=")) {
//            sb.append(i + "\tD06\t" + getPlace(place[41].split("=")) + "\n");
//            i++;
//        }
//        if (!place[53].endsWith("=")) {
//            sb.append(i + "\tE06\t" + getPlace(place[53].split("=")) + "\n");
//            i++;
//        }
//        if (!place[65].endsWith("=")) {
//            sb.append(i + "\tF06\t" + getPlace(place[65].split("=")) + "\n");
//            i++;
//        }
//        if (!place[77].endsWith("=")) {
//            sb.append(i + "\tG06\t" + getPlace(place[77].split("=")) + "\n");
//            i++;
//        }
//        if (!place[89].endsWith("=")) {
//            sb.append(i + "\tH06\t" + getPlace(place[89].split("=")) + "\n");
//            i++;
//        }
//        if (!place[6].endsWith("=")) {
//            sb.append(i + "\tA07\t" + getPlace(place[6].split("=")) + "\n");
//            i++;
//        }
//        if (!place[18].endsWith("=")) {
//            sb.append(i + "\tB07\t" + getPlace(place[18].split("=")) + "\n");
//            i++;
//        }
//        if (!place[30].endsWith("=")) {
//            sb.append(i + "\tC07\t" + getPlace(place[30].split("=")) + "\n");
//            i++;
//        }
//        if (!place[42].endsWith("=")) {
//            sb.append(i + "\tD07\t" + getPlace(place[42].split("=")) + "\n");
//            i++;
//        }
//        if (!place[54].endsWith("=")) {
//            sb.append(i + "\tE07\t" + getPlace(place[54].split("=")) + "\n");
//            i++;
//        }
//        if (!place[66].endsWith("=")) {
//            sb.append(i + "\tF07\t" + getPlace(place[66].split("=")) + "\n");
//            i++;
//        }
//        if (!place[78].endsWith("=")) {
//            sb.append(i + "\tG07\t" + getPlace(place[78].split("=")) + "\n");
//            i++;
//        }
//        if (!place[90].endsWith("=")) {
//            sb.append(i + "\tH07\t" + getPlace(place[90].split("=")) + "\n");
//            i++;
//        }
//        if (!place[7].endsWith("=")) {
//            sb.append(i + "\tA08\t" + getPlace(place[7].split("=")) + "\n");
//            i++;
//        }
//        if (!place[19].endsWith("=")) {
//            sb.append(i + "\tB08\t" + getPlace(place[19].split("=")) + "\n");
//            i++;
//        }
//        if (!place[31].endsWith("=")) {
//            sb.append(i + "\tC08\t" + getPlace(place[31].split("=")) + "\n");
//            i++;
//        }
//        if (!place[43].endsWith("=")) {
//            sb.append(i + "\tD08\t" + getPlace(place[43].split("=")) + "\n");
//            i++;
//        }
//        if (!place[55].endsWith("=")) {
//            sb.append(i + "\tE08\t" + getPlace(place[55].split("=")) + "\n");
//            i++;
//        }
//        if (!place[67].endsWith("=")) {
//            sb.append(i + "\tF08\t" + getPlace(place[67].split("=")) + "\n");
//            i++;
//        }
//        if (!place[79].endsWith("=")) {
//            sb.append(i + "\tG08\t" + getPlace(place[79].split("=")) + "\n");
//            i++;
//        }
//        if (!place[91].endsWith("=")) {
//            sb.append(i + "\tH08\t" + getPlace(place[91].split("=")) + "\n");
//            i++;
//        }
//        if (!place[8].endsWith("=")) {
//            sb.append(i + "\tA09\t" + getPlace(place[8].split("=")) + "\n");
//            i++;
//        }
//        if (!place[20].endsWith("=")) {
//            sb.append(i + "\tB09\t" + getPlace(place[20].split("=")) + "\n");
//            i++;
//        }
//        if (!place[32].endsWith("=")) {
//            sb.append(i + "\tC09\t" + getPlace(place[32].split("=")) + "\n");
//            i++;
//        }
//        if (!place[44].endsWith("=")) {
//            sb.append(i + "\tD09\t" + getPlace(place[44].split("=")) + "\n");
//            i++;
//        }
//        if (!place[56].endsWith("=")) {
//            sb.append(i + "\tE09\t" + getPlace(place[56].split("=")) + "\n");
//            i++;
//        }
//        if (!place[68].endsWith("=")) {
//            sb.append(i + "\tF09\t" + getPlace(place[68].split("=")) + "\n");
//            i++;
//        }
//        if (!place[80].endsWith("=")) {
//            sb.append(i + "\tG09\t" + getPlace(place[80].split("=")) + "\n");
//            i++;
//        }
//        if (!place[92].endsWith("=")) {
//            sb.append(i + "\tH09\t" + getPlace(place[92].split("=")) + "\n");
//            i++;
//        }
//        if (!place[9].endsWith("=")) {
//            sb.append(i + "\tA10\t" + getPlace(place[9].split("=")) + "\n");
//            i++;
//        }
//        if (!place[21].endsWith("=")) {
//            sb.append(i + "\tB10\t" + getPlace(place[21].split("=")) + "\n");
//            i++;
//        }
//        if (!place[33].endsWith("=")) {
//            sb.append(i + "\tC10\t" + getPlace(place[33].split("=")) + "\n");
//            i++;
//        }
//        if (!place[45].endsWith("=")) {
//            sb.append(i + "\tD10\t" + getPlace(place[45].split("=")) + "\n");
//            i++;
//        }
//        if (!place[57].endsWith("=")) {
//            sb.append(i + "\tE10\t" + getPlace(place[57].split("=")) + "\n");
//            i++;
//        }
//        if (!place[69].endsWith("=")) {
//            sb.append(i + "\tF10\t" + getPlace(place[69].split("=")) + "\n");
//            i++;
//        }
//        if (!place[81].endsWith("=")) {
//            sb.append(i + "\tG10\t" + getPlace(place[81].split("=")) + "\n");
//            i++;
//        }
//        if (!place[93].endsWith("=")) {
//            sb.append(i + "\tH10\t" + getPlace(place[93].split("=")) + "\n");
//            i++;
//        }
//        if (!place[10].endsWith("=")) {
//            sb.append(i + "\tA11\t" + getPlace(place[10].split("=")) + "\n");
//            i++;
//        }
//        if (!place[22].endsWith("=")) {
//            sb.append(i + "\tB11\t" + getPlace(place[22].split("=")) + "\n");
//            i++;
//        }
//        if (!place[34].endsWith("=")) {
//            sb.append(i + "\tC11\t" + getPlace(place[34].split("=")) + "\n");
//            i++;
//        }
//        if (!place[46].endsWith("=")) {
//            sb.append(i + "\tD11\t" + getPlace(place[46].split("=")) + "\n");
//            i++;
//        }
//        if (!place[58].endsWith("=")) {
//            sb.append(i + "\tE11\t" + getPlace(place[58].split("=")) + "\n");
//            i++;
//        }
//        if (!place[70].endsWith("=")) {
//            sb.append(i + "\tF11\t" + getPlace(place[70].split("=")) + "\n");
//            i++;
//        }
//        if (!place[82].endsWith("=")) {
//            sb.append(i + "\tG11\t" + getPlace(place[82].split("=")) + "\n");
//            i++;
//        }
//        if (!place[94].endsWith("=")) {
//            sb.append(i + "\tH11\t" + getPlace(place[94].split("=")) + "\n");
//            i++;
//        }
//        if (!place[11].endsWith("=")) {
//            sb.append(i + "\tA12\t" + getPlace(place[11].split("=")) + "\n");
//            i++;
//        }
//        if (!place[23].endsWith("=")) {
//            sb.append(i + "\tB12\t" + getPlace(place[23].split("=")) + "\n");
//            i++;
//        }
//        if (!place[35].endsWith("=")) {
//            sb.append(i + "\tC12\t" + getPlace(place[35].split("=")) + "\n");
//            i++;
//        }
//        if (!place[47].endsWith("=")) {
//            sb.append(i + "\tD12\t" + getPlace(place[47].split("=")) + "\n");
//            i++;
//        }
//        if (!place[59].endsWith("=")) {
//            sb.append(i + "\tE12\t" + getPlace(place[59].split("=")) + "\n");
//            i++;
//        }
//        if (!place[71].endsWith("=")) {
//            sb.append(i + "\tF12\t" + getPlace(place[71].split("=")) + "\n");
//            i++;
//        }
//        if (!place[83].endsWith("=")) {
//            sb.append(i + "\tG12\t" + getPlace(place[83].split("=")) + "\n");
//            i++;
//        }
//        if (!place[95].endsWith("=")) {
//            sb.append(i + "\tH12\t" + getPlace(place[95].split("=")) + "\n");
//            i++;
//        }
//        return sb.toString();
//    }

    @Override
    public Map<Long, String> getPlaces(String experiment_no,String laboratory_no) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("experiment_no", experiment_no);
        map.put("laboratory_no",laboratory_no);
        List<Object> model = rdsJudicialExperimentMapper.queryAll(map);
        Map<Long, String> result = new HashMap<Long, String>();
        result.put(97L, experiment_no);
        String places = ((RdsJudicialExperimentModel)model.get(0)).getPlaces();
        String[] place = places.split(",");
        Long i = 1L;
        if (!place[0].endsWith("="))
            result.put(i, getPlace(place[0].split("=")));
        i++;

        if (!place[12].endsWith("="))
            result.put(i, getPlace(place[12].split("=")));
        i++;

        if (!place[24].endsWith("="))
            result.put(i, getPlace(place[24].split("=")));
        i++;

        if (!place[36].endsWith("="))
            result.put(i, getPlace(place[36].split("=")));
        i++;

        if (!place[48].endsWith("="))
            result.put(i, getPlace(place[48].split("=")));
        i++;

        if (!place[60].endsWith("="))
            result.put(i, getPlace(place[60].split("=")));
        i++;

        if (!place[72].endsWith("="))
            result.put(i, getPlace(place[72].split("=")));
        i++;

        if (!place[84].endsWith("="))
            result.put(i, getPlace(place[84].split("=")));
        i++;

        if (!place[1].endsWith("="))
            result.put(i, getPlace(place[1].split("=")));
        i++;

        if (!place[13].endsWith("="))
            result.put(i, getPlace(place[13].split("=")));
        i++;

        if (!place[25].endsWith("="))
            result.put(i, getPlace(place[25].split("=")));
        i++;

        if (!place[37].endsWith("="))
            result.put(i, getPlace(place[37].split("=")));
        i++;

        if (!place[49].endsWith("="))
            result.put(i, getPlace(place[49].split("=")));
        i++;

        if (!place[61].endsWith("="))
            result.put(i, getPlace(place[61].split("=")));
        i++;

        if (!place[73].endsWith("="))
            result.put(i, getPlace(place[73].split("=")));
        i++;

        if (!place[85].endsWith("="))
            result.put(i, getPlace(place[85].split("=")));
        i++;

        if (!place[2].endsWith("="))
            result.put(i, getPlace(place[2].split("=")));
        i++;

        if (!place[14].endsWith("="))
            result.put(i, getPlace(place[14].split("=")));
        i++;

        if (!place[26].endsWith("="))
            result.put(i, getPlace(place[26].split("=")));
        i++;

        if (!place[38].endsWith("="))
            result.put(i, getPlace(place[38].split("=")));
        i++;

        if (!place[50].endsWith("="))
            result.put(i, getPlace(place[50].split("=")));
        i++;

        if (!place[62].endsWith("="))
            result.put(i, getPlace(place[62].split("=")));
        i++;

        if (!place[74].endsWith("="))
            result.put(i, getPlace(place[74].split("=")));
        i++;

        if (!place[86].endsWith("="))
            result.put(i, getPlace(place[86].split("=")));
        i++;

        if (!place[3].endsWith("="))
            result.put(i, getPlace(place[3].split("=")));
        i++;

        if (!place[15].endsWith("="))
            result.put(i, getPlace(place[15].split("=")));
        i++;

        if (!place[27].endsWith("="))
            result.put(i, getPlace(place[27].split("=")));
        i++;

        if (!place[39].endsWith("="))
            result.put(i, getPlace(place[39].split("=")));
        i++;

        if (!place[51].endsWith("="))
            result.put(i, getPlace(place[51].split("=")));
        i++;

        if (!place[63].endsWith("="))
            result.put(i, getPlace(place[63].split("=")));
        i++;

        if (!place[75].endsWith("="))
            result.put(i, getPlace(place[75].split("=")));
        i++;

        if (!place[87].endsWith("="))
            result.put(i, getPlace(place[87].split("=")));
        i++;

        if (!place[4].endsWith("="))
            result.put(i, getPlace(place[4].split("=")));
        i++;

        if (!place[16].endsWith("="))
            result.put(i, getPlace(place[16].split("=")));
        i++;

        if (!place[28].endsWith("="))
            result.put(i, getPlace(place[28].split("=")));
        i++;

        if (!place[40].endsWith("="))
            result.put(i, getPlace(place[40].split("=")));
        i++;

        if (!place[52].endsWith("="))
            result.put(i, getPlace(place[52].split("=")));
        i++;

        if (!place[64].endsWith("="))
            result.put(i, getPlace(place[64].split("=")));
        i++;

        if (!place[76].endsWith("="))
            result.put(i, getPlace(place[76].split("=")));
        i++;

        if (!place[88].endsWith("="))
            result.put(i, getPlace(place[88].split("=")));
        i++;

        if (!place[5].endsWith("="))
            result.put(i, getPlace(place[5].split("=")));
        i++;

        if (!place[17].endsWith("="))
            result.put(i, getPlace(place[17].split("=")));
        i++;

        if (!place[29].endsWith("="))
            result.put(i, getPlace(place[29].split("=")));
        i++;

        if (!place[41].endsWith("="))
            result.put(i, getPlace(place[41].split("=")));
        i++;

        if (!place[53].endsWith("="))
            result.put(i, getPlace(place[53].split("=")));
        i++;

        if (!place[65].endsWith("="))
            result.put(i, getPlace(place[65].split("=")));
        i++;

        if (!place[77].endsWith("="))
            result.put(i, getPlace(place[77].split("=")));
        i++;

        if (!place[89].endsWith("="))
            result.put(i, getPlace(place[89].split("=")));
        i++;

        if (!place[6].endsWith("="))
            result.put(i, getPlace(place[6].split("=")));
        i++;

        if (!place[18].endsWith("="))
            result.put(i, getPlace(place[18].split("=")));
        i++;

        if (!place[30].endsWith("="))
            result.put(i, getPlace(place[30].split("=")));
        i++;

        if (!place[42].endsWith("="))
            result.put(i, getPlace(place[42].split("=")));
        i++;

        if (!place[54].endsWith("="))
            result.put(i, getPlace(place[54].split("=")));
        i++;

        if (!place[66].endsWith("="))
            result.put(i, getPlace(place[66].split("=")));
        i++;

        if (!place[78].endsWith("="))
            result.put(i, getPlace(place[78].split("=")));
        i++;

        if (!place[90].endsWith("="))
            result.put(i, getPlace(place[90].split("=")));
        i++;

        if (!place[7].endsWith("="))
            result.put(i, getPlace(place[7].split("=")));
        i++;

        if (!place[19].endsWith("="))
            result.put(i, getPlace(place[19].split("=")));
        i++;

        if (!place[31].endsWith("="))
            result.put(i, getPlace(place[31].split("=")));
        i++;

        if (!place[43].endsWith("="))
            result.put(i, getPlace(place[43].split("=")));
        i++;

        if (!place[55].endsWith("="))
            result.put(i, getPlace(place[55].split("=")));
        i++;

        if (!place[67].endsWith("="))
            result.put(i, getPlace(place[67].split("=")));
        i++;

        if (!place[79].endsWith("="))
            result.put(i, getPlace(place[79].split("=")));
        i++;

        if (!place[91].endsWith("="))
            result.put(i, getPlace(place[91].split("=")));
        i++;

        if (!place[8].endsWith("="))
            result.put(i, getPlace(place[8].split("=")));
        i++;

        if (!place[20].endsWith("="))
            result.put(i, getPlace(place[20].split("=")));
        i++;

        if (!place[32].endsWith("="))
            result.put(i, getPlace(place[32].split("=")));
        i++;

        if (!place[44].endsWith("="))
            result.put(i, getPlace(place[44].split("=")));
        i++;

        if (!place[56].endsWith("="))
            result.put(i, getPlace(place[56].split("=")));
        i++;

        if (!place[68].endsWith("="))
            result.put(i, getPlace(place[68].split("=")));
        i++;

        if (!place[80].endsWith("="))
            result.put(i, getPlace(place[80].split("=")));
        i++;

        if (!place[92].endsWith("="))
            result.put(i, getPlace(place[92].split("=")));
        i++;

        if (!place[9].endsWith("="))
            result.put(i, getPlace(place[9].split("=")));
        i++;

        if (!place[21].endsWith("="))
            result.put(i, getPlace(place[21].split("=")));
        i++;

        if (!place[33].endsWith("="))
            result.put(i, getPlace(place[33].split("=")));
        i++;

        if (!place[45].endsWith("="))
            result.put(i, getPlace(place[45].split("=")));
        i++;

        if (!place[57].endsWith("="))
            result.put(i, getPlace(place[57].split("=")));
        i++;

        if (!place[69].endsWith("="))
            result.put(i, getPlace(place[69].split("=")));
        i++;

        if (!place[81].endsWith("="))
            result.put(i, getPlace(place[81].split("=")));
        i++;

        if (!place[93].endsWith("="))
            result.put(i, getPlace(place[93].split("=")));
        i++;

        if (!place[10].endsWith("="))
            result.put(i, getPlace(place[10].split("=")));
        i++;

        if (!place[22].endsWith("="))
            result.put(i, getPlace(place[22].split("=")));
        i++;

        if (!place[34].endsWith("="))
            result.put(i, getPlace(place[34].split("=")));
        i++;

        if (!place[46].endsWith("="))
            result.put(i, getPlace(place[46].split("=")));
        i++;

        if (!place[58].endsWith("="))
            result.put(i, getPlace(place[58].split("=")));
        i++;

        if (!place[70].endsWith("="))
            result.put(i, getPlace(place[70].split("=")));
        i++;

        if (!place[82].endsWith("="))
            result.put(i, getPlace(place[82].split("=")));
        i++;

        if (!place[94].endsWith("="))
            result.put(i, getPlace(place[94].split("=")));
        i++;

        if (!place[11].endsWith("="))
            result.put(i, getPlace(place[11].split("=")));
        i++;

        if (!place[23].endsWith("="))
            result.put(i, getPlace(place[23].split("=")));
        i++;

        if (!place[35].endsWith("="))
            result.put(i, getPlace(place[35].split("=")));
        i++;

        if (!place[47].endsWith("="))
            result.put(i, getPlace(place[47].split("=")));
        i++;

        if (!place[59].endsWith("="))
            result.put(i, getPlace(place[59].split("=")));
        i++;

        if (!place[71].endsWith("="))
            result.put(i, getPlace(place[71].split("=")));
        i++;

        if (!place[83].endsWith("="))
            result.put(i, getPlace(place[83].split("=")));
        i++;

        if (!place[95].endsWith("="))
            result.put(i, getPlace(place[95].split("=")));
        i++;

        return result;
    }

    private String getPlace(String[] str1) {
        try {
            return str1[1];
        } catch (Exception e) {
            return "";
        }
    }

    private String getDataPath() {
        return PropertiesUtils.readValue(ConfigPath.getWebInfPath()
                + File.separatorChar + "spring" + File.separatorChar +
                "properties" + File.separatorChar + "config.properties", "");
    }
}