package com.rds.judicial.model;

public class AlgReturnValueModel {
	
	//步长
	private double step;
	//可能性
	private String possibility;
	
	private String function;
	
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public double getStep() {
		return step;
	}
	public void setStep(double step) {
		this.step = step;
	}
	public String getPossibility() {
		return possibility;
	}
	public void setPossibility(String possibility) {
		this.possibility = possibility;
	}
	}