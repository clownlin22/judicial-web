package com.rds.judicial.model;

public class RdsJudicialVerifyCaseInfoModel {

	private String case_id;
	private String case_code;
	private String case_areacode;
	private String case_userid;
	private String client;
	private String receiver_area;
	private String case_receiver;
	private String receiver_id;
	private int urgent_state = 0;
	private int print_copies = 0;
	private int check_state = 0;
	private String check_remark;
	private int print_count=0;
	private String gather_id;
	private String accept_time;
	private String consignment_time;
	private String close_time;
	private String report_model;
	private String report_modelname;
	private String case_in_per;
	private String case_in_pername;
	private String sample_in_time;
	private String sample_in_per;
	private String remark;
	private int is_delete =0;
    private String verify_baseinfo_time;
    private String verify_baseinfo_person;
    private int verify_baseinfo_state;
    private String verify_baseinfo_remark;
    private String verify_sampleinfo_time;
    private String verify_sampleinfo_person;
    private int verify_sampleinfo_state;
    private String verify_sampleinfo_remark;
    private String phone;
    private String province;
    private String city;
    private String county;
    private int verify_state = 0;
    //案例状态
    private int case_state;
    //案例来源
    private String source_type;
    //单位类型
    private String unit_type;

    private int copies;
    
    private int sample_relation;
    
    private String typeid;
    private String purpose;//鉴定目的
    public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	private String parnter_name;
    
    private String confirm_code;
    private String serial_number;
	public String getSerial_number() {
		return serial_number;
	}
	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	private String process_instance_id;
	private String task_id;
	private String task_def_key;
	private String task_name;
	private int suspension_state;
	private String last_task_id;
	/*0代表没有意见，大于0代表有意见*/
	private int has_comment;

	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getCase_id() {
		return case_id;
	}
	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}
	public String getCase_code() {
		return case_code;
	}
	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}
	public String getCase_userid() {
		return case_userid;
	}
	public void setCase_userid(String case_userid) {
		this.case_userid = case_userid;
	}
	public String getCase_areacode() {
		return case_areacode;
	}
	public void setCase_areacode(String case_areacode) {
		this.case_areacode = case_areacode;
	}
	public String getReceiver_area() {
		return receiver_area;
	}
	public void setReceiver_area(String receiver_area) {
		this.receiver_area = receiver_area;
	}
	public String getCase_receiver() {
		return case_receiver;
	}
	public void setCase_receiver(String case_receiver) {
		this.case_receiver = case_receiver;
	}
	public String getReceiver_id() {
		return receiver_id;
	}
	public void setReceiver_id(String receiver_id) {
		this.receiver_id = receiver_id;
	}
	public int getUrgent_state() {
		return urgent_state;
	}
	public void setUrgent_state(int urgent_state) {
		this.urgent_state = urgent_state;
	}
	public int getPrint_copies() {
		return print_copies;
	}
	public void setPrint_copies(int print_copies) {
		this.print_copies = print_copies;
	}
	public int getCheck_state() {
		return check_state;
	}
	public void setCheck_state(int check_state) {
		this.check_state = check_state;
	}
	public String getCheck_remark() {
		return check_remark;
	}
	public void setCheck_remark(String check_remark) {
		this.check_remark = check_remark;
	}
	public String getSource_type() {
		return source_type;
	}
	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}
	public String getParnter_name() {
		return parnter_name;
	}
	public void setParnter_name(String parnter_name) {
		this.parnter_name = parnter_name;
	}
	public String getConfirm_code() {
		return confirm_code;
	}
	public void setConfirm_code(String confirm_code) {
		this.confirm_code = confirm_code;
	}
	public int getPrint_count() {
		return print_count;
	}
	public void setPrint_count(int print_count) {
		this.print_count = print_count;
	}
	public String getGather_id() {
		return gather_id;
	}
	public void setGather_id(String gather_id) {
		this.gather_id = gather_id;
	}
	public String getAccept_time() {
		return accept_time;
	}
	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}
	public String getConsignment_time() {
		return consignment_time;
	}
	public void setConsignment_time(String consignment_time) {
		this.consignment_time = consignment_time;
	}
	public String getClose_time() {
		return close_time;
	}
	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}
	public String getUnit_type() {
		return unit_type;
	}
	public void setUnit_type(String unit_type) {
		this.unit_type = unit_type;
	}
	public String getReport_model() {
		return report_model;
	}
	public void setReport_model(String report_model) {
		this.report_model = report_model;
	}
	public String getReport_modelname() {
		return report_modelname;
	}
	public void setReport_modelname(String report_modelname) {
		this.report_modelname = report_modelname;
	}
	public String getCase_in_per() {
		return case_in_per;
	}
	public void setCase_in_per(String case_in_per) {
		this.case_in_per = case_in_per;
	}
	public String getCase_in_pername() {
		return case_in_pername;
	}
	public void setCase_in_pername(String case_in_pername) {
		this.case_in_pername = case_in_pername;
	}
	public String getSample_in_time() {
		return sample_in_time;
	}
	public void setSample_in_time(String sample_in_time) {
		this.sample_in_time = sample_in_time;
	}
	public String getSample_in_per() {
		return sample_in_per;
	}
	public void setSample_in_per(String sample_in_per) {
		this.sample_in_per = sample_in_per;
	}
	public int getCopies() {
		return copies;
	}
	public void setCopies(int copies) {
		this.copies = copies;
	}
	public int getSample_relation() {
		return sample_relation;
	}
	public void setSample_relation(int sample_relation) {
		this.sample_relation = sample_relation;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}
	public String getVerify_baseinfo_time() {
		return verify_baseinfo_time;
	}
	public void setVerify_baseinfo_time(String verify_baseinfo_time) {
		this.verify_baseinfo_time = verify_baseinfo_time;
	}
	public String getVerify_baseinfo_person() {
		return verify_baseinfo_person;
	}
	public void setVerify_baseinfo_person(String verify_baseinfo_person) {
		this.verify_baseinfo_person = verify_baseinfo_person;
	}
	public int getVerify_baseinfo_state() {
		return verify_baseinfo_state;
	}
	public void setVerify_baseinfo_state(int verify_baseinfo_state) {
		this.verify_baseinfo_state = verify_baseinfo_state;
	}
	public String getVerify_baseinfo_remark() {
		return verify_baseinfo_remark;
	}
	public void setVerify_baseinfo_remark(String verify_baseinfo_remark) {
		this.verify_baseinfo_remark = verify_baseinfo_remark;
	}
	public String getVerify_sampleinfo_time() {
		return verify_sampleinfo_time;
	}
	public void setVerify_sampleinfo_time(String verify_sampleinfo_time) {
		this.verify_sampleinfo_time = verify_sampleinfo_time;
	}
	public String getVerify_sampleinfo_person() {
		return verify_sampleinfo_person;
	}
	public void setVerify_sampleinfo_person(String verify_sampleinfo_person) {
		this.verify_sampleinfo_person = verify_sampleinfo_person;
	}
	public int getVerify_sampleinfo_state() {
		return verify_sampleinfo_state;
	}
	public void setVerify_sampleinfo_state(int verify_sampleinfo_state) {
		this.verify_sampleinfo_state = verify_sampleinfo_state;
	}

	public String getVerify_sampleinfo_remark() {
		return verify_sampleinfo_remark;
	}
	public void setVerify_sampleinfo_remark(String verify_sampleinfo_remark) {
		this.verify_sampleinfo_remark = verify_sampleinfo_remark;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public int getVerify_state() {
		return verify_state;
	}
	public void setVerify_state(int verify_state) {
		this.verify_state = verify_state;
	}
	public int getCase_state() {
		return case_state;
	}
	public void setCase_state(int case_state) {
		this.case_state = case_state;
	}
	public String getProcess_instance_id() {
		return process_instance_id;
	}

	public void setProcess_instance_id(String process_instance_id) {
		this.process_instance_id = process_instance_id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getTask_def_key() {
		return task_def_key;
	}

	public void setTask_def_key(String task_def_key) {
		this.task_def_key = task_def_key;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public int getSuspension_state() {
		return suspension_state;
	}

	public void setSuspension_state(int suspension_state) {
		this.suspension_state = suspension_state;
	}

	public String getLast_task_id() {
		return last_task_id;
	}

	public void setLast_task_id(String last_task_id) {
		this.last_task_id = last_task_id;
	}

	public int getHas_comment() {
		return has_comment;
	}

	public void setHas_comment(int has_comment) {
		this.has_comment = has_comment;
	}
}
