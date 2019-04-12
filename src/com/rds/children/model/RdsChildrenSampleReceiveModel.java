package com.rds.children.model;

import java.sql.Date;

public class RdsChildrenSampleReceiveModel {
	private String id;
	private String receive_id;
	private String transfer_num;
	private Date transfer_time;
	private String transfer_pername;
	private int state;
	private String remark;
	private String receive_pername;
	private Date receive_time;
	private String receive_remark;
	private int confirm_state;
	private String sample_code;
	public String getReceive_id() {
		return receive_id;
	}
	public void setReceive_id(String receive_id) {
		this.receive_id = receive_id;
	}
	public String getTransfer_num() {
		return transfer_num;
	}
	public void setTransfer_num(String transfer_num) {
		this.transfer_num = transfer_num;
	}
	public String getReceive_pername() {
		return receive_pername;
	}
	public void setReceive_pername(String receive_pername) {
		this.receive_pername = receive_pername;
	}
	public String getReceive_remark() {
		return receive_remark;
	}
	public void setReceive_remark(String receive_remark) {
		this.receive_remark = receive_remark;
	}
	public String getTransfer_pername() {
		return transfer_pername;
	}
	public void setTransfer_pername(String transfer_pername) {
		this.transfer_pername = transfer_pername;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getRevice_pername() {
		return receive_pername;
	}
	public void setRevice_pername(String receive_pername) {
		this.receive_pername = receive_pername;
	}
	public String getRevice_remark() {
		return receive_remark;
	}
	public void setRevice_remark(String receive_remark) {
		this.receive_remark = receive_remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getConfirm_state() {
		return confirm_state;
	}
	public void setConfirm_state(int confirm_state) {
		this.confirm_state = confirm_state;
	}
	public String getSample_code() {
		return sample_code;
	}
	public void setSample_code(String sample_code) {
		this.sample_code = sample_code;
	}
	public Date getTransfer_time() {
		return transfer_time;
	}
	public void setTransfer_time(Date transfer_time) {
		this.transfer_time = transfer_time;
	}
	public Date getReceive_time() {
		return receive_time;
	}
	public void setReceive_time(Date receive_time) {
		this.receive_time = receive_time;
	}
}
