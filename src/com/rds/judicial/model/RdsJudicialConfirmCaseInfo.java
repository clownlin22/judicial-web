package com.rds.judicial.model;

public class RdsJudicialConfirmCaseInfo {

	private String case_id;
	private int confirm_state =0;
	private String relay_id;
	private String case_code;
	private String relay_code;
	private String relay_time;
	private String relay_remark;
	private String relay_check;
	private String relay_checktwo;
	public String getRelay_checktwo() {
		return relay_checktwo;
	}

	public void setRelay_checktwo(String relay_checktwo) {
		this.relay_checktwo = relay_checktwo;
	}

	private String relay_Gluing;
	private String relay_Seal;
	private String relay_split;
    public String getRelay_check() {
		return relay_check;
	}

	public void setRelay_check(String relay_check) {
		this.relay_check = relay_check;
	}

	public String getRelay_Gluing() {
		return relay_Gluing;
	}

	public void setRelay_Gluing(String relay_Gluing) {
		this.relay_Gluing = relay_Gluing;
	}

	public String getRelay_Seal() {
		return relay_Seal;
	}

	public void setRelay_Seal(String relay_Seal) {
		this.relay_Seal = relay_Seal;
	}

	public String getRelay_split() {
		return relay_split;
	}

	public void setRelay_split(String relay_split) {
		this.relay_split = relay_split;
	}

	private boolean selected=false;
    
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getCase_code() {
		return case_code;
	}

	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}

	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
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

	public String getRelay_code() {
		return relay_code;
	}

	public void setRelay_code(String relay_code) {
		this.relay_code = relay_code;
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

	public RdsJudicialConfirmCaseInfo() {
		super();
	}

	public RdsJudicialConfirmCaseInfo(String case_id, String relay_id) {
		super();
		this.case_id = case_id;
		this.relay_id = relay_id;
	}
}
