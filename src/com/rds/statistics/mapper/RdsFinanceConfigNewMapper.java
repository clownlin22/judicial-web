package com.rds.statistics.mapper;

import java.util.List;
import java.util.Map;

import com.rds.statistics.model.RdsAllCaseInfoModel;
import com.rds.statistics.model.RdsFinanceAmoebaStatisticsModel;
import com.rds.statistics.model.RdsFinanceAptitudModel;
import com.rds.statistics.model.RdsFinanceCaseDetailModel;
import com.rds.statistics.model.RdsFinanceCaseDetailStatisticsModel;
import com.rds.statistics.model.RdsFinanceConfigModel;
import com.rds.statistics.model.RdsFinanceProgramModel;
import com.rds.statistics.model.RdsStatisticsDepartmentModel;

public interface RdsFinanceConfigNewMapper {
    List<RdsFinanceConfigModel> queryAll(Map<String,Object> params);

    int queryAllCount(Map<String,Object> params);
    
    List<RdsFinanceProgramModel> queryProgramFinance(Map<String,Object> params);
    
    /**
     * 插入内部结算费用
     * @param params
     * @return
     */
    boolean insertProgramPrice(Map<String,Object> params);
    
    /**
     * 查询所有合作方
     * @param params
     * @return
     */
    List<RdsFinanceAptitudModel> queryAptitude(Map<String,Object> params);
    
    int queryCountAptitude(Map<String,Object> params);
    
    /**
     * 插入月度合作方资质费
     * @param params
     * @return
     */
    boolean insertAptitudeFee(Map<String,Object> params);
    
    /**
     * 委外费用
     * @param params
     * @return
     */
    boolean insertOutsourcinFee(Map<String,Object> params);
    /**
     * 插入渠道月份资质费
     * @param params
     * @return
     */
    boolean insertChannelAptitudeFee(Map<String,Object> params);
    
    /**
     * 查询所有事业部
     * @return
     */
    List<String> queryUserDept();
    /**
     * 查询事业部 二级部门
     * @return
     */
    List<String> queryUserDeptSecond(Map<String,Object> params);
    
    /**
     * 根据事业部查询该月份应收服务收入总额
     * @param params
     * @return
     */
    List<Map<String, Object>> queryDepServiceSum(Map<String,Object> params);
    List<Map<String, Object>> queryDepServiceSumSecond(Map<String,Object> params);

    List<Map<String, Object>> queryDeptDepreciationCostSum(Map<String,Object> params);
    List<Map<String, Object>> querySaleManageSum(Map<String,Object> params);
    
    List<Map<String, Object>> queryDeptInstrumentCostSum(Map<String,Object> params);
    
    /**
     * 根据事业部查询该月份应收销售收入总额
     * @param params
     * @return
     */
    List<Map<String, Object>> queryDepSellSum(Map<String,Object> params);
    List<Map<String, Object>> queryDepSellSumSecond(Map<String,Object> params);
    
    /**
     * 资质费
     * @param params
     * @return
     */
    List<Map<String, Object>> queryDeptAptitudeCostSum(Map<String,Object> params);
    List<Map<String, Object>> queryDeptAptitudeCostSumSecond(Map<String,Object> params);

    /**
     * 人工成本
     * @param params
     * @return
     */
    List<Map<String, Object>> queryDeptWagesSum(Map<String,Object> params);
    List<Map<String, Object>> queryDeptWagesSumSecond(Map<String,Object> params);
    
    /**
     * 材料成本
     * @param params
     * @return
     */
    List<Map<String, Object>>  queryDeptMaterialCostSum(Map<String,Object> params);
    
    /**
     * 根据事业部查询内部结算收入
     * @param params
     * @return
     */
    List<Map<String, Object>> queryDeptInsideSum(Map<String,Object> params);
    
    /**
     * 根据事业部查询内部结算支出
     * @param params
     * @return
     */
    List<Map<String, Object>> queryDeptExternalCostSum(Map<String,Object> params);
    List<Map<String, Object>> queryDeptExternalCostSumSecond(Map<String,Object> params);
    
    /**
     * 插入合作方资质管理
     * @param params
     * @return
     */
    boolean insertAptitudeConfig(Map<String,Object> params);
    
    /**
     * 更新合作方资质管理
     * @param params
     * @return
     */
    boolean updateApitudeConfig(Map<String,Object> params);
    
    /**
     * 作废合作方资质
     * @param params
     * @return
     */
    boolean updateApitudeConfigFlag(Map<String,Object> params);
    
    boolean insertFinanceConfig(Map<String,Object> params);
    
    boolean updateFinanceConfig(Map<String,Object> params);
    
    boolean insertCostPrice(Map<String,Object> params);

    /**
     * 根据事业部查询内部结算成本
     * @param params
     * @return
     */
    List<Map<String, Object>> queryCostInsideSum(Map<String,Object> params);
    List<Map<String, Object>> queryCostInsideSumSecond(Map<String,Object> params);
    
    /**
     * 查询当天每个财务确认的案例
     * @param params
     * @return
     */
    List<RdsFinanceCaseDetailModel> queryFinanceCaseDetail(Map<String,Object> params);
    
    boolean insertFinanceCaseDetail(Map<String,Object> params);
    
    /**
     * 合同款项
     * @param params
     * @return
     */
    boolean insertContractStatistic(Map<String,Object> params);
    
    /**
     * 试剂盒管理
     * @param params
     * @return
     */
    boolean insertKitSetStatistic(Map<String,Object> params);
    
    /**条件查询案例  */
    List<RdsFinanceCaseDetailStatisticsModel> queryCaseDetailAll(Map<String,Object> params);
    
    int queryCaseDetailAllCount(Map<String,Object> params);
    
    List<RdsFinanceCaseDetailStatisticsModel> queryCaseDetailAllSum(Map<String,Object> params);
    /**条件查询案例  */
    
    List<Map<String, String>> queryUserDepartment(Map<String,Object> params);
    
    /** 阿米巴项目录入分页查询*/
    List<RdsFinanceAmoebaStatisticsModel> queryAmoebaInfoPage(Map<String,Object> params);
    
    int queryCountAmoebaInfo(Map<String,Object> params);
    
    boolean insertAmoebaInfo(Map<String,Object> params);
    /** 阿米巴项目录入分页查询*/
    
    List<Map<String,Object>> selectUserLevel(String usercode);
    
    public List<RdsStatisticsDepartmentModel> queryDepartmentAll(Object params);
    
    public Map<String,Object> queryDeptParentId(String deptcode);

	List<RdsStatisticsDepartmentModel> queryUserDeptAndId();

	List<Map<String, Object>> queryDepHeZou(Map<String, Object> params);

	List<Map<String, Object>> queryDeptBeiYonCostSum(Map<String, Object> params);

	List<Map<String, Object>> queryDeptHeZouFangCostSum(Map<String, Object> params);

	List<String> queryUserDeptThree(Map<String, Object> params);

	List<RdsStatisticsDepartmentModel> queryXiaJiBuMen(String listDeptTempId);
	List<String> queryXiaJiBuMen2(String listDeptTempId);

	RdsStatisticsDepartmentModel queryBuMen(String listDeptTempId);

	Map<String, String> queryListBuMen(String listDeptTempId);

	RdsStatisticsDepartmentModel queryBuMen2(String listDeptTempId);

	List<Map<String, String>> queryUserDepartment2(Map<String, Object> params);

	List<Map<String, Object>> queryDeptInsideSum1(Map<String, Object> params);
	List<Map<String, Object>> queryDeptInsideSum2(Map<String, Object> params);
	List<Map<String, Object>> queryDeptInsideSum3(Map<String, Object> params);

	List<Map<String, Object>> queryDepServiceSum2(Map<String, Object> params);

	List<Map<String, Object>> queryDeptWagesSum2(Map<String, Object> map);

	List<Map<String, Object>> queryCostInsideSum2(Map<String, Object> params);




}
