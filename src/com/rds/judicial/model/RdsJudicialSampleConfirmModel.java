package com.rds.judicial.model;

public class RdsJudicialSampleConfirmModel {

	private String confirm_id;
	private String confirm_per;
	private String confirm_time;
	private String confirm_remark;
	private String confirm_pername;
	private String relay_id;

	public String getConfirm_id() {
		return confirm_id;
	}

	public String getRelay_id() {
		return relay_id;
	}

	public void setRelay_id(String relay_id) {
		this.relay_id = relay_id;
	}

	public void setConfirm_id(String confirm_id) {
		this.confirm_id = confirm_id;
	}

	public String getConfirm_pername() {
		return confirm_pername;
	}

	public void setConfirm_pername(String confirm_pername) {
		this.confirm_pername = confirm_pername;
	}

	public String getConfirm_per() {
		return confirm_per;
	}

	public void setConfirm_per(String confirm_per) {
		this.confirm_per = confirm_per;
	}

	public String getConfirm_time() {
		return confirm_time;
	}

	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}

	public String getConfirm_remark() {
		return confirm_remark;
	}

	public void setConfirm_remark(String confirm_remark) {
		this.confirm_remark = confirm_remark;
	}

	public RdsJudicialSampleConfirmModel(String confirm_id, String confirm_per,
			String relay_id, String confirm_remark) {
		super();
		this.relay_id = relay_id;
		this.confirm_id = confirm_id;
		this.confirm_per = confirm_per;
		this.confirm_remark = confirm_remark;
	}

	public RdsJudicialSampleConfirmModel() {
		super();
		// TODO Auto-generated constructor stub
	}
}
