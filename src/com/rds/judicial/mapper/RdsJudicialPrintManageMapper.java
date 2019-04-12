package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.model.RdsJudicialPrintCompanyAreaModel;

public interface RdsJudicialPrintManageMapper {

    public List<RdsJudicialPrintCompanyAreaModel> getPrintCaseInfo(
            Map<String, Object> params);

    public int countPrintCaseInfo(Map<String, Object> params);
    
    
    public List<RdsJudicialPrintCompanyAreaModel> getPrintInfoFoModel(
            Map<String, Object> params);

    public int countPrintCaseInfoForModel(Map<String, Object> params);
    
    

    public List<RdsJudicialKeyValueModel> getCompany();

    public List<RdsJudicialDicAreaModel> getArea(Map<String, Object> params);

    public List<RdsJudicialDicAreaModel> getAllCity(Map<String, Object> params);

    public List<RdsJudicialDicAreaModel> getAllZone(Map<String, Object> params);

    public void delPrintCompany(Map<String, Object> params);

    public void savePrintCompany(Map<String, Object> params);

    public void delPrintArea(Map<String, Object> params);

    public boolean savePrintArea(List<Map<String, String>> list);
    
    public void delDicPrintModel(Map<String, Object> params);

    public boolean saveDicPrintModel(List<Map<String, String>> list);
    


}
