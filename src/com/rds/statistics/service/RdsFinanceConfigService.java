package com.rds.statistics.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.statistics.model.RdsFinanceAmoebaStatisticsModel;
import com.rds.statistics.model.RdsFinanceAptitudModel;
import com.rds.statistics.model.RdsFinanceCaseDetailStatisticsModel;
import com.rds.statistics.model.RdsFinanceConfigModel;
import com.rds.statistics.model.RdsStatisticsDepartmentModel;

public interface RdsFinanceConfigService {
    List<RdsFinanceConfigModel> queryAll(Map<String,Object> params);

    int queryAllCount(Map<String,Object> params);
    
    void finance_config(Map<String,Object> params);
    
    List<List<String>> queryAmoeba(Map<String,Object> params);

    void exportAmoeba(Map<String,Object> params,HttpServletResponse response );

    void exportAmoebaSecond(Map<String,Object> params,HttpServletResponse response );
    
    List<List<String>> queryAmoebaSecond(Map<String,Object> params);
    
    List<RdsFinanceAptitudModel> queryAptitude(Map<String,Object> params);
    
    int queryCountAptitude(Map<String,Object> params);
    
    boolean saveAptitudeConfig(Map<String,Object> params);

    boolean deleteAptitudeConfig(Map<String,Object> param);
    
    boolean saveFinanceConfig(Map<String,Object> params);
    
    List<String> queryUserDept();

    void financeCaseDetail(Map<String,Object> params);
    
    List<RdsFinanceCaseDetailStatisticsModel> queryCaseDetailAll(Map<String,Object> params);

    int queryCaseDetailAllCount(Map<String,Object> params);
    
    List<RdsFinanceCaseDetailStatisticsModel> queryCaseDetailAllSum(Map<String,Object> params);
    
    List<Map<String, String>> queryUserDepartment(Map<String,Object> params);
    
    void exportCaseDetailAll(Map<String,Object> params,HttpServletResponse response)  throws Exception;
    
    List<RdsFinanceAmoebaStatisticsModel> queryAmoebaInfoPage(Map<String,Object> params);
    
    int queryCountAmoebaInfo(Map<String,Object> params);
    
    boolean insertAmoebaInfo(Map<String,Object> params) throws Exception;
    
    List<RdsStatisticsDepartmentModel> queryDepartmentAll(Map<String,Object> params);
    
    void exportAmoebaTree(Map<String,Object> params,HttpServletResponse response)  throws Exception;
}
