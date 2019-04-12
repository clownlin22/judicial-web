package com.rds.statistics.model;


public class RdsAllCaseInfoModel {
    private String accept_time;
    private String case_code;
    private String real_sum;
    private String return_sum;
    private String ptype;
    private String rtype;
    private String receiver;
    private String user_dept_level1;
    private String user_dept_level2;
    private String user_dept_level3;
    private String user_dept_level4;
    private String user_dept_level5;
    private String agent;
    private String case_area;
    private String partner;
    private String status;
    private String remittanceDate;
    private String sample_count;
    private String remark;
    private String finance_remark;
    private String client;
    private String deductible;
	public String getAccept_time() {
		return accept_time;
	}
	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}
	public String getCase_code() {
		return case_code;
	}
	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}
	public String getReal_sum() {
		return real_sum;
	}
	public void setReal_sum(String real_sum) {
		this.real_sum = real_sum;
	}
	public String getReturn_sum() {
		return return_sum;
	}
	public void setReturn_sum(String return_sum) {
		this.return_sum = return_sum;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	public String getRtype() {
		return rtype;
	}
	public void setRtype(String rtype) {
		this.rtype = rtype;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getCounty() {
		return case_area==null?"":(case_area.split("-").length>=3?case_area.split("-")[2]:"");
	}
	public String getCity() {
		return case_area==null?"":(case_area.split("-").length>=2?case_area.split("-")[1]:"");
	}
	public String getProvince() {
		return case_area==null?"":(case_area.split("-").length>=1?case_area.split("-")[0]:"");
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemittanceDate() {
		return remittanceDate;
	}
	public void setRemittanceDate(String remittanceDate) {
		this.remittanceDate = remittanceDate;
	}
	public String getSample_count() {
		return sample_count;
	}
	public void setSample_count(String sample_count) {
		this.sample_count = sample_count;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFinance_remark() {
		return finance_remark;
	}
	public void setFinance_remark(String finance_remark) {
		this.finance_remark = finance_remark;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getUser_dept_level1() {
		return user_dept_level1;
	}
	public void setUser_dept_level1(String user_dept_level1) {
		this.user_dept_level1 = user_dept_level1;
	}
	public String getUser_dept_level2() {
		return user_dept_level2;
	}
	public void setUser_dept_level2(String user_dept_level2) {
		this.user_dept_level2 = user_dept_level2;
	}
	public String getUser_dept_level3() {
		return user_dept_level3;
	}
	public void setUser_dept_level3(String user_dept_level3) {
		this.user_dept_level3 = user_dept_level3;
	}
	
	public String getUser_dept_level4() {
		return user_dept_level4;
	}
	public void setUser_dept_level4(String user_dept_level4) {
		this.user_dept_level4 = user_dept_level4;
	}
	public String getUser_dept_level5() {
		return user_dept_level5;
	}
	public void setUser_dept_level5(String user_dept_level5) {
		this.user_dept_level5 = user_dept_level5;
	}
	public String getCase_area() {
		return case_area;
	}
	public void setCase_area(String case_area) {
		this.case_area = case_area;
	}
	public String getDeductible() {
		return deductible;
	}
	public void setDeductible(String deductible) {
		this.deductible = deductible;
	}
    
}
