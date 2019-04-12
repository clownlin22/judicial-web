package com.rds.judicial.model;

public class RdsJudicialCaseFeeModel {
	private String id;
	private String fee_id;
	private String case_id;
	private String case_code;
	private double stand_sum;
	private String confirm_date;
	private double real_sum;
	private double return_sum;
	private String oa_num;
	private String areaname;
	private Double discount;
	private String date;
	private String sample_str;
	private String case_remark;
	private String receive_name;
	private String client;
	private String account;
	private String paragraphtime;
	private String remittanceName;
	private String remittanceDate;
	private String remark;
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private String is_delete;
	/**
	 * 财务审核状态 0 正常 1 异常 2日报已结算 3登记状态
	 */

	private int status;
	
	private double discountPrice;
	private double fees;
	private double siteFee;

	public String getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}

	private String finance_remark;
	private String dailyid;
	/**
	 * 0为正常 1为先出报告后付款 2为免单
	 */
	private int type;

	public String getAreaname() {
		return areaname;
	}

	public String getSample_str() {
		return sample_str;
	}

	public void setSample_str(String sample_str) {
		this.sample_str = sample_str;
	}

	public String getCase_remark() {
		return case_remark;
	}

	public void setCase_remark(String case_remark) {
		this.case_remark = case_remark;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public String getFee_id() {
		return fee_id;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getOa_num() {
		return oa_num;
	}

	public void setOa_num(String oa_num) {
		this.oa_num = oa_num;
	}

	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}

	public double getStand_sum() {
		return stand_sum;
	}

	public void setStand_sum(double stand_sum) {
		this.stand_sum = stand_sum;
	}

	public String getCase_code() {
		return case_code;
	}

	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}

	public double getReal_sum() {
		return real_sum;
	}

	public void setReal_sum(double real_sum) {
		this.real_sum = real_sum;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFinance_remark() {
		return finance_remark;
	}

	public void setFinance_remark(String finance_remark) {
		this.finance_remark = finance_remark;
	}

	public String getDailyid() {
		return dailyid;
	}

	public void setDailyid(String dailyid) {
		this.dailyid = dailyid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public RdsJudicialCaseFeeModel() {
		super();
	}

	public RdsJudicialCaseFeeModel(String id, String case_id) {
		super();
		this.id = id;
		this.case_id = case_id;
	}

	public double getReturn_sum() {
		return return_sum;
	}

	public void setReturn_sum(double return_sum) {
		this.return_sum = return_sum;
	}

	public String getReceive_name() {
		return receive_name;
	}

	public void setReceive_name(String receive_name) {
		this.receive_name = receive_name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getParagraphtime() {
		return paragraphtime;
	}

	public void setParagraphtime(String paragraphtime) {
		this.paragraphtime = paragraphtime;
	}

	public String getRemittanceName() {
		return remittanceName;
	}

	public void setRemittanceName(String remittanceName) {
		this.remittanceName = remittanceName;
	}

	public String getRemittanceDate() {
		return remittanceDate;
	}

	public void setRemittanceDate(String remittanceDate) {
		this.remittanceDate = remittanceDate;
	}

	public double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public double getFees() {
		return fees;
	}

	public void setFees(double fees) {
		this.fees = fees;
	}

	public double getSiteFee() {
		return siteFee;
	}

	public void setSiteFee(double siteFee) {
		this.siteFee = siteFee;
	}

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

}
