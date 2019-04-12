package com.rds.judicial.model;

public class RdsJudicialSampleRelayModel {

	private String relay_id;
	private String relay_per;
	private String relay_code;
	private String relay_time;
	private String relay_remark;
	private String relay_pername;
	private int is_delete=0;
	private int confirm_state=0;
	private String confirm_remark;
	private String confirm_time;
	private String confirm_pername;
	private String relay_time_str;
	private String now_time;

	public String getConfirm_remark() {
		return confirm_remark;
	}

	public void setConfirm_remark(String confirm_remark) {
		this.confirm_remark = confirm_remark;
	}

	public String getConfirm_time() {
		return confirm_time;
	}

	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}

	public String getConfirm_pername() {
		return confirm_pername;
	}

	public void setConfirm_pername(String confirm_pername) {
		this.confirm_pername = confirm_pername;
	}

	public String getRelay_time_str() {
		return relay_time_str;
	}

	public void setRelay_time_str(String relay_time_str) {
		this.relay_time_str = relay_time_str;
	}

	public String getNow_time() {
		return now_time;
	}

	public void setNow_time(String now_time) {
		this.now_time = now_time;
	}

	public int getConfirm_state() {
		return confirm_state;
	}

	public void setConfirm_state(int confirm_state) {
		this.confirm_state = confirm_state;
	}

	public String getRelay_id() {
		return relay_id;
	}

	public void setRelay_id(String relay_id) {
		this.relay_id = relay_id;
	}

	public String getRelay_per() {
		return relay_per;
	}

	public void setRelay_per(String relay_per) {
		this.relay_per = relay_per;
	}

	public String getRelay_time() {
		return relay_time;
	}

	public void setRelay_time(String relay_time) {
		this.relay_time = relay_time;
	}

	public String getRelay_remark() {
		return relay_remark;
	}

	public void setRelay_remark(String relay_remark) {
		this.relay_remark = relay_remark;
	}

	public String getRelay_pername() {
		return relay_pername;
	}

	public void setRelay_pername(String relay_pername) {
		this.relay_pername = relay_pername;
	}

	public int getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}

	public String getRelay_code() {
		return relay_code;
	}

	public void setRelay_code(String relay_code) {
		this.relay_code = relay_code;
	}

	public RdsJudicialSampleRelayModel(String relay_id, String relay_per,
			String relay_code, String relay_remark) {
		super();
		this.relay_id = relay_id;
		this.relay_per = relay_per;
		this.relay_code = relay_code;
		this.relay_remark = relay_remark;
	}

	public RdsJudicialSampleRelayModel() {
		super();
	}
	public RdsJudicialSampleRelayModel(String relay_id, String relay_remark) {
		super();
		this.relay_id = relay_id;
		this.relay_remark = relay_remark;
	}
}
