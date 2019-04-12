package com.rds.bacera.model;

/**
 * this model be used by ctDNA , 2016-11-14 used by
 * ctDNA 2016-11-14 user by Ind-character-PCR and
 * 
 * @author yxb
 *
 */
public class RdsBaceraCTDnaModel extends RdsBaceraBaseModel{
	
	private String id;
	//编号
	private String num;
	//日期
	private String date;
	//姓名
	private String name;
	//年龄
	private String sex;
	//临床诊断
	private String clinical_diagnosis;
	//具体用药史和疗效
	private String histort_effect;
	//案例备注
	private String remark;
	//财务备注
	private String finance_remark;
	//是否废除:1是2否
	private String cancelif;
	//实收金额
	private String receivables;
	//回款金额
	private String payment;
	//标准金额
	private String paid;
	//到款时间
	private String paragraphtime;
	//账户类型
	private String account_type;
	//代理商姓名
	private String agentname;
	//归属人id
	private String ownperson;
	//归属人姓名
	private String ownpersonname;
	//归属地
	private String areaname;
	//快递类型
	private String expresstype;
	//快递编号
	private String expressnum;
	//快递时间
	private String expresstime;
	//快递备注
	private String expressremark;
	//收件人
	private String recive;
	//是否发报告
	@SuppressWarnings("unused")
	private String reportif;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCancelif() {
		return cancelif;
	}
	public void setCancelif(String cancelif) {
		this.cancelif = cancelif;
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
	public String getReportif() {
		return expresstype==null?"2":"1";
	}
	public void setReportif(String reportif) {
		this.reportif = reportif;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getClinical_diagnosis() {
		return clinical_diagnosis;
	}
	public void setClinical_diagnosis(String clinical_diagnosis) {
		this.clinical_diagnosis = clinical_diagnosis;
	}
	public String getHistort_effect() {
		return histort_effect;
	}
	public void setHistort_effect(String histort_effect) {
		this.histort_effect = histort_effect;
	}
	public String getFinance_remark() {
		return finance_remark;
	}
	public void setFinance_remark(String finance_remark) {
		this.finance_remark = finance_remark;
	}
	
}
