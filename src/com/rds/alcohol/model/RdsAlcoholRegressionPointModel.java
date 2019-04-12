package com.rds.alcohol.model;

import lombok.Data;
/**
 * 校验曲线点
 * @author 傅少明
 * 2015年6月10日
 *
 */
@Data
public class RdsAlcoholRegressionPointModel {
	private String reg_id;
	/**
	 * 浓度
	 */
	private double concentration;
	/**
	 * 酒精
	 */
    private double alcohol;
    /**
     * 叔丁醇
     */
    private double butanol;
    
    public RdsAlcoholRegressionPointModel(String reg_id,double concentration,double alcohol,double butanol){
    	this.alcohol = alcohol;
    	this.butanol = butanol;
    	this.concentration = concentration;
    	this.reg_id = reg_id;
    }
}
