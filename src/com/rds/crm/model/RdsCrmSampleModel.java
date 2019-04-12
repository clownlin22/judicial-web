package com.rds.crm.model;

import lombok.Data;

@Data
public class RdsCrmSampleModel {
	private String sampleId;
	private String sampleName;
	private String orderId;
	
	public RdsCrmSampleModel(String sampleId) {
		super();
		this.sampleId = sampleId;
	}

	public RdsCrmSampleModel() {
	}
}
