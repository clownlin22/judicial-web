package com.rds.children.model;

public class RdsChildrenAgentiaLocusModel {
	private String locus_name;
	private String agentia_id;
	private String agentia_name;
	private Integer order;
	
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getAgentia_name() {
		return agentia_name;
	}
	public void setAgentia_name(String agentia_name) {
		this.agentia_name = agentia_name;
	}
	public String getLocus_name() {
		return locus_name;
	}
	public void setLocus_name(String locus_name) {
		this.locus_name = locus_name;
	}
	public String getAgentia_id() {
		return agentia_id;
	}
	public void setAgentia_id(String agentia_id) {
		this.agentia_id = agentia_id;
	}
	
}
