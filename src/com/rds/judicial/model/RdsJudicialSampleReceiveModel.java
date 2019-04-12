package com.rds.judicial.model;

public class RdsJudicialSampleReceiveModel {

	private String receive_id;
	private String receive_per;
	private String receive_time;
	private String receive_remark;
	private String receive_pername;
	private String relay_code;
	private String relay_id;
	private int is_delete = 0;
	private int use_state = 0;

	public int getUse_state() {
		return use_state;
	}

	public void setUse_state(int use_state) {
		this.use_state = use_state;
	}

	public int getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}

	public String getReceive_id() {
		return receive_id;
	}

	public void setReceive_id(String receive_id) {
		this.receive_id = receive_id;
	}

	public String getReceive_per() {
		return receive_per;
	}

	public void setReceive_per(String receive_per) {
		this.receive_per = receive_per;
	}

	public String getReceive_time() {
		return receive_time;
	}

	public void setReceive_time(String receive_time) {
		this.receive_time = receive_time;
	}

	public String getRelay_code() {
		return relay_code;
	}

	public void setRelay_code(String relay_code) {
		this.relay_code = relay_code;
	}

	public String getRelay_id() {
		return relay_id;
	}

	public void setRelay_id(String relay_id) {
		this.relay_id = relay_id;
	}

	public String getReceive_remark() {
		return receive_remark;
	}

	public void setReceive_remark(String receive_remark) {
		this.receive_remark = receive_remark;
	}

	public String getReceive_pername() {
		return receive_pername;
	}

	public void setReceive_pername(String receive_pername) {
		this.receive_pername = receive_pername;
	}

	public RdsJudicialSampleReceiveModel(String receive_id, String receive_per,
			String receive_remark) {
		super();
		this.receive_id = receive_id;
		this.receive_per = receive_per;
		this.receive_remark = receive_remark;
	}

	public RdsJudicialSampleReceiveModel() {
		super();
	}

	public RdsJudicialSampleReceiveModel(String receive_id,
			String receive_remark) {
		super();
		this.receive_id = receive_id;
		this.receive_remark = receive_remark;
	}
}
