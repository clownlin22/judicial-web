package com.rds.children.model;

import java.sql.Date;

public class RdsChildrenMailCaseModel {

	private String case_userid;
	private String address;
	private String birth_hospital;
	private String house_area;
	private String life_area;
	private String case_in_per;
	private String case_in_pername;
	private String case_in_time;
	private int is_delete = 0;
	private String agentia_id;
	private String agentia_name;
	private String invoice;
	private String mail_area;
	private String tariff_id;
	private String tariff_name;
	private String mail_name;
	private String mail_code;
	private String mail_time;

	private String case_id;
	private String case_code;
	private String sample_code;
	private String child_name;
	private int child_sex = 0;
	private String birth_date;
	private String id_number;
	private String print_time;
	private String case_username;
	private String case_areaname;
    private String mail_info;
	private String remark;
	private int fee_type;
	private int status;
	private String fee_remark;
    private String case_type;
    private int mail_count;
    private Date gather_time;
    private String gather_id;
	public String getPrint_time() {
		return print_time;
	}

	public void setPrint_time(String print_time) {
		this.print_time = print_time;
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

	public String getSample_code() {
		return sample_code;
	}

	public void setSample_code(String sample_code) {
		this.sample_code = sample_code;
	}

	public String getChild_name() {
		return child_name;
	}

	public void setChild_name(String child_name) {
		this.child_name = child_name;
	}

	public int getChild_sex() {
		return child_sex;
	}

	public void setChild_sex(int child_sex) {
		this.child_sex = child_sex;
	}

	public String getBirth_date() {
		return birth_date;
//		if(StringUtils.isNotEmpty(birth_date)){
//			try {
//				return DateUtils.formatzh2.format(DateUtils.lineformat.parse(birth_date));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
//		return "";
	}

	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getCase_username() {
		return case_username;
	}

	public void setCase_username(String case_username) {
		this.case_username = case_username;
	}

	public String getCase_areaname() {
		return case_areaname;
	}

	public void setCase_areaname(String case_areaname) {
		this.case_areaname = case_areaname;
	}

	public String getMail_info() {
		return mail_info;
	}

	public void setMail_info(String mail_info) {
		this.mail_info = mail_info;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getFee_type() {
		return fee_type;
	}

	public void setFee_type(int fee_type) {
		this.fee_type = fee_type;
	}

	public String getFee_remark() {
		return fee_remark;
	}

	public void setFee_remark(String fee_remark) {
		this.fee_remark = fee_remark;
	}

	public String getCase_type() {
		return case_type;
	}

	public void setCase_type(String case_type) {
		this.case_type = case_type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMail_count() {
		return mail_count;
	}

	public void setMail_count(int mail_count) {
		this.mail_count = mail_count;
	}

	public Date getGather_time() {
		return gather_time;
	}

	public void setGather_time(Date gather_time) {
		this.gather_time = gather_time;
	}

	public String getGather_id() {
		return gather_id;
	}

	public void setGather_id(String gather_id) {
		this.gather_id = gather_id;
	}

	public String getCase_userid() {
		return case_userid;
	}

	public void setCase_userid(String case_userid) {
		this.case_userid = case_userid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirth_hospital() {
		return birth_hospital;
	}

	public void setBirth_hospital(String birth_hospital) {
		this.birth_hospital = birth_hospital;
	}

	public String getHouse_area() {
		return house_area;
	}

	public void setHouse_area(String house_area) {
		this.house_area = house_area;
	}

	public String getLife_area() {
		return life_area;
	}

	public void setLife_area(String life_area) {
		this.life_area = life_area;
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

	public String getCase_in_time() {
		return case_in_time;
	}

	public void setCase_in_time(String case_in_time) {
		this.case_in_time = case_in_time;
	}

	public int getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}

	public String getAgentia_id() {
		return agentia_id;
	}

	public void setAgentia_id(String agentia_id) {
		this.agentia_id = agentia_id;
	}

	public String getAgentia_name() {
		return agentia_name;
	}

	public void setAgentia_name(String agentia_name) {
		this.agentia_name = agentia_name;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getMail_area() {
		return mail_area;
	}

	public void setMail_area(String mail_area) {
		this.mail_area = mail_area;
	}

	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}

	public String getTariff_name() {
		return tariff_name;
	}

	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}

	public String getMail_name() {
		return mail_name;
	}

	public void setMail_name(String mail_name) {
		this.mail_name = mail_name;
	}

	public String getMail_code() {
		return mail_code;
	}

	public void setMail_code(String mail_code) {
		this.mail_code = mail_code;
	}

	public String getMail_time() {
		return mail_time;
	}

	public void setMail_time(String mail_time) {
		this.mail_time = mail_time;
	}

}
