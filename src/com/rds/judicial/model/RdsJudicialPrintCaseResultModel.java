package com.rds.judicial.model;

import java.text.DecimalFormat;

public class RdsJudicialPrintCaseResultModel {
	private String pi;
    private String rcp;
	private double pi_double;
	private String pi_str;
	private int pi_num=1;
	private RdsJudicialCompareResultModel compareResultModel = new RdsJudicialCompareResultModel();
	
	
	public RdsJudicialCompareResultModel getCompareResultModel() {
		return compareResultModel;
	}

	public void setCompareResultModel(
			RdsJudicialCompareResultModel compareResultModel) {
		this.compareResultModel = compareResultModel;
	}

	public String getPi_str() {
		DecimalFormat df = new DecimalFormat("0.00");
		return  df.format(pi_double/Math.pow(10,getPi_num()));
	}

	public void setPi_str(String pi_str) {
		this.pi_str = pi_str;
	}

	public double getPi_double() {
		return pi_double;
	}

	public void setPi_double(double pi_double) {
		this.pi_double = pi_double;
	}
	
	public int getPi_num() {
		String[] num=String.valueOf(new DecimalFormat("0.00E0").format(pi_double)).split("E");
		if(num.length>1){
			return Integer.valueOf(num[1]);
		}
		return 1;
	}
	public void setPi_num(int pi_num) {
		this.pi_num = pi_num;
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
}
