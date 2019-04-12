package com.rds.bacera.model;




public class RdsBaceraTumorMarkerModel extends RdsBaceraBaseModel{
	private String id;
	private String num;
	private String sex;
	private String date;
	private String name;
	private String program;//检测项目
	private String inspection;//送检人
	
	private String receivables;
	private String payment;
	private String paid;
	private String paragraphtime;
	private String account_type;//账目类型
	

	private String ownperson;
	private String ownpersonname;
	
	private String areaname;
	private String areacode;
	
	private String expresstype;
	private String expressnum;
	private String recive;
	
	private String cancelif;
	private String reportif;
	private String remark;
	private String remarks;
	private String expresstime;
	private String expressremark;

	
	private String remittanceName;
	private String remittanceDate;
	
	private String discountPrice;

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getInspection() {
		return inspection;
	}

	public void setInspection(String inspection) {
		this.inspection = inspection;
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

	public String getOwnpersonname() {
		return ownpersonname;
	}

	public void setOwnpersonname(String ownpersonname) {
		this.ownpersonname = ownpersonname;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
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

	public String getExpressremark() {
		return expressremark;
	}

	public void setExpressremark(String expressremark) {
		this.expressremark = expressremark;
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
	
	
}
