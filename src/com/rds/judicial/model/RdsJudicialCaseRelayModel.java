package com.rds.judicial.model;

public class RdsJudicialCaseRelayModel {

	private String relay_id;
	private String relay_time;
	private String relay_code;
	private String relay_per;
	private String relay_remark;
	private String relay_pername;
	private int is_delete=0;
	private String confirm_time;
	private String confirm_pername;
	private String confirm_remark;
	private int confirm_state=0;
	private String relay_check;//报告核对人员1
	private String relay_checktwo;//报告核对人员2
	public String getRelay_checktwo() {
		return relay_checktwo;
	}

	public void setRelay_checktwo(String relay_checktwo) {
		this.relay_checktwo = relay_checktwo;
	}

	private String relay_Gluing;//胶装人员
	private String relay_Seal;//盖章人员
	private String relay_split;//拆分人员

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

	public int getConfirm_state() {
		return confirm_state;
	}

	public void setConfirm_state(int confirm_state) {
		this.confirm_state = confirm_state;
	}

	public int getIs_delete() {
		return is_delete;
	}

	public String getConfirm_pername() {
		return confirm_pername;
	}

	public void setConfirm_pername(String confirm_pername) {
		this.confirm_pername = confirm_pername;
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

	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}

	public String getRelay_pername() {
		return relay_pername;
	}

	public void setRelay_pername(String relay_pername) {
		this.relay_pername = relay_pername;
	}

	public RdsJudicialCaseRelayModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RdsJudicialCaseRelayModel(String relay_id, String relay_code,
			String relay_per, String relay_remark,String relay_check,String relay_checktwo,String  relay_Gluing,String relay_Seal,String relay_split) {
		super();
		this.relay_id = relay_id;
		this.relay_code = relay_code;
		this.relay_per = relay_per;
		this.relay_remark = relay_remark;
		this.relay_check = relay_check;
		this.relay_Gluing = relay_Gluing;
		this.relay_Seal = relay_Seal;
		this.relay_split = relay_split;
	}

	public RdsJudicialCaseRelayModel(String relay_id, String relay_remark) {
		super();
		this.relay_id = relay_id;
		this.relay_remark = relay_remark;
	}

	public String getRelay_id() {
		return relay_id;
	}

	public void setRelay_id(String relay_id) {
		this.relay_id = relay_id;
	}

	public String getRelay_time() {
		return relay_time;
	}

	public void setRelay_time(String relay_time) {
		this.relay_time = relay_time;
	}

	public String getRelay_code() {
		return relay_code;
	}

	public void setRelay_code(String relay_code) {
		this.relay_code = relay_code;
	}

	public String getRelay_per() {
		return relay_per;
	}

	public void setRelay_per(String relay_per) {
		this.relay_per = relay_per;
	}

	public String getRelay_remark() {
		return relay_remark;
	}

	public void setRelay_remark(String relay_remark) {
		this.relay_remark = relay_remark;
	}

}
