package com.rds.judicial.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class RdsJudicialPrintSampleModel {
	
	private RdsJudicialSampleInfoModel father_info =new RdsJudicialSampleInfoModel();
	private RdsJudicialSampleInfoModel mother_info =new RdsJudicialSampleInfoModel();
	private RdsJudicialSampleInfoModel child_info =new RdsJudicialSampleInfoModel();
	private List<RdsJudicialSampleInfoModel> childs_info=new ArrayList<RdsJudicialSampleInfoModel>();
	private List<Map<String,String>> sampleResults=new ArrayList<Map<String,String>>();
	
	public RdsJudicialSampleInfoModel getFather_info() {
		return father_info;
	}
	public void setFather_info(RdsJudicialSampleInfoModel father_info) {
		this.father_info = father_info;
	}
	public RdsJudicialSampleInfoModel getMother_info() {
		return mother_info;
	}
	public void setMother_info(RdsJudicialSampleInfoModel mother_info) {
		this.mother_info = mother_info;
	}
	public RdsJudicialSampleInfoModel getChild_info() {
		return child_info;
	}
	public void setChild_info(RdsJudicialSampleInfoModel child_info) {
		this.child_info = child_info;
	}
	public List<RdsJudicialSampleInfoModel> getChilds_info() {
		return childs_info;
	}
	public void setChilds_info(List<RdsJudicialSampleInfoModel> childs_info) {
		this.childs_info = childs_info;
	}
	public List<Map<String, String>> getSampleResults() {
		return sampleResults;
	}
	public void setSampleResults(List<Map<String, String>> sampleResults) {
		this.sampleResults = sampleResults;
	}
	
	@Override
	public String toString() {
		return "RdsJudicialPrintSampleModel [father_info=" + father_info
				+ ", mother_info=" + mother_info + ", child_info=" + child_info
				+ ", childs_info=" + childs_info + ", sampleResults="
				+ sampleResults + "]";
	}
	
	
}
