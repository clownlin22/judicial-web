package com.rds.alcohol.model;

import org.apache.commons.lang.StringUtils;

public class RdsAlcoholCaseExportModel {
	
	private String case_id;
	private String case_code;
	/**
	 * 委托人
	 */
	private String client;
	private String checkper;
	private String checkper_phone;
	private String area_code;
	private String area_name;
	private String province;
	private String county;
	private String city;
	private String report_modelname;
	/**
	 * 事件描述
	 */
	private String event_desc;
	private String remark;
	private String address;
	private String sample_id;
	/**
	 * 受理日期
	 */
	private String accept_time;

	private String close_time;
	private String receiver_id;

	/**
	 * 样本描述
	 */
	private String sample_remark;

	/**
	 * 检验结果
	 */
	private String sample_result;

	/**
	 * 是否AB管
	 */
	private Integer isDoubleTube;

	private String sample_remark2;

	private Integer is_detection;
	
	/**
	 * 委托日期
	 */
	private String client_time;
	private String mail_address;
	private String mail_per;
	private String mail_phone;
	private String attachment;
	private String case_in_per;
	private String case_in_pername;
	private String area;
	private String report_url;
	private int state;
	private String stateStr;
	private String receiver_area;
	private String receiver;
	private String sample_name;
	private String sample_time;
	private String id_number;
	private double real_sum;
	private double stand_sum;
	private double return_sum;
	private String financeRemark;
	private String paragraphtime;
	private String account;
	private String remittanceName;
	private String remittanceDate;
	private String discountPrice;
	private String fees;
	private String siteFee;
	private String case_checkper;
	private String bloodnumA;
	private String bloodnumB;
	private String case_intr;
	private String case_det;
	private String report_model;
	private Integer is_check;
	
	
	
	public String getSample_time() {
		return sample_time;
	}



	public String getReport_model() {
		return report_model;
	}



	public String getReport_modelname() {
		return report_modelname;
	}



	public void setReport_modelname(String report_modelname) {
		this.report_modelname = report_modelname;
	}



	public void setReport_model(String report_model) {
		this.report_model = report_model;
	}



	public void setSample_time(String sample_time) {
		this.sample_time = sample_time;
	}



	public String getArea_name() {
		if(StringUtils.isEmpty(county)&&StringUtils.isNotEmpty(province)&&StringUtils.isNotEmpty(city)) {
			return province + "-" + city ;
		}else if(StringUtils.isEmpty(city)&&StringUtils.isNotEmpty(province)&&StringUtils.isEmpty(county)){
			return province ;
		}else if(StringUtils.isNotEmpty(province)&&StringUtils.isNotEmpty(city)&&StringUtils.isNotEmpty(county)){
			return province + "-" + city + "-" + county;
		}else{
			return "";
		}
	}
	
	

	public String getProvince() {
		return province;
	}



	public void setProvince(String province) {
		this.province = province;
	}



	public String getCounty() {
		return county;
	}



	public void setCounty(String county) {
		this.county = county;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public Integer getIs_check() {
		return is_check;
	}
	public void setIs_check(Integer is_check) {
		this.is_check = is_check;
	}
	public String getCase_intr() {
		return case_intr;
	}
	public void setCase_intr(String case_intr) {
		this.case_intr = case_intr;
	}
	public String getCase_det() {
		return case_det;
	}
	public void setCase_det(String case_det) {
		this.case_det = case_det;
	}
	public String getBloodnumA() {
		return bloodnumA;
	}
	public void setBloodnumA(String bloodnumA) {
		this.bloodnumA = bloodnumA;
	}
	public String getBloodnumB() {
		return bloodnumB;
	}
	public void setBloodnumB(String bloodnumB) {
		this.bloodnumB = bloodnumB;
	}
	public String getCase_checkper() {
		return case_checkper;
	}
	public void setCase_checkper(String case_checkper) {
		this.case_checkper = case_checkper;
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
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public String getEvent_desc() {
		return event_desc;
	}
	public void setEvent_desc(String event_desc) {
		this.event_desc = event_desc;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSample_id() {
		return sample_id;
	}
	public void setSample_id(String sample_id) {
		this.sample_id = sample_id;
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
	public String getReceiver_id() {
		return receiver_id;
	}
	public void setReceiver_id(String receiver_id) {
		this.receiver_id = receiver_id;
	}
	public String getSample_remark() {
		return sample_remark;
	}
	public void setSample_remark(String sample_remark) {
		this.sample_remark = sample_remark;
	}
	public String getSample_result() {
		return sample_result;
	}
	public void setSample_result(String sample_result) {
		this.sample_result = sample_result;
	}
	public Integer getIsDoubleTube() {
		return isDoubleTube;
	}
	public void setIsDoubleTube(Integer isDoubleTube) {
		this.isDoubleTube = isDoubleTube;
	}
	public String getSample_remark2() {
		return sample_remark2;
	}
	public void setSample_remark2(String sample_remark2) {
		this.sample_remark2 = sample_remark2;
	}
	public Integer getIs_detection() {
		return is_detection;
	}
	public void setIs_detection(Integer is_detection) {
		this.is_detection = is_detection;
	}
	public String getClient_time() {
		return client_time;
	}
	public void setClient_time(String client_time) {
		this.client_time = client_time;
	}
	public String getMail_address() {
		return mail_address;
	}
	public void setMail_address(String mail_address) {
		this.mail_address = mail_address;
	}
	public String getMail_per() {
		return mail_per;
	}
	public void setMail_per(String mail_per) {
		this.mail_per = mail_per;
	}
	public String getMail_phone() {
		return mail_phone;
	}
	public void setMail_phone(String mail_phone) {
		this.mail_phone = mail_phone;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
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
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getReport_url() {
		return report_url;
	}
	public void setReport_url(String report_url) {
		this.report_url = report_url;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateStr() {
		return stateStr;
	}
	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}
	public String getReceiver_area() {
		return receiver_area;
	}
	public void setReceiver_area(String receiver_area) {
		this.receiver_area = receiver_area;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getSample_name() {
		return sample_name;
	}
	public void setSample_name(String sample_name) {
		this.sample_name = sample_name;
	}
	public String getId_number() {
		return id_number;
	}
	public void setId_number(String id_number) {
		this.id_number = id_number;
	}
	public double getReal_sum() {
		return real_sum;
	}
	public void setReal_sum(double real_sum) {
		this.real_sum = real_sum;
	}
	public double getStand_sum() {
		return stand_sum;
	}
	public void setStand_sum(double stand_sum) {
		this.stand_sum = stand_sum;
	}
	public double getReturn_sum() {
		return return_sum;
	}
	public void setReturn_sum(double return_sum) {
		this.return_sum = return_sum;
	}
	public String getFinanceRemark() {
		return financeRemark;
	}
	public void setFinanceRemark(String financeRemark) {
		this.financeRemark = financeRemark;
	}
	public String getParagraphtime() {
		return paragraphtime;
	}
	public void setParagraphtime(String paragraphtime) {
		this.paragraphtime = paragraphtime;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
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
	public String getFees() {
		return fees;
	}
	public void setFees(String fees) {
		this.fees = fees;
	}
	public String getSiteFee() {
		return siteFee;
	}
	public void setSiteFee(String siteFee) {
		this.siteFee = siteFee;
	}
	public String getCheckper() {
		return checkper;
	}
	public void setCheckper(String checkper) {
		this.checkper = checkper;
	}
	public String getCheckper_phone() {
		return checkper_phone;
	}
	public void setCheckper_phone(String checkper_phone) {
		this.checkper_phone = checkper_phone;
	}
	
}
