package com.rds.alcohol.model;

import java.math.BigDecimal;
import java.text.ParseException;

import com.rds.code.date.DateUtils;

public class RdsAlcoholPrintInfoModel {
	private double reg_A;
	private double reg_B;
	private String expression;
	private double reg_R2;
	private double result_1;
	private double result_2;
	private double avg_result;
	private String exper_time;
	private String exper_id;

	public String getExpression() {
		if (reg_B > 0) {
			return "Y=" + getDecimal(reg_A, 4) + "X+" + getDecimal(reg_B, 4);
		}
		return "Y=" +getDecimal(reg_A, 4) + "X" + getDecimal(reg_B, 4);
	}

	public double getReg_A() {
		return reg_A;
	}

	public void setReg_A(double reg_A) {
		this.reg_A = reg_A;
	}

	public double getReg_B() {
		return reg_B;
	}

	public void setReg_B(double reg_B) {
		this.reg_B = reg_B;
	}

	public double getReg_R2() {
		return getDecimal(reg_R2, 5);
	}

	public void setReg_R2(double reg_R2) {
		this.reg_R2 = reg_R2;
	}

	public String getExper_id() {
		return exper_id;
	}

	public void setExper_id(String exper_id) {
		this.exper_id = exper_id;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public double getResult_1() {
		return getDecimal(result_1, 6);
	}

	public void setResult_1(double result_1) {
		this.result_1 = result_1;
	}

	public double getResult_2() {
		return getDecimal(result_2, 6);
	}

	public void setResult_2(double result_2) {
		this.result_2 = result_2;
	}

	public double getAvg_result() {
		return getDecimal((result_1 + result_2) / 2, 6);
	}

	public void setAvg_result(double avg_result) {
		this.avg_result = avg_result;
	}

	public String getExper_time() {
		try {
			return DateUtils.zhformat.format(DateUtils.lineformat
					.parse(exper_time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setExper_time(String exper_time) {
		this.exper_time = exper_time;
	}

	public double getDecimal(double d,int size) {
		BigDecimal b = new BigDecimal(d);
		double dou = b.setScale(size, BigDecimal.ROUND_HALF_UP).doubleValue();
		return dou;
	}

}
