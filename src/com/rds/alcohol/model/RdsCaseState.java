package com.rds.alcohol.model;

public enum RdsCaseState {
	REGISTER("未审核", 0), CHECK_NO("审核未通过", 1), ON_EXPERIMENT("已审核，可以打印", 2), ON_PRINT(
			"已审核，可以打印", 3), ON_MAIL("已打印，可以邮寄", 4), ON_ARCHIVE("已邮寄，可以归档", 5), ARCHIVE(
			"已归档", 6), DELETE("已删除", 7);

	private String name;
	private int index;

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	private RdsCaseState(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getState(int index){
		RdsCaseState[] states=RdsCaseState.values();
		for(RdsCaseState state:states){
			if(state.index==index){
				return state.name;
			}
		}
		return "";
	}
}
