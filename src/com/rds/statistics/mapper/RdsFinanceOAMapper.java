package com.rds.statistics.mapper;

import java.util.List;
import java.util.Map;

import com.rds.statistics.model.RdsFinanceOAModel;
import com.rds.statistics.model.RdsFinanceOATypeModel;
import com.rds.statistics.model.RdsStatisticsTypeModel;
import com.rds.statistics.model.RdsStatisticsTypeModel2;

public interface RdsFinanceOAMapper {
	
    List<RdsFinanceOAModel> queryAllPage(Map<String,Object> params);

    int queryAllCount(Map<String,Object> params);
    
    List<RdsFinanceOAModel> queryExist1(Map<String,Object> params);
    
    List<RdsFinanceOAModel> queryExist2(Map<String,Object> params);
    
    boolean updateOAdept1(Map<String,Object> params);
    
    boolean updateOAdept2(Map<String,Object> params);
    
    boolean insertOALog(Map<String,Object> params);
    
    boolean insertOAtype(Map<String,Object> params);
    
    boolean updateOAtype(Map<String,Object> params);
    
    boolean deleteOAtype(Map<String,Object> params);
    
    List<RdsFinanceOATypeModel> queryOAtypePage(Map<String,Object> params);
    
    int queryOAtypeCount(Map<String,Object> params);

	List<RdsStatisticsTypeModel2> queryUserDeptToModel();

	boolean updateOAInfo1(Map<String, Object> params);

	boolean updateOAInfo2(Map<String, Object> params);

	List<RdsStatisticsTypeModel2> queryUserDeptToModel2(Map<String, Object> params);

	String queryBuMen(String deptid);
    
}
