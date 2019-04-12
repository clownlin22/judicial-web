package com.rds.bacera.model;

import java.sql.Date;

public class BaceraGeneTestingProjectModel extends RdsBaceraBaseModel{
	private String id;
	private int bioms_genetic_test_id;
	private Date add_time;
	private String consumer_name;//检测客户姓名
	private String consumer_sex;//检测客户性别
	private String consumer_birthday;//检测用户生日
	private String consumer_phone;//检测用户手机号
	private String sample_number;//样本编号
	private String test_number;//报告编码
	
	private String age;//年龄
	private String hospital;//医院
	private String office;//科室
	private String sample_type;
	private String admission_num;
	private String sample_status;
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getSample_type() {
		return sample_type;
	}
	public void setSample_type(String sample_type) {
		this.sample_type = sample_type;
	}
	public String getAdmission_num() {
		return admission_num;
	}
	public void setAdmission_num(String admission_num) {
		this.admission_num = admission_num;
	}
	public String getSample_status() {
		return sample_status;
	}
	public void setSample_status(String sample_status) {
		this.sample_status = sample_status;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getBed_num() {
		return bed_num;
	}
	public void setBed_num(String bed_num) {
		this.bed_num = bed_num;
	}
	public String getGenetic_test_id() {
		return genetic_test_id;
	}
	public void setGenetic_test_id(String genetic_test_id) {
		this.genetic_test_id = genetic_test_id;
	}
	private String doctor;
	private String bed_num;
	private String genetic_test_id;
	public  String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getBioms_genetic_test_id() {
		return bioms_genetic_test_id;
	}
	public void setBioms_genetic_test_id(int bioms_genetic_test_id) {
		this.bioms_genetic_test_id = bioms_genetic_test_id;
	}
	public Date getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}
	public String getConsumer_name() {
		return consumer_name;
	}
	public void setConsumer_name(String consumer_name) {
		this.consumer_name = consumer_name;
	}
	public String getConsumer_sex() {
		return consumer_sex;
	}
	public String getSConsumer_sex() {
		if("F".equals(consumer_sex)){
			return "女";}
		return "男";
	}
	public void setConsumer_sex(String consumer_sex) {
		this.consumer_sex = consumer_sex;
	}
	public String getConsumer_birthday() {
		return consumer_birthday;
	}
	public void setConsumer_birthday(String consumer_birthday) {
		this.consumer_birthday = consumer_birthday;
	}
	public String getConsumer_phone() {
		return consumer_phone;
	}
	public void setConsumer_phone(String consumer_phone) {
		this.consumer_phone = consumer_phone;
	}
	public String getSample_number() {
		return sample_number;
	}
	public void setSample_number(String sample_number) {
		this.sample_number = sample_number;
	}
	public String getTest_number() {
		return test_number;
	}
	public void setTest_number(String test_number) {
		this.test_number = test_number;
	}
	public String getReport_date() {
		return report_date;
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public int getTest_package_id() {
		return test_package_id;
	}
	public void setTest_package_id(int test_package_id) {
		this.test_package_id = test_package_id;
	}
	public String getTest_package_name() {
		return test_package_name;
	}
	public void setTest_package_name(String test_package_name) {
		this.test_package_name = test_package_name;
	}
	public int getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(int agency_id) {
		this.agency_id = agency_id;
	}
	public String getAgency_name() {
		return agency_name;
	}
	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}
	public String getTest_item_ids() {
		return test_item_ids;
	}
	public void setTest_item_ids(String test_item_ids) {
		this.test_item_ids = test_item_ids;
	}
	public String getTest_item_names() {
		return test_item_names;
	}
	public void setTest_item_names(String test_item_names) {
		this.test_item_names = test_item_names;
	}
	public String getCharge_standard_id() {
		return charge_standard_id;
	}
	public void setCharge_standard_id(String charge_standard_id) {
		this.charge_standard_id = charge_standard_id;
	}
	public String getCharge_standard_name() {
		return charge_standard_name;
	}
	public void setCharge_standard_name(String charge_standard_name) {
		this.charge_standard_name = charge_standard_name;
	}
	public int getPrice() {
		return price;
	}
	public Double getSPrice() {
		return price*0.01;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCase_type() {
		return case_type;
	}
	public void setCase_type(String case_type) {
		this.case_type = case_type;
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
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getOa_num() {
		return oa_num;
	}
	public void setOa_num(String oa_num) {
		this.oa_num = oa_num;
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
	public String getExpressremark() {
		return expressremark;
	}
	public void setExpressremark(String expressremark) {
		this.expressremark = expressremark;
	}
	public String getExpresstime() {
		return expresstime;
	}
	public void setExpresstime(String expresstime) {
		this.expresstime = expresstime;
	}
	public String getRecive() {
		return recive;
	}
	public void setRecive(String recive) {
		this.recive = recive;
	}
	private String report_date;//出报告时间
	private int test_package_id;//检测套餐id
	private String test_package_name;//检测套餐名称
	private int agency_id;//代理商id
	private String agency_name;//代理商名
	private String test_item_ids;//检测项ids
	private String test_item_names;//检测项名称
	private String charge_standard_id;//归属人id
	private String charge_standard_name;//归属人全称
	private int price;//应收金额
	
	private String case_type;//检测项目名与test_item_names一致
	
	private String receivables;//应收
	private String payment;//实收
	private String paid;//回款
	private String paragraphtime;//到款时间
	private String discountPrice;
	private String account_type;//账户类型
	private String remittanceDate;
	private String remittanceName;
	private String remarks;
	private String oa_num;
	
	private String remark;
	
	private String expresstype;
	private String expressnum;
	private String expressremark;
	private String expresstime;
	// 收件人
	private String recive;
	
	
	

}
