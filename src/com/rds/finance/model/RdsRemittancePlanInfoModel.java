package com.rds.finance.model;

import lombok.Data;

@Data
public class RdsRemittancePlanInfoModel {
	private String contract_id;
	private String contract_remittance_planid;
	private String remittance;
	private String remittance_date;
	private String status;
	private String create_pername;
	private String create_date;
	private Double insideCost;
	private String insideCostUnit;
	private Double manageCost;
	private String manageUnit;
	private Double externalCost;
}
