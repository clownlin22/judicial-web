package com.rds.judicial.model;

import java.util.ArrayList;
import java.util.List;

public class RdsJudicialPrintResultModel {
	private String result_type;
	private RdsJudicialPrintSampleModel sampleModel = new RdsJudicialPrintSampleModel();
	private List<RdsJudicialPrintCaseResultModel> results=new ArrayList<RdsJudicialPrintCaseResultModel>();
	private RdsJudicialPrintCaseResultModel result=new RdsJudicialPrintCaseResultModel();
	private RdsJudicialCaseAttachmentModel attachmentModel = new RdsJudicialCaseAttachmentModel();
	
	public String getResult_type() {
		return result_type;
	}

	public void setResult_type(String result_type) {
		this.result_type = result_type;
	}

	public RdsJudicialCaseAttachmentModel getAttachmentModel() {
		return attachmentModel;
	}

	public void setAttachmentModel(RdsJudicialCaseAttachmentModel attachmentModel) {
		this.attachmentModel = attachmentModel;
	}

	public List<RdsJudicialPrintCaseResultModel> getResults() {
		return results;
	}

	public void setResults(List<RdsJudicialPrintCaseResultModel> results) {
		this.results = results;
	}

	public RdsJudicialPrintCaseResultModel getResult() {
		return result;
	}

	public void setResult(RdsJudicialPrintCaseResultModel result) {
		this.result = result;
	}

	public RdsJudicialPrintSampleModel getSampleModel() {
		return sampleModel;
	}

	public void setSampleModel(RdsJudicialPrintSampleModel sampleModel) {
		this.sampleModel = sampleModel;
	}

	@Override
	public String toString() {
		return "RdsJudicialPrintResultModel [sampleModel=" + sampleModel
				+ ", results=" + results + ", result=" + result
				+ ", attachmentModel=" + attachmentModel + "]";
	}
	
}
