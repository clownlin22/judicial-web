package com.rds.bacera.model;

/**
 * 
 * @author yxb
 *
 */
public class RdsBaceraDocumentAppCooModel {

	private String id;
	// 项目编号
	private String num;
	private String client;
	private String phone;
	private String client_date;
	private String accept_date;
	private String appraisal_pro;
	private String basic_case;
	private String appraisal_end_date;
	private String return_type;
	private String invoice_exp;
	private String case_close;
	// 案例备注
	private String remark;
	//录入人
	private String create_pername;
	
	private String create_time;
	// 财务备注
	private String finance_remark;
	// 是否废除:1是2否
	private String cancelif;
	// 实收金额
	private String receivables;
	// 回款金额
	private String payment;
	// 标准金额
	private String paid;
	// 到款时间
	private String paragraphtime;
	// 账户类型
	private String account_type;
	// 代理商姓名
	private String agentname;
	// 归属人id
	private String ownperson;
	// 归属人姓名
	private String ownpersonname;
	// 归属地
	private String areaname;
	// 快递类型
	private String expresstype;
	// 快递编号
	private String expressnum;
	// 快递时间
	private String expresstime;
	// 快递备注
	private String expressremark;
	// 收件人
	private String recive;
	// 是否发报告
	@SuppressWarnings("unused")
	private String reportif;
	// 汇款账户
	private String remittanceName;
	// 汇款日期
	private String remittanceDate;
	// 优惠价格
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

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getClient_date() {
		return client_date;
	}

	public void setClient_date(String client_date) {
		this.client_date = client_date;
	}

	public String getAccept_date() {
		return accept_date;
	}

	public void setAccept_date(String accept_date) {
		this.accept_date = accept_date;
	}

	public String getAppraisal_pro() {
		return appraisal_pro;
	}

	public void setAppraisal_pro(String appraisal_pro) {
		this.appraisal_pro = appraisal_pro;
	}

	public String getBasic_case() {
		return basic_case;
	}

	public void setBasic_case(String basic_case) {
		this.basic_case = basic_case;
	}

	public String getAppraisal_end_date() {
		return appraisal_end_date;
	}

	public void setAppraisal_end_date(String appraisal_end_date) {
		this.appraisal_end_date = appraisal_end_date;
	}

	public String getReturn_type() {
		return return_type;
	}

	public void setReturn_type(String return_type) {
		this.return_type = return_type;
	}

	public String getInvoice_exp() {
		return invoice_exp;
	}

	public void setInvoice_exp(String invoice_exp) {
		this.invoice_exp = invoice_exp;
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

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
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

	public String getRecive() {
		return recive;
	}

	public void setRecive(String recive) {
		this.recive = recive;
	}

	public String getReportif() {
		return expresstype == null ? "2" : "1";
	}

	public void setReportif(String reportif) {
		this.reportif = reportif;
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

	public String getCreate_pername() {
		return create_pername;
	}

	public void setCreate_pername(String create_pername) {
		this.create_pername = create_pername;
	}

	public String getCase_close() {
		return case_close;
	}

	public void setCase_close(String case_close) {
		this.case_close = case_close;
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

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
