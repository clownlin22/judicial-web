package com.rds.crm.model;


/**
 * crm订单pojo
 * @author 少明
 *
 */
public class RdsCrmOrderModel {
	private String orderId;
	private String orderInPer;
	private String phone;
	private String client;
	private Integer orderType;
	private String address;
	private Integer consultCount;
	private String remark;
	private Integer status;
	private Integer isArchive;
	private String orderInDate;
	private Integer isPostpaid;
	private Double standFee;
	private String detectionClass;
	private String detectionOrg;
	private Integer isExtendOrder;
	private String detectionClassName;
	private String detectionOrgName;
	private int isRefund;
	private Double paidFee;
	private String sampleInfo;
	
	public String getSampleInfo() {
		return sampleInfo;
	}
	public void setSampleInfo(String sampleInfo) {
		this.sampleInfo = sampleInfo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderInPer() {
		return orderInPer;
	}
	public void setOrderInPer(String orderInPer) {
		this.orderInPer = orderInPer;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getConsultCount() {
		return consultCount;
	}
	public void setConsultCount(Integer consultCount) {
		this.consultCount = consultCount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIsArchive() {
		return isArchive;
	}
	public void setIsArchive(Integer isArchive) {
		this.isArchive = isArchive;
	}
	public String getOrderInDate() {
		return orderInDate;
	}
	public void setOrderInDate(String orderInDate) {
		this.orderInDate = orderInDate;
	}
	public Integer getIsPostpaid() {
		return isPostpaid;
	}
	public void setIsPostpaid(Integer isPostpaid) {
		this.isPostpaid = isPostpaid;
	}
	public Double getStandFee() {
		return standFee;
	}
	public void setStandFee(Double standFee) {
		this.standFee = standFee;
	}
	public String getDetectionClass() {
		return detectionClass;
	}
	public void setDetectionClass(String detectionClass) {
		this.detectionClass = detectionClass;
	}
	public String getDetectionOrg() {
		return detectionOrg;
	}
	public void setDetectionOrg(String detectionOrg) {
		this.detectionOrg = detectionOrg;
	}
	public Integer getIsExtendOrder() {
		return isExtendOrder;
	}
	public void setIsExtendOrder(Integer isExtendOrder) {
		this.isExtendOrder = isExtendOrder;
	}
	public String getDetectionClassName() {
		return detectionClassName;
	}
	public void setDetectionClassName(String detectionClassName) {
		this.detectionClassName = detectionClassName;
	}
	public String getDetectionOrgName() {
		return detectionOrgName;
	}
	public void setDetectionOrgName(String detectionOrgName) {
		this.detectionOrgName = detectionOrgName;
	}
	public int getIsRefund() {
		return 0;
	}
	public Double getPaidFee() {
		return paidFee;
	}
	public void setPaidFee(Double paidFee) {
		this.paidFee = paidFee;
	}
}
