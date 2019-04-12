package com.rds.statistics.model;

import java.text.DecimalFormat;


public class RdsFinancePersonAmboeaModel {
    private String username;
    private String usertype;
    private String webchart;
    private String telphone;
    private String return_sum;
    private String mx1bxje;
    private String wages;
    private String dept1;
    private String dept2;
    private String dept3;
    private String dept4;
    private String dept5;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getWebchart() {
		return webchart;
	}
	public void setWebchart(String webchart) {
		this.webchart = webchart;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getReturn_sum() {
		return return_sum;
	}
	public void setReturn_sum(String return_sum) {
		this.return_sum = return_sum;
	}
	public String getMx1bxje() {
		return mx1bxje;
	}
	public void setMx1bxje(String mx1bxje) {
		this.mx1bxje = mx1bxje;
	}
	public String getWages() {
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置
		if("".equals(wages)||null == wages)
			return "0";
		else
		return decimalFormat.format(Double.parseDouble(wages));
	}
	public void setWages(String wages) {
		this.wages = wages;
	}
	public String getDept1() {
		return dept1;
	}
	public void setDept1(String dept1) {
		this.dept1 = dept1;
	}
	public String getDept2() {
		return dept2;
	}
	public void setDept2(String dept2) {
		this.dept2 = dept2;
	}
	public String getDept3() {
		return dept3;
	}
	public void setDept3(String dept3) {
		this.dept3 = dept3;
	}
	public String getDept4() {
		return dept4;
	}
	public void setDept4(String dept4) {
		this.dept4 = dept4;
	}
	public String getDept5() {
		return dept5;
	}
	public void setDept5(String dept5) {
		this.dept5 = dept5;
	}
    
}
