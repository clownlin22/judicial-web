package com.rds.alcohol.model;

import lombok.Data;

@Data
public class RdsAlcoholExperimentDataModel {
	private String id;
	private String exper_id;
	private Double alcohol;
	private Double butanol;
	private Double result;
}
