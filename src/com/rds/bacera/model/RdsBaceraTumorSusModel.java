package com.rds.bacera.model;

/**
 * this model be used by Tumor susceptibility gene , 2016-06-21 used by
 * Slim-Beauty-PCR 2016-06-30 user by Ind-character-PCR and
 * Major-chronic-diseases-PCR
 * 肿瘤易感基因-实体
 * @author yxb
 *
 */
public class RdsBaceraTumorSusModel {
	
	private String id;
	//编号
	private String num;
	//日期
	private String date;
	//姓名
	private String name;
	//年龄
	private String age;
	//检测项目
	private String testitems;
	//送检人
	private String checkper;
	//电话
	private String phonenum;
	//案例备注
	private String remark;
	//财务备注
	private String remarks;
	//是否废除:1是2否
	private String cancelif;
	//性别：男，女
	private String gender;
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
	//代理商id
	private String agentid;
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
	//报告种类
	private String reportType;
	//接单平台
	private String orderPlatform;
	//送检单位
	private String inspectionUnits;
	//案例类型
	private String case_type;
	
	private String expressremark;

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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getTestitems() {
		return testitems;
	}
	public void setTestitems(String testitems) {
		this.testitems = testitems;
	}
	public String getCheckper() {
		return checkper;
	}
	public void setCheckper(String checkper) {
		this.checkper = checkper;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getOrderPlatform() {
		return orderPlatform;
	}
	public void setOrderPlatform(String orderPlatform) {
		this.orderPlatform = orderPlatform;
	}
	public String getInspectionUnits() {
		return inspectionUnits;
	}
	public void setInspectionUnits(String inspectionUnits) {
		this.inspectionUnits = inspectionUnits;
	}
	public String getCase_type() {
		return case_type;
	}
	public void setCase_type(String case_type) {
		this.case_type = case_type;
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
