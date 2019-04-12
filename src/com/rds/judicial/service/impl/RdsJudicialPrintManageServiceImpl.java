package com.rds.judicial.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialAreaMapper;
import com.rds.judicial.mapper.RdsJudicialPrintManageMapper;
import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.model.RdsJudicialPrintCompanyAreaModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialPrintManageService;

@Service
public class RdsJudicialPrintManageServiceImpl implements RdsJudicialPrintManageService{

    @Autowired
    private RdsJudicialPrintManageMapper RdsJudicialPrintManageMapper;
    @Autowired
    private RdsJudicialAreaMapper rdsJudicialAreaMapper;

    @Override
    public RdsJudicialResponse getPrintCaseInfo(Map<String, Object> params) {
        RdsJudicialResponse response=new RdsJudicialResponse();
        List<RdsJudicialPrintCompanyAreaModel> printInfos=RdsJudicialPrintManageMapper.getPrintCaseInfo(params);
        int count =RdsJudicialPrintManageMapper.countPrintCaseInfo(params);
        response.setCount(count);
        response.setItems(printInfos);
        return response;
    }
    
    
    @Override
    public RdsJudicialResponse getPrintInfoFoModel(Map<String, Object> params) {
        RdsJudicialResponse response=new RdsJudicialResponse();
        List<RdsJudicialPrintCompanyAreaModel> printInfos=RdsJudicialPrintManageMapper.getPrintInfoFoModel(params);
        int count =RdsJudicialPrintManageMapper.countPrintCaseInfoForModel(params);
        response.setCount(count);
        response.setItems(printInfos);
        return response;
    }
    

    @Override
    public List<RdsJudicialKeyValueModel> getCompany() {
        return RdsJudicialPrintManageMapper.getCompany();
    }

    @Override
    public List<RdsJudicialDicAreaModel> getArea(Map<String, Object> params) {
        //省
        List<RdsJudicialDicAreaModel> dicProvinceModels=RdsJudicialPrintManageMapper.getArea(params);

//        //市
//        List<RdsJudicialDicAreaModel> dicCityModels=RdsJudicialPrintManageMapper.getAllCity(params);
//
//        //区
//        List<RdsJudicialDicAreaModel> dicZoneModels=RdsJudicialPrintManageMapper.getAllZone(params);
//
//        out:
//        for(RdsJudicialDicAreaModel zone:dicZoneModels){
//            for(RdsJudicialDicAreaModel city:dicCityModels){
//                city.setLeaf(false);
//                //city.setText(city.getText()+"("+city.getId()+")");
//                if(zone.getParentId().equals(city.getId())) {
//                    city.getChildren().add(zone);
//                    for (RdsJudicialDicAreaModel province : dicProvinceModels) {
//                        province.setLeaf(false);
//                        //zone.setText(zone.getText() + "(" + zone.getId() + ")");
//                        if (city.getParentId().equals(province.getId())) {
//                            province.getChildren().add(city);
//                            continue out;
//                        }
//                    }
//                }
//            }
//        }
        return dicProvinceModels;
    }

    
    @Override
    public boolean saveArea(Map<String, Object> params) {
        String code=params.get("area_code").toString();
        if(StringUtils.isNotEmpty(code)){
            String[] area_codes=code.split(",");
            RdsJudicialPrintManageMapper.delPrintCompany(params);
            params.put("print_company_id", UUIDUtil.getUUID());
            RdsJudicialPrintManageMapper.savePrintCompany(params);
            RdsJudicialPrintManageMapper.delPrintArea(params);
            List<Map<String, String>> list=new ArrayList<Map<String,String>>();
            for(String area_code:area_codes){
                Map<String, String> map=new HashMap<String, String>();
                map.put("area_code", area_code);
                map.put("id", UUIDUtil.getUUID());
                map.put("print_company_id", params.get("print_company_id").toString());
                list.add(map);
            }
            return RdsJudicialPrintManageMapper.savePrintArea(list);
        }
        return false;
    }

    @Override
    public boolean saveDicPrintModel(Map<String, Object> params) {
    	String code=params.get("modelcode").toString();
        String text=params.get("modelname").toString();
        String reagent_name=params.get("reagentname").toString();
        String reagent_name_ext=params.get("reagentextname").toString();
        
        if(StringUtils.isNotEmpty(reagent_name)){
            String[] reagent_names=reagent_name.split(",");
            String[] reagent_name_exts=reagent_name_ext.split(",");
             
            params.put("print_company_id", UUIDUtil.getUUID());
 
            RdsJudicialPrintManageMapper.delDicPrintModel(params);
            List<Map<String, String>> list1=new ArrayList<Map<String,String>>();
            List<Map<String, String>> list2=new ArrayList<Map<String,String>>();
            for(String reagent_name_info:reagent_names){
                Map<String, String> map=new HashMap<String, String>();
                map.put("reagent_name", reagent_name_info);
                map.put("code", code);
                map.put("text", text);
                map.put("reagent_name_exts", reagent_name_exts[0]);
                list1.add(map);
                Map<String, String> map1=new HashMap<String, String>();
                map.put("reagent_name", reagent_name_info);
                map.put("code", code);
                map.put("text", text);
                map.put("reagent_name_exts", reagent_name_exts[1]);
                
                list2.add(map1);
            }
            return RdsJudicialPrintManageMapper.saveDicPrintModel(list1);
        }
        return false;
    }

}
