package com.rds.judicial.model;

public class RdsJudicialRelaySampleInfo {
	private String sample_id;
	private String sample_code;
	private int confirm_state = 0;
	private String relay_id;

	public String getSample_code() {
		return sample_code;
	}

	public void setSample_code(String sample_code) {
		this.sample_code = sample_code;
	}

	public String getSample_id() {
		return sample_id;
	}

	public void setSample_id(String sample_id) {
		this.sample_id = sample_id;
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

	public RdsJudicialRelaySampleInfo(String sample_id, String relay_id) {
		super();
		this.sample_id = sample_id;
		this.relay_id = relay_id;
	}

	public RdsJudicialRelaySampleInfo(String sample_id, String sample_code,
			String relay_id) {
		super();
		this.sample_id = sample_id;
		this.sample_code = sample_code;
		this.relay_id = relay_id;
	}

	public RdsJudicialRelaySampleInfo() {
		super();
	}

}
