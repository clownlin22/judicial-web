package com.rds.judicial.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class RdsJudicialCaseInfoModel {
	//案例费用id
	private String id;
	//案例id
	private String case_id;
	//案例编号
	private String case_code;
	//案例归属地
	private String case_areacode;
	//案例归属人
	private String case_userid;
	//单双亲
	private String typeid;
	private int case_state;
	//打印份数
	private int copies;
	private int attach_need_case;
	private String agent;
	private String receiver_area;
	private String case_receiver;
	private int urgent_state = 0;
	private int print_copies = 0;
	private String address;
	private String phone;
	private int verify_state = 0;
	private int print_count = 0;
	private int gather_status=0;
	private String remittance_id;
	private String accept_time;
	private String consignment_time;
	//优惠码
	private String confirm_code;
	
	//新增字段 打印时间 同受理时间
	private String addextnew_time;
	//打印时间
	private String close_time;
	//案例模版
	private String report_model;
	//案例模版名称
	private String report_modelname;
	//采样人员
	private String case_in_per;
	//采样人员名字
	private String case_in_pername;
	
	private String parnter_name;
	private String sample_in_time;
	//案例来源
	private String source_type;
	//备注
	private String remark;
	private String report_url;
	private String report_chart;
	//作废标记
	private int is_delete = 0;
	private String county;
	private String city;
	private String province;
	private String achieve;
	private String achieve_phone;
	private int mail_count = 0;
	private int charge_amount=0;
	private int attach_need = 0;
	private int fee_type=0;
	//委托人
	private String client;
	private int is_new = 0;
	private String sample_in_per;
	private String unit_type;
    private String laboratory_no;
    private String compare_date;
    private int case_type =0;
    private int sample_relation=0;
    private Double stand_sum;
    private Double return_sum;
    private Double real_sum;
    private String fee_date;
	private String fee_remark;
    private String mail_info;
    private String photo_state;
    private String attachment_date;
    private String attachment_paht;
    private String purpose ;//鉴定目的
    private String iswilltime;//即将超时
    private String isintime;//正常
    private String isouttime;//超时
    private String time1;
    private String time2;
    private String confirm_date;

	private String process_instance_id;
	private String task_id;
	private String task_def_key;
	private String task_name;
	private int suspension_state;
	private String last_task_id;
	/*0代表没有意见，大于0代表有意见*/
	private int has_comment;
	  private String reagent_name;
	    private String reagent_name_ext;
	    
		private String receiver_id;
	    
	public RdsJudicialCaseInfoModel(String case_id, String case_code,
			String case_areacode, int urgent_state, String phone,
			String accept_time, String case_in_per, String report_model,
			String receiver_id) {
		super();
		this.case_id = case_id;
		this.case_code = case_code;
		this.case_areacode = case_areacode;
		this.urgent_state = urgent_state;
		this.phone = phone;
		this.accept_time = accept_time;
		this.case_in_per = case_in_per;
		this.report_model = report_model;
		this.receiver_id = receiver_id;
	}

	public RdsJudicialCaseInfoModel(String case_id, String case_code,
			String case_areacode, String receiver_id, int urgent_state,
			String phone, String report_model,String remark, String client,String sample_in_per,String unit_type,int case_type,int sample_relation) {
		super();
		this.case_id = case_id;
		this.case_code = case_code;
		this.case_areacode = case_areacode;
		this.urgent_state = urgent_state;
		this.phone = phone;
		this.report_model = report_model;
		this.remark = remark;
		this.client = client;
		this.sample_in_per=sample_in_per;
		this.unit_type=unit_type;
		this.case_type=case_type;
		this.sample_relation=sample_relation;
		this.receiver_id = receiver_id;
	}

	public RdsJudicialCaseInfoModel(String case_id, String case_code,String typeid,
			String case_areacode, String case_userid,String receiver_id, int urgent_state,
			String phone, String accept_time, String report_model,
			String case_in_per, String remark, String client,
			String sample_in_per, String unit_type, int case_type,
			int sample_relation) {
		super();
		this.case_id = case_id;
		this.case_code = case_code;
		this.case_areacode = case_areacode;
		this.urgent_state = urgent_state;
		this.phone = phone;
		this.accept_time = accept_time;
		this.report_model = report_model;
		this.case_in_per = case_in_per;
		this.remark = remark;
		this.client = client;
		this.sample_in_per = sample_in_per;
		this.unit_type = unit_type;
		this.case_type = case_type;
		this.sample_relation = sample_relation;
		this.receiver_id = receiver_id;
	}
    public String getLaboratory_no() {
        return laboratory_no;
    }

    public void setLaboratory_no(String laboratory_no){
        this.laboratory_no = laboratory_no;
    }
    public String getReceiver_id() {
		return receiver_id;
	}

	public void setReceiver_id(String receiver_id) {
		this.receiver_id = receiver_id;
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

	public String getAddress() {
		return address == null ? "" : this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPrint_count() {
		return print_count;
	}

	public void setPrint_count(int print_count) {
		this.print_count = print_count;
	}

	public String getAccept_time() {
		return accept_time;
	}

	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}

	public String getAddextnew_time() {
		return addextnew_time;
	}

	public void setAddextnew_time(String addextnew_time) {
		this.addextnew_time = addextnew_time;
	}
	
	public String getClose_time() {
		return close_time;
	}

	public void setClose_time(String close_time) {
		this.close_time = close_time;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReport_url() {
		return report_url;
	}

	public void setReport_url(String report_url) {
		this.report_url = report_url;
	}

	public int getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(int is_delete) {
		this.is_delete = is_delete;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getAchieve() {
		return achieve == null ? "" : this.achieve;
	}

	public void setAchieve(String achieve) {
		this.achieve = achieve;
	}

	public String getAchieve_phone() {
		return achieve_phone == null ? "" : this.achieve_phone;
	}

	public void setAchieve_phone(String achieve_phone) {
		this.achieve_phone = achieve_phone;
	}

	public int getAttach_need() {
		if(attach_need==0){
			return 0;
		}else{
			if(attach_need_case==0){
				return 0;
			}
		}
		return 1;
	}

	public void setAttach_need(int attach_need) {
		this.attach_need = attach_need;
	}

	public int getMail_count() {
		return mail_count;
	}

	public void setMail_count(int mail_count) {
		this.mail_count = mail_count;
	}

	public String getClient() {
		return client == null ? "" : this.client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public int getIs_new() {
		return is_new;
	}

	public void setIs_new(int is_new) {
		this.is_new = is_new;
	}

	public int getVerify_state() {
		return verify_state;
	}

	public void setVerify_state(int verify_state) {
		this.verify_state = verify_state;
	}

	public String getReport_chart() {
		return report_chart;
	}

	public void setReport_chart(String report_chart) {
		this.report_chart = report_chart;
	}

    public String getCompare_date(){
        return this.compare_date;
    }

    public void setCompare_date(String compare_date){
        this.compare_date = compare_date;
    }

	public String getSample_in_per() {
		return sample_in_per;
	}

	public void setSample_in_per(String sample_in_per) {
		this.sample_in_per = sample_in_per;
	}

	public String getUnit_type() {
		return unit_type;
	}

	public void setUnit_type(String unit_type) {
		this.unit_type = unit_type;
	}

	public int getCase_type() {
		return case_type;
	}

	public void setCase_type(int case_type) {
		this.case_type = case_type;
	}

	public int getSample_relation() {
		return sample_relation;
	}

	public void setSample_relation(int sample_relation) {
		this.sample_relation = sample_relation;
	}

	public int getGather_status() {
		return gather_status;
	}

	public void setGather_status(int gather_status) {
		this.gather_status = gather_status;
	}

	public int getFee_type() {
		return fee_type;
	}

	public void setFee_type(int fee_type) {
		this.fee_type = fee_type;
	}

	public Double getStand_sum() {
		return stand_sum;
	}

	public void setStand_sum(Double stand_sum) {
		this.stand_sum = stand_sum;
	}

	public Double getReturn_sum() {
		return return_sum;
	}

	public void setReturn_sum(Double return_sum) {
		this.return_sum = return_sum;
	}

	public Double getReal_sum() {
		return real_sum;
	}

	public void setReal_sum(Double real_sum) {
		this.real_sum = real_sum;
	}

	public String getFee_date() {
		return fee_date;
	}

	public void setFee_date(String fee_date) {
		this.fee_date = fee_date;
	}

	public String getMail_info() {
		return mail_info;
	}

	public void setMail_info(String mail_info) {
		this.mail_info = mail_info;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getFee_remark() {
		return fee_remark;
	}

	public void setFee_remark(String fee_remark) {
		this.fee_remark = fee_remark;
	}

	public int getCharge_amount() {
		return charge_amount;
	}

	public void setCharge_amount(int charge_amount) {
		this.charge_amount = charge_amount;
	}

	public int getAttach_need_case() {
		return attach_need_case;
	}

	public void setAttach_need_case(int attach_need_case) {
		this.attach_need_case = attach_need_case;
	}

	public String getPhoto_state() {
		return photo_state;
	}

	public void setPhoto_state(String photo_state) {
		this.photo_state = photo_state;
	}

	public String getAttachment_date() {
		return attachment_date;
	}

	public void setAttachment_date(String attachment_date) {
		this.attachment_date = attachment_date;
	}

	public String getAttachment_paht() {
		return attachment_paht;
	}

	public void setAttachment_paht(String attachment_paht) {
		this.attachment_paht = attachment_paht;
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

	public String getCase_userid() {
		return case_userid;
	}

	public void setCase_userid(String case_userid) {
		this.case_userid = case_userid;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}

	public String getParnter_name() {
		return parnter_name;
	}

	public void setParnter_name(String parnter_name) {
		this.parnter_name = parnter_name;
	}

	public int getCase_state() {
		return case_state;
	}

	public void setCase_state(int case_state) {
		this.case_state = case_state;
	}

	public String getConfirm_code() {
		return confirm_code;
	}

	public void setConfirm_code(String confirm_code) {
		this.confirm_code = confirm_code;
	}

	public String getSource_type() {
		return source_type;
	}

	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}

	public String getReagent_name() {
		return reagent_name;
	}

	public void setReagent_name(String reagent_name) {
		this.reagent_name = reagent_name;
	}

	public String getReagent_name_ext() {
		return reagent_name_ext;
	}

	public void setReagent_name_ext(String reagent_name_ext) {
		this.reagent_name_ext = reagent_name_ext;
	}

	public String getConsignment_time() {
		return consignment_time;
	}

	public void setConsignment_time(String consignment_time) {
		this.consignment_time = consignment_time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getRemittance_id() {
		return remittance_id;
	}

	public void setRemittance_id(String remittance_id) {
		this.remittance_id = remittance_id;
	}

	public String getIswilltime() {
		return iswilltime;
	}

	public void setIswilltime(String iswilltime) {
		this.iswilltime = iswilltime;
	}

	public String getIsintime() {
		return isintime;
	}

	public void setIsintime(String isintime) {
		this.isintime = isintime;
	}

	public String getIsouttime() {
		return isouttime;
	}

	public void setIsouttime(String isouttime) {
		this.isouttime = isouttime;
	}

	public String getTime1() {
		return time1;
	}

	public void setTime1(String time1) {
		this.time1 = time1;
	}

	public String getTime2() {
		return time2;
	}

	public void setTime2(String time2) {
		this.time2 = time2;
	}

	public String getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}
}
