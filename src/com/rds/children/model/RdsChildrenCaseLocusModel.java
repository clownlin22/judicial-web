package com.rds.children.model;

public class RdsChildrenCaseLocusModel {
	private String case_id;
	private String locus_name;
	private String locus_value;
	private String locus_value1;
	private String locus_value2;
	
	public String getLocus_value1() {
		return locus_value1;
	}
	public void setLocus_value1(String locus_value1) {
		this.locus_value1 = locus_value1;
	}
	public String getLocus_value2() {
		return locus_value2;
	}
	public void setLocus_value2(String locus_value2) {
		this.locus_value2 = locus_value2;
	}
	public String getCase_id() {
		return case_id;
	}
	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}
	public String getLocus_name() {
		return locus_name;
	}
	public void setLocus_name(String locus_name) {
		this.locus_name = locus_name;
	}
	public String getLocus_value() {
		return locus_value;
	}
	public void setLocus_value(String locus_value) {
		this.locus_value = locus_value;
	}
	public RdsChildrenCaseLocusModel(){
		
	}
	public RdsChildrenCaseLocusModel(String case_id,String locus_name,String locus_value){
		this.case_id = case_id;
		this.locus_name = locus_name;
		this.locus_value = locus_value;
	}
}
