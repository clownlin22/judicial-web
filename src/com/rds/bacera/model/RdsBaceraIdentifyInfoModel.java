package com.rds.bacera.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class RdsBaceraIdentifyInfoModel {

	private String case_id;
	private String case_code;
	private String case_areacode;
	private String receiver_area;
	private String case_receiver;
	private String receiver_id;
	private String address;
	private String phone;
	private String accept_time;
	private String close_time;
	private String case_in_per;
	private String case_in_pername;
	private String sample_in_time;
	private String remark;
	private int is_delete = 0;
	private String client;
	
	private String stand_sum;
	private String real_sum;
	private String return_sum;
	private String paragraphtime;
	private String discountPrice;
	private String remittanceDate;
	private String remittanceName;
	private String account;
	private String finance_remark;
	
	// 账户类型
	private String expresstype;
	private String expressnum;
	private String expressremark;
	private String agentname;
	// 收件人
	private String recive;
	@SuppressWarnings("unused")
	private String reportif;

	private String fandm;
	private String child;
	private String id_card;
	private String birth_date;
	private String type;
	private String expresstime;
	private String sample_count;
	private String entrustment_time;
	private String entrustment_matter;
	// 确认标识
	private String status;
	
	private String typeid;
	private String case_type;

	public RdsBaceraIdentifyInfoModel(String case_id, String case_code,
			String case_areacode, String phone, String accept_time,
			String case_in_per, String receiver_id) {
		super();
		this.case_id = case_id;
		this.case_code = case_code;
		this.case_areacode = case_areacode;
		this.phone = phone;
		this.accept_time = accept_time;
		this.case_in_per = case_in_per;
		this.receiver_id = receiver_id;
	}

	public RdsBaceraIdentifyInfoModel(String case_id, String case_code,
			String case_areacode, String receiver_id, String phone,
			String accept_time, String case_in_per, String remark,
			String client, String type, String entrustment_time,
			String entrustment_matter, String case_type, String typeid) {
		super();
		this.case_id = case_id;
		this.case_code = case_code;
		this.case_areacode = case_areacode;
		this.receiver_id = receiver_id;
		this.phone = phone;
		this.accept_time = accept_time;
		this.case_in_per = case_in_per;
		this.remark = remark;
		this.client = client;
		this.type=type;
		this.entrustment_time = entrustment_time;
		this.entrustment_matter = entrustment_matter;
		this.case_type = case_type;
		this.typeid = typeid;
	}

	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}

	public String getCase_code() {
		return case_code;
	}

	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}

	public String getCase_areacode() {
		return case_areacode;
	}

	public void setCase_areacode(String case_areacode) {
		this.case_areacode = case_areacode;
	}

	public String getReceiver_area() {
		return receiver_area;
	}

	public void setReceiver_area(String receiver_area) {
		this.receiver_area = receiver_area;
	}

	public String getCase_receiver() {
		return case_receiver;
	}

	public void setCase_receiver(String case_receiver) {
		this.case_receiver = case_receiver;
	}

	public String getReceiver_id() {
		return receiver_id;
	}

	public void setReceiver_id(String receiver_id) {
		this.receiver_id = receiver_id;
	}

	public String getAddress() {
		return address == null ? "" : this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAccept_time() {
		return accept_time;
	}

	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}

	public String getClose_time() {
		return close_time;
	}

	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}

	public String getCase_in_per() {
		return case_in_per;
	}

	public void setCase_in_per(String case_in_per) {
		this.case_in_per = case_in_per;
	}

	public String getCase_in_pername() {
		return case_in_pername;
	}

	public void setCase_in_pername(String case_in_pername) {
		this.case_in_pername = case_in_pername;
	}

	public String getSample_in_time() {
		return sample_in_time;
	}

	public void setSample_in_time(String sample_in_time) {
		this.sample_in_time = sample_in_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getClient() {
		return client == null ? "" : this.client;
	}

	public void setClient(String client) {
		this.client = client;
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

	public int getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
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

	public String getAgentname() {
		return agentname;
	}

	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}

	public String getFandm() {
		return fandm;
	}

	public void setFandm(String fandm) {
		this.fandm = fandm;
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}

	public String getId_card() {
		return id_card;
	}

	public void setId_card(String id_card) {
		this.id_card = id_card;
	}

	public String getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExpresstime() {
		return expresstime;
	}

	public void setExpresstime(String expresstime) {
		this.expresstime = expresstime;
	}

	public String getSample_count() {
		return sample_count;
	}

	public void setSample_count(String sample_count) {
		this.sample_count = sample_count;
	}

	public String getEntrustment_time() {
		return entrustment_time;
	}

	public void setEntrustment_time(String entrustment_time) {
		this.entrustment_time = entrustment_time;
	}

	public String getEntrustment_matter() {
		return entrustment_matter;
	}

	public void setEntrustment_matter(String entrustment_matter) {
		this.entrustment_matter = entrustment_matter;
	}

	public String getCase_type() {
		return case_type;
	}

	public void setCase_type(String case_type) {
		this.case_type = case_type;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getExpressremark() {
		return expressremark;
	}

	public void setExpressremark(String expressremark) {
		this.expressremark = expressremark;
	}

	public String getStand_sum() {
		return stand_sum;
	}

	public void setStand_sum(String stand_sum) {
		this.stand_sum = stand_sum;
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

	public String getFinance_remark() {
		return finance_remark;
	}

	public void setFinance_remark(String finance_remark) {
		this.finance_remark = finance_remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParagraphtime() {
		return paragraphtime;
	}

	public void setParagraphtime(String paragraphtime) {
		this.paragraphtime = paragraphtime;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getRemittanceDate() {
		return remittanceDate;
	}

	public void setRemittanceDate(String remittanceDate) {
		this.remittanceDate = remittanceDate;
	}

	public String getRemittanceName() {
		return remittanceName;
	}

	public void setRemittanceName(String remittanceName) {
		this.remittanceName = remittanceName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
