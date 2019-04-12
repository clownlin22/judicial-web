package com.rds.children.model;
/**
 * 套餐
 * @author 少明
 *
 */
public class RdsChildrenTariffModel {
	private String tariff_id;
	private String tariff_name;
	private Double tariff_price;
	private String tariff_remark;
	private Integer tariff_state;
	
	public Integer getTariff_state() {
		return tariff_state;
	}
	public void setTariff_state(Integer tariff_state) {
		this.tariff_state = tariff_state;
	}
	public String getTariff_id() {
		return tariff_id;
	}
	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}
	public String getTariff_name() {
		return tariff_name;
	}
	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}
	public Double getTariff_price() {
		return tariff_price;
	}
	public void setTariff_price(Double tariff_price) {
		this.tariff_price = tariff_price;
	}
	public String getTariff_remark() {
		return tariff_remark;
	}
	public void setTariff_remark(String tariff_remark) {
		this.tariff_remark = tariff_remark;
	}
	
}
