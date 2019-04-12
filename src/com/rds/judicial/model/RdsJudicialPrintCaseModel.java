package com.rds.judicial.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.rds.code.date.DateUtils;

public class RdsJudicialPrintCaseModel {
	private String case_id;
	private String case_code;
	private String accept_time;
	private String purpose;
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	private String addextnew_time;
	
	private String close_time;
	private String close_time_china;
	private String client;
	private List<RdsJudicialPrintResultModel> results=new ArrayList<RdsJudicialPrintResultModel>();

	public String getCase_id() {
		return case_id;
	}

	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public List<RdsJudicialPrintResultModel> getResults() {
		return results;
	}

	public void setResults(List<RdsJudicialPrintResultModel> results) {
		this.results = results;
	}

	public String getCase_code() {
		return case_code;
	}

	public void setCase_code(String case_code) {
		this.case_code = case_code;
	}

	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}
	public void setAddextnew_time(String addextnew_time) {
		this.addextnew_time = addextnew_time;
	}
	

	public String getClose_time() {
		if (StringUtils.isNotEmpty(close_time)) {
			try {
				return DateUtils.formatzh2.format(DateUtils.lineformat
						.parse(close_time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return DateUtils.formatzh2.format(new Date());
	}

	public String getClose_time_china() {
		if (StringUtils.isNotEmpty(close_time)) {
			return com.rds.code.utils.StringUtils.dateToChinese(close_time);
		}
		return com.rds.code.utils.StringUtils
				.dateToChinese(DateUtils.lineformat.format(new Date()));
	}

	public void setClose_time_china(String close_time_china) {
		this.close_time_china = close_time_china;
	}

	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}
	
	public String getAddextnew_time() {

		if (StringUtils.isNotEmpty(addextnew_time)) {
			try {
				return DateUtils.formatzh2.format(DateUtils.lineformat
						.parse(addextnew_time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		
		return "";
	}

	public String getAccept_time() {
		if (StringUtils.isNotEmpty(accept_time)) {
			try {
				return DateUtils.formatzh2.format(DateUtils.lineformat
						.parse(accept_time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		
		return "";
	}

	@Override
	public String toString() {
		return "RdsJudicialPrintCaseModel [case_id=" + case_id + ", case_code="
				+ case_code + ", accept_time=" + accept_time + ",addextnew_time=" + addextnew_time + ", close_time="
				+ close_time + ", close_time_china=" + close_time_china
				+ ", client=" + client + ", results=" + results + "]";
	}

	

	 
	
	
}
