package com.rds.judicial.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class RdsJudicialPrintTableModel {
	private String model_type;
	private String sub_case_code;
	private String case_code;
	private String result;
	private String sample_code1;
	private String sample_code2;
	private String sample_code3;
	private String pi;
	private String rcp;
	private double pi_double;
	private String pi_str;
	private int pi_num = 1;
	private List<RdsJudicialPiInfoModel> piInfoModels = new ArrayList<RdsJudicialPiInfoModel>();

	public int getPi_num() {
		String[] num = String.valueOf(
				new DecimalFormat("0.00E0").format(pi_double)).split("E");
		if (num.length > 1) {
			return Integer.valueOf(num[1]);
		}
		return 1;
	}
	
	public String getSample_code1() {
		return sample_code1;
	}

	public void setSample_code1(String sample_code1) {
		this.sample_code1 = sample_code1;
	}

	public String getSample_code2() {
		return sample_code2;
	}

	public void setSample_code2(String sample_code2) {
		this.sample_code2 = sample_code2;
	}

	public String getSample_code3() {
		return sample_code3;
	}

	public void setSample_code3(String sample_code3) {
		this.sample_code3 = sample_code3;
	}

	public void setPi_num(int pi_num) {
		this.pi_num = pi_num;
	}

	public String getPi_str() {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(pi_double / Math.pow(10, getPi_num()));
	}

	public void setPi_str(String pi_str) {
		this.pi_str = pi_str;
	}

	public String getModel_type() {
		return model_type;
	}

	public void setModel_type(String model_type) {
		this.model_type = model_type;
	}

	public String getSub_case_code() {
		return sub_case_code;
	}

	public void setSub_case_code(String sub_case_code) {
		this.sub_case_code = sub_case_code;
	}

	public String getCase_code() {
		return case_code;
	}

	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getPi() {
		return pi;
	}

	public void setPi(String pi) {
		this.pi = pi;
	}

	public String getRcp() {
		return rcp;
	}

	public void setRcp(String rcp) {
		this.rcp = rcp;
	}

	public double getPi_double() {
		return pi_double;
	}

	public void setPi_double(double pi_double) {
		this.pi_double = pi_double;
	}

	public List<RdsJudicialPiInfoModel> getPiInfoModels() {
		return piInfoModels;
	}

	public void setPiInfoModels(List<RdsJudicialPiInfoModel> piInfoModels) {
		this.piInfoModels = piInfoModels;
	}
}
