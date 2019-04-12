package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsStatisticsDepartmentModel {
	private String deptid;
	private String deptname;
	private String parentname;
	private String parentdeptcode;
	private String parentid;
	private String deptcode;
	private boolean leaf;
	//收入小计不含税
	private String deptOutTaxSum;
	//服务收入
	private String serviceSum;
	//销售收入
	private String sellSum;
	//内部结算收入
	private String insideSum;
	//收入小计含税
	private String deptInTaxSum;
	//税
	private String taxSum;
	
	//成本合计
	private String deptCostSum;
	//人工成本
	private String deptWagesSum;
	//材料成本
	private String deptMaterialCostSum;
	//委外检测成本
	private String deptExternalCostSum;
	//内部结算成本
	private String deptCostInsideSum;
	//销售管理费用
	private String deptSaleManageSum;
	//资质费用
	private String deptAptitudeSum;
	//其他费用（含折旧及摊销）
	private String deptOtherSum;
	//对外投资
	private String deptInvestmentSum;
	//仪器及设备采购成本
	private String deptInstrumentSum;
	
	
	//利润小计
	private String deptProfit;
	//税
	private String deptTax;
	//净利润
	private String deptNetProfit;
}
