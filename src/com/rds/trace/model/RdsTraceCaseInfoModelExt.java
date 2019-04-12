package com.rds.trace.model;


/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/23
 */
public class RdsTraceCaseInfoModelExt extends RdsTraceCaseInfoModel{
    //审核状态 0未审核 1已审核 2审核不通过
    private int verify_baseinfo_state;

    //案例归属地
    private String areaname;

    //归属人姓名
    private String username;

    private String stand_sum;
    
    //应收款项
    private String real_sum;

    //所付款项
    private String return_sum;
    
    //发票编号
    private String invoice_number;

    //到款时间
    private String paragraphtime;

    //备注
    private String remark;
    
    //邮寄日期
    private String mail_info;
    
    //延期原因
    private String dely_reson;
    
    private String finance_status;

	public int getVerify_baseinfo_state() {
		return verify_baseinfo_state;
	}

	public void setVerify_baseinfo_state(int verify_baseinfo_state) {
		this.verify_baseinfo_state = verify_baseinfo_state;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStand_sum() {
		return stand_sum;
	}

	public void setStand_sum(String stand_sum) {
		this.stand_sum = stand_sum;
	}

	public String getReal_sum() {
		return real_sum;
	}

	public void setReal_sum(String real_sum) {
		this.real_sum = real_sum;
	}

	public String getReturn_sum() {
		return return_sum;
	}

	public void setReturn_sum(String return_sum) {
		this.return_sum = return_sum;
	}

	public String getParagraphtime() {
		return paragraphtime;
	}

	public void setParagraphtime(String paragraphtime) {
		this.paragraphtime = paragraphtime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMail_info() {
		return mail_info;
	}

	public void setMail_info(String mail_info) {
		this.mail_info = mail_info;
	}

	public String getDely_reson() {
		return dely_reson;
	}

	public void setDely_reson(String dely_reson) {
		this.dely_reson = dely_reson;
	}

	public String getFinance_status() {
		return finance_status;
	}

	public void setFinance_status(String finance_status) {
		this.finance_status = finance_status;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

}
