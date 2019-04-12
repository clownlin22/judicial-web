package com.rds.children.model;


public class RdsChildrenPrintCaseModel {

	private String case_id;
	private String case_code;
	private String sample_code;
	private String child_name;
	private int child_sex = 0;
	private String birth_date;
	private String id_number;
	private String house_area;
	private String life_area;
	private String father_name;
	private String father_number;
	private String father_phone;
	private String mother_name;
	private String mother_number;
	private String mother_phone;
	private String print_time;
	private String case_username;
	private String case_areaname;
	private String gather_id;
	private String verify_state;
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
	public String getChildsex() {
		return (child_sex==1)?"男":"女";
	}
	public String getBirth_date() {
		return birth_date;
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

	public String getFather_name() {
		return father_name;
	}

	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}

	public String getFather_number() {
		return father_number;
	}

	public void setFather_number(String father_number) {
		this.father_number = father_number;
	}

	public String getFather_phone() {
		return father_phone;
	}

	public void setFather_phone(String father_phone) {
		this.father_phone = father_phone;
	}

	public String getMother_name() {
		return mother_name;
	}

	public void setMother_name(String mother_name) {
		this.mother_name = mother_name;
	}

	public String getMother_number() {
		return mother_number;
	}

	public void setMother_number(String mother_number) {
		this.mother_number = mother_number;
	}

	public String getMother_phone() {
		return mother_phone;
	}

	public void setMother_phone(String mother_phone) {
		this.mother_phone = mother_phone;
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

	public String getGather_id() {
		return gather_id;
	}

	public void setGather_id(String gather_id) {
		this.gather_id = gather_id;
	}

	public String getVerify_state() {
		return verify_state;
	}

	public void setVerify_state(String verify_state) {
		this.verify_state = verify_state;
	}

}
