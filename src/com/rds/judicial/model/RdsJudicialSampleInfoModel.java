package com.rds.judicial.model;

import java.text.ParseException;

import org.apache.commons.lang.StringUtils;

import com.rds.code.date.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class RdsJudicialSampleInfoModel {
	//样本id
	private String sample_id;
	//样本编号
	private String sample_code;
	//系统生成案例条码
	private String sample_code_sys;
	//样本类型
	private String sample_type;
	//类型名
	private String sample_typename;
	//称呼
	private String sample_call;//称谓
	private String sample_callname;//中文称谓
	private String sample_username;//名字
	private String id_number;
	private String birth_date;
	private String address;
	private int age;
	private String birth_date_format;
	private String birth_date_format_forqs;
	private String birth_date_format_forzx;
	private String case_id;
	//是否特殊样本
	private String special;
	
	public RdsJudicialSampleInfoModel(String sample_id, String sample_code,
			String sample_type, String sample_call, String sample_username,
			String id_number, String birth_date, String case_id) {
		super();
		this.sample_id = sample_id;
		this.sample_code = sample_code;
		this.sample_type = sample_type;
		this.sample_call = sample_call;
		this.sample_username = sample_username;
		this.id_number = id_number;
		this.birth_date = birth_date;
		this.case_id = case_id;
	}

	public String getSample_id() {
		return sample_id;
	}

	public void setSample_id(String sample_id) {
		this.sample_id = sample_id;
	}

	public String getSample_code() {
		return sample_code;
	}

	public void setSample_code(String sample_code) {
		this.sample_code = sample_code;
	}

	public String getSample_type() {
		return sample_type;
	}

	public void setSample_type(String sample_type) {
		this.sample_type = sample_type;
	}

	public String getSample_typename() {
		return sample_typename;
	}

	public void setSample_typename(String sample_typename) {
		this.sample_typename = sample_typename;
	}

	public String getSample_call() {
		return sample_call;
	}

	public void setSample_call(String sample_call) {
		this.sample_call = sample_call;
	}

	public String getSample_callname() {
		return sample_callname;
	}

	public void setSample_callname(String sample_callname) {
		this.sample_callname = sample_callname;
	}

	public String getSample_username() {
		return sample_username;
	}

	public void setSample_username(String sample_username) {
		this.sample_username = sample_username;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getBirth_date() {

		if(StringUtils.isNotEmpty(birth_date)){
			try {
				return DateUtils.lineformat.format(DateUtils.lineformat.parse(birth_date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}

	public String getBirth_date_format() {
		if(StringUtils.isNotEmpty(birth_date)){
			try {
				return DateUtils.formatzh2.format(DateUtils.lineformat.parse(birth_date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return "";
		
	}

	public void setBirth_date_format(String birth_date_format) {
		this.birth_date_format = birth_date_format;
	}

	public String getBirth_date_format_forqs() {
		if(StringUtils.isNotEmpty(birth_date)){
			try {
				return DateUtils.zhformat.format(DateUtils.lineformat.parse(birth_date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public void setBirth_date_format_forqs(String birth_date_format_forqs) {
		this.birth_date_format_forqs = birth_date_format_forqs;
	}
	
	public String getBirth_date_format_forzx() {

		if(StringUtils.isNotEmpty(birth_date)){
			try {
				return DateUtils.formatforzx.format(DateUtils.lineformat.parse(birth_date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public void setBirth_date_format_forzx(String birth_date_format_forzx) {
		this.birth_date_format_forzx = birth_date_format_forzx;
	}
	
	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getSample_code_sys() {
		return sample_code_sys;
	}

	public void setSample_code_sys(String sample_code_sys) {
		this.sample_code_sys = sample_code_sys;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	

}
