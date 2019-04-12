package com.rds.bacera.model;


public class RdsBaceraForensicModel {
	private String id;
	private String num;
	private String date;
	private String name;
	private String receivables;
	private String payment;
	private String paid;
	private String paragraphtime;
	private String account_type;
	private String ownperson;
	private String agentid;
	private String agentname;
	private String ownpersonname;
	@SuppressWarnings("unused")
	private String reportif;
	private String remark;
	private String remarks;
	private String expresstype;
	private String expressnum;
	private String expressremark;
	private String recive;
	private String cancelif;
	private String oa_num;
	private String client;
	private String commitment;
	private String expresstime;
	private String areaname;
	private String remittanceName;
	private String remittanceDate;
	
	private String discountPrice;
	private String confirm_flag;
	private String confirm_date;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReceivables() {
		return receivables;
	}
	public void setReceivables(String receivables) {
		this.receivables = receivables;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getPaid() {
		return paid;
	}
	public void setPaid(String paid) {
		this.paid = paid;
	}
	public String getParagraphtime() {
		return paragraphtime;
	}
	public void setParagraphtime(String paragraphtime) {
		this.paragraphtime = paragraphtime;
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public String getOwnperson() {
		return ownperson;
	}
	public void setOwnperson(String ownperson) {
		this.ownperson = ownperson;
	}
	public String getAgentid() {
		return agentid;
	}
	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	public String getOwnpersonname() {
		return ownpersonname;
	}
	public void setOwnpersonname(String ownpersonname) {
		this.ownpersonname = ownpersonname;
	}
	public String getReportif() {
		return expresstype==null?"2":"1";
	}
	public void setReportif(String reportif) {
		this.reportif = reportif;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getExpresstype() {
		return expresstype;
	}
	public void setExpresstype(String expresstype) {
		this.expresstype = expresstype;
	}
	public String getExpressnum() {
		return expressnum;
	}
	public void setExpressnum(String expressnum) {
		this.expressnum = expressnum;
	}
	public String getRecive() {
		return recive;
	}
	public void setRecive(String recive) {
		this.recive = recive;
	}
	public String getCancelif() {
		return cancelif;
	}
	public void setCancelif(String cancelif) {
		this.cancelif = cancelif;
	}
	public String getOa_num() {
		return oa_num;
	}
	public void setOa_num(String oa_num) {
		this.oa_num = oa_num;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getCommitment() {
		return commitment;
	}
	public void setCommitment(String commitment) {
		this.commitment = commitment;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getExpresstime() {
		return expresstime;
	}
	public void setExpresstime(String expresstime) {
		this.expresstime = expresstime;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
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
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}
	public String getExpressremark() {
		return expressremark;
	}
	public void setExpressremark(String expressremark) {
		this.expressremark = expressremark;
	}
	public String getConfirm_flag() {
		return confirm_flag;
	}
	public void setConfirm_flag(String confirm_flag) {
		this.confirm_flag = confirm_flag;
	}
	public String getConfirm_date() {
		return confirm_date;
	}
	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}
	
}
