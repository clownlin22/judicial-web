package com.rds.bacera.model;



public class RdsBaceraMedExamineModel extends RdsBaceraBaseModel{
	private String id;
	private String num;
	private String date;
	private String report_date;
	private String name;
	private String entrustment_date;
	private String barcode;
	private String program;
	private String diagnosis;
	private String age;
	private String sex;
	private String sampletype;
	private String samplecount;
	private String hospital;
	

	private String added_tax;
	private String additional_tax;
	private String total_aftertax;
	private String returnsum_aftertax;
	private String receivables;
	private String payment;
	private String paid;
	private String paragraphtime;
	private String account_type;
	
	private String agentname;
	private String ownperson;
	private String ownpersonname;
	
	private String expresstype;
	private String expressnum;
	private String recive;
	
	private String cancelif;
	@SuppressWarnings("unused")
	private String reportif;
	private String remark;
	private String remarks;
	private String expresstime;
	private String expressremark;
	private String areaname;
	
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSampletype() {
		return sampletype;
	}
	public void setSampletype(String sampletype) {
		this.sampletype = sampletype;
	}
	public String getSamplecount() {
		return samplecount;
	}
	public void setSamplecount(String samplecount) {
		this.samplecount = samplecount;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
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
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
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
	public String getExpressremark() {
		return expressremark;
	}
	public void setExpressremark(String expressremark) {
		this.expressremark = expressremark;
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
	public String getReport_date() {
		return report_date;
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public String getEntrustment_date() {
		return entrustment_date;
	}
	public void setEntrustment_date(String entrustment_date) {
		this.entrustment_date = entrustment_date;
	}
	public String getAdded_tax() {
		return added_tax;
	}
	public void setAdded_tax(String added_tax) {
		this.added_tax = added_tax;
	}
	public String getAdditional_tax() {
		return additional_tax;
	}
	public void setAdditional_tax(String additional_tax) {
		this.additional_tax = additional_tax;
	}
	public String getTotal_aftertax() {
		return total_aftertax;
	}
	public void setTotal_aftertax(String total_aftertax) {
		this.total_aftertax = total_aftertax;
	}
	public String getReturnsum_aftertax() {
		return returnsum_aftertax;
	}
	public void setReturnsum_aftertax(String returnsum_aftertax) {
		this.returnsum_aftertax = returnsum_aftertax;
	}
	
}
