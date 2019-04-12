package com.rds.statistics.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2016/5/3
 */
public interface RdsAllCaseInfoMapper {
    List<Object> queryAll(Map<String,Object> params);

    int queryAllCount(Map<String,Object> params);
    
    List<Object> queryAllOld(Map<String,Object> params);

    int queryAllCountOld(Map<String,Object> params);
    
    int callCollect();
    
    int callExportCaseInof();
    
    int callCaseDetailInfo(String yestedayDate);
    
    int callStat_finance();
}
