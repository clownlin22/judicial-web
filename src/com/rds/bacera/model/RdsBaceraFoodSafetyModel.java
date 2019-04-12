package com.rds.bacera.model;


public class RdsBaceraFoodSafetyModel extends RdsBaceraBaseModel {
	private String id;
	//编号
	private String num;
	//样品名称
	private String samplename;
	//生成日期
	private String productiondate;
	//批号
	private String batch;
	//数量
	private String quantity;
	//测试项目
	private String testitems;
	//检验方法
	private String testmethod;
	
	private String program_type;
	//备注
	private String remark;
	//财务备注
	private String remarks;
	//日期
	private String date;
	//服务商
	private String serviceprovider;
	//样本数
	private String sampleCount;
	//录入人
	private String inputperson;
	//归属人
	private String ownperson;
	//归属人姓名
	private String ownpersonname;
	
	private String agentname;
	//是否作废：1，是，2，否
	private String cancelif;
	//是否发报告
	@SuppressWarnings("unused")
	private String reportif;
	//应收款项
	private String receivables;
	//所付款项
	private String payment;
	//实收款项
	private String paid;
	//到款时间
	private String paragraphtime;
	//账户类型
	private String account_type;
	//快递类型
	private String expresstype;
	//快递单号
	private String expressnum;
	//收件人
	private String recive;
	//快递时间
	private String expresstime;
	//快递备注
	private String expressremark;
	//归属地
	private String areaname;
	//汇款账户
	private String remittanceName;
	//汇款日期
	private String remittanceDate;
	//优惠价格
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
	public String getSamplename() {
		return samplename;
	}
	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}
	public String getProductiondate() {
		return productiondate;
	}
	public void setProductiondate(String productiondate) {
		this.productiondate = productiondate;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getTestitems() {
		return testitems;
	}
	public void setTestitems(String testitems) {
		this.testitems = testitems;
	}
	public String getTestmethod() {
		return testmethod;
	}
	public void setTestmethod(String testmethod) {
		this.testmethod = testmethod;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getServiceprovider() {
		return serviceprovider;
	}
	public void setServiceprovider(String serviceprovider) {
		this.serviceprovider = serviceprovider;
	}
	public String getSampleCount() {
		return sampleCount;
	}
	public void setSampleCount(String sampleCount) {
		this.sampleCount = sampleCount;
	}
	public String getInputperson() {
		return inputperson;
	}
	public void setInputperson(String inputperson) {
		this.inputperson = inputperson;
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
	public String getExpressremark() {
		return expressremark;
	}
	public void setExpressremark(String expressremark) {
		this.expressremark = expressremark;
	}
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
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
	public String getProgram_type() {
		return program_type;
	}
	public void setProgram_type(String program_type) {
		this.program_type = program_type;
	}
}
