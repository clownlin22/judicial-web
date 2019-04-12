package com.rds.statistics.model;

import java.util.ArrayList;
import java.util.List;

public class RdsStatisticsResponse {
	private List<RdsStatisticsBaseModel> totalModels = new ArrayList<RdsStatisticsBaseModel>();
    private List<String> ownPersons=new ArrayList<String>();
    private List<RdsStatisticsTypeModel> types=new ArrayList<RdsStatisticsTypeModel>();
    
	public List<String> getOwnPersons() {
		return ownPersons;
	}

	public void setOwnPersons(List<String> ownPersons) {
		this.ownPersons = ownPersons;
	}

	public List<RdsStatisticsTypeModel> getTypes() {
		return types;
	}

	public void setTypes(List<RdsStatisticsTypeModel> types) {
		this.types = types;
	}

	public List<RdsStatisticsBaseModel> getTotalModels() {
		return totalModels;
	}

	public void setTotalModels(List<RdsStatisticsBaseModel> totalModels) {
		this.totalModels = totalModels;
	}
	
	
}
