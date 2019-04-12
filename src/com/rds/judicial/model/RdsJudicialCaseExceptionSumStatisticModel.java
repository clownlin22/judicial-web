package com.rds.judicial.model;


public class RdsJudicialCaseExceptionSumStatisticModel {
	private int days;
	// 已超期未快递的案例数
	private int no_mail = 0;
	// 未快递的案例数
	private int no_mail_all= 0;
	// 已超期数据未上传的案例数
	private int no_data= 0;
	// 已超期照片未上传的案例数
	private int no_photo= 0;
	// 已超期异常的案例数
	private int other_exception= 0;

	// 已超期未回款的案例数
	private int no_fee= 0;
	//已超期数据上传照片未上传未回款的案例数
	private int data_nophoto_nofee= 0;
	//已超期数据上传照片上传未回款的案例数
    private int data_photo_nofee= 0;
    //已超期数据上传照片未上传已回款的案例数
    private int data_nophoto_fee= 0;
    //已超期数据未上传照片已上传未回款的案例数
    private int nodata_photo_nofee= 0;
    //已超期数据未上传照片已上传已回款的案例数
    private int nodata_photo_fee= 0;
    //已超期数据未上传照片未上传已回款的案例数
    private int nodata_nophoto_fee= 0;
    //已超期数据未上传照片未上传未回款的案例数
    private int nodata_nophoto_nofee= 0;
    //已超期异常的案例数
    private int case_exception= 0;
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getNo_mail() {
		return no_mail;
	}
	public void setNo_mail(int no_mail) {
		this.no_mail = no_mail;
	}
	public int getNo_mail_all() {
		return no_mail_all;
	}
	public void setNo_mail_all(int no_mail_all) {
		this.no_mail_all = no_mail_all;
	}
	public int getNo_data() {
		return no_data;
	}
	public void setNo_data(int no_data) {
		this.no_data = no_data;
	}
	public int getNo_photo() {
		return no_photo;
	}
	public void setNo_photo(int no_photo) {
		this.no_photo = no_photo;
	}
	public int getOther_exception() {
		return other_exception;
	}
	public void setOther_exception(int other_exception) {
		this.other_exception = other_exception;
	}
	public int getNo_fee() {
		return no_fee;
	}
	public void setNo_fee(int no_fee) {
		this.no_fee = no_fee;
	}
	public int getData_nophoto_nofee() {
		return data_nophoto_nofee;
	}
	public void setData_nophoto_nofee(int data_nophoto_nofee) {
		this.data_nophoto_nofee = data_nophoto_nofee;
	}
	public int getData_photo_nofee() {
		return data_photo_nofee;
	}
	public void setData_photo_nofee(int data_photo_nofee) {
		this.data_photo_nofee = data_photo_nofee;
	}
	public int getData_nophoto_fee() {
		return data_nophoto_fee;
	}
	public void setData_nophoto_fee(int data_nophoto_fee) {
		this.data_nophoto_fee = data_nophoto_fee;
	}
	public int getNodata_photo_nofee() {
		return nodata_photo_nofee;
	}
	public void setNodata_photo_nofee(int nodata_photo_nofee) {
		this.nodata_photo_nofee = nodata_photo_nofee;
	}
	public int getNodata_photo_fee() {
		return nodata_photo_fee;
	}
	public void setNodata_photo_fee(int nodata_photo_fee) {
		this.nodata_photo_fee = nodata_photo_fee;
	}
	public int getNodata_nophoto_fee() {
		return nodata_nophoto_fee;
	}
	public void setNodata_nophoto_fee(int nodata_nophoto_fee) {
		this.nodata_nophoto_fee = nodata_nophoto_fee;
	}
	public int getNodata_nophoto_nofee() {
		return nodata_nophoto_nofee;
	}
	public void setNodata_nophoto_nofee(int nodata_nophoto_nofee) {
		this.nodata_nophoto_nofee = nodata_nophoto_nofee;
	}
	public int getCase_exception() {
		return case_exception;
	}
	public void setCase_exception(int case_exception) {
		this.case_exception = case_exception;
	}
    
    
}
