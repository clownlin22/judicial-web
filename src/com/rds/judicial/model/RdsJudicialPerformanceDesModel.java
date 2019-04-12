package com.rds.judicial.model;

import lombok.Data;


public class RdsJudicialPerformanceDesModel {
	private String desc;
	private int count;
	
	public RdsJudicialPerformanceDesModel(String desc,int count){
		this.desc=desc;
		this.count=count;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
