package com.rds.judicial.service.impl;

import com.rds.code.utils.FileUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.mapper.RdsJudicialResultMapper;
import com.rds.judicial.mapper.RdsJudicialSubCaseMapper;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSubCaseInfoModel;
import com.rds.judicial.model.RdsJudicialSubCaseResultModel;
import com.rds.judicial.service.RdsJudicialSubCaseService;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/23
 */
@Service("RdsJudicialSubCaseService")
public class RdsJudicialSubCaseServiceImpl implements RdsJudicialSubCaseService{

    @Autowired
    RdsJudicialSubCaseMapper rdsJudicialSubCaseMapper;

    @Autowired
    RdsJudicialResultMapper rdsJudicialResultMapper;

    @Override
    public int queryAllCount(Map<String, Object> params) {
        return rdsJudicialSubCaseMapper.queryAllCount(params);
    }

    @Override
    public List<Object> queryAll(Map<String, Object> params) {
        return rdsJudicialSubCaseMapper.queryAll(params);
    }
    @Override
    public void insert(RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel){
        rdsJudicialSubCaseMapper.insert(rdsJudicialSubCaseInfoModel);
    }

    @Override
    public int update(Map<String,Object> params){
        if(rdsJudicialResultMapper.updateResult(params)==1)
        return rdsJudicialSubCaseMapper.update(params);
        else return 0;
    }

    @Override
    public int queryCountForGen(String case_code) {
        return rdsJudicialSubCaseMapper.queryCountForGen(case_code);
    }

    @Override
    public int verifySampleCode(Map<String,Object> params) {
        return rdsJudicialSubCaseMapper.verifySampleCode(params);
    }

    @Override
    public void insertCheckNegReport(Map<String, Object> params) {
        rdsJudicialSubCaseMapper.insertCheckNegReport(params);
    }

    @Override
    public int updateCheckNegReport(String case_code) {
        return rdsJudicialSubCaseMapper.updateCheckNegReport(case_code);
    }

    @Override
    public RdsJudicialCaseInfoModel queryCaseInfoByCaseCode(String case_code) {
        return rdsJudicialSubCaseMapper.queryCaseInfoByCaseCode(case_code);
    }

    @Override
    public String queryReagentNameByCaseId(String case_id) {
        return rdsJudicialSubCaseMapper.queryReagentNameByCaseId(case_id);
    }

    @Override
    public int deleteData(String case_code) {
        int result = rdsJudicialSubCaseMapper.deleteData(case_code);
       int  count =rdsJudicialSubCaseMapper.updateCaseinfofinalbycode(case_code) ;
        String judicialPath = PropertiesUtils.readValue(ConfigPath.getWebInfPath()
                + File.separatorChar + "spring" + File.separatorChar +
                "properties" + File.separatorChar + "config.properties", "judicial_path");
        FileUtils.delFolder(judicialPath + File.separatorChar+ case_code);
        return result;
    }

    @Override
    public int queryAllCountForZhengTaiExt(Map<String, Object> params) {
        return rdsJudicialSubCaseMapper.queryAllCountForZhengTaiExt(params);
    }

    @Override
    public List<RdsJudicialSubCaseResultModel> queryAllForZhengTaiExt(Map<String, Object> params) {
        return rdsJudicialSubCaseMapper.queryAllForZhengTaiExt(params);
    }
    @Transactional
	@Override
	public int allDelete(Map<String, String>[] data) {
		  List<String> lists = new ArrayList<>();
	        for (Map<String, String> da: data){
	        	String case_code = da.get("case_code");
	        	rdsJudicialSubCaseMapper.deleteOne(case_code);
	        	rdsJudicialSubCaseMapper.deleteTwo(case_code);
	        	rdsJudicialSubCaseMapper.updatesubcase(case_code);
	            lists.add(case_code);
	        }
	        Map<String,Object> mapCheck = new HashMap<>();
	        mapCheck.put("case_code", lists);
	        List<Map<String,String>> listsample = rdsJudicialSubCaseMapper.querySampleCodes(mapCheck);
		       int b=rdsJudicialSubCaseMapper.allDelete(mapCheck);
		       int c=rdsJudicialSubCaseMapper.allDelete1(mapCheck);
		       int d=rdsJudicialSubCaseMapper.allDelete2(mapCheck);
		       int e=rdsJudicialSubCaseMapper.allDelete3(mapCheck);
		       int f=rdsJudicialSubCaseMapper.allDelete4(mapCheck);
	        List<String> listss = new ArrayList<>();
	        for (Map<String, String> da: listsample){
	        	String case_code =  da.get("sample_code");
	            listss.add(case_code);
	        }
	        Map<String,Object> mapCheck1 = new HashMap<>();
	        mapCheck1.put("sample_code", listss);
	        if(0!=listss.size()){
	       int a= rdsJudicialSubCaseMapper.deleteSamples(mapCheck1);
	       int g= rdsJudicialSubCaseMapper.deleteSampless(mapCheck1);
	        }
		return 1;
	}

}
