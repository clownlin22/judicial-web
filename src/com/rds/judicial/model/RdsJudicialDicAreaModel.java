package com.rds.judicial.model;

public class RdsJudicialDicAreaModel {
	private String id;
	private String text;
	private String parentId;
	private boolean checked = false;
	private boolean leaf = true;
	private String type;

	// // private Set<RdsJudicialDicAreaModel> children = new TreeSet<>();
	//
	// @Override
	// public boolean equals(Object obj){
	// if(obj instanceof RdsJudicialDicAreaModel)
	// return this.id.equals(((RdsJudicialDicAreaModel) obj).getId());
	// else return false;
	// }
	//
	// @Override
	// public int compareTo(Object o) {
	// return
	// Integer.parseInt(this.id)-(Integer.parseInt(((RdsJudicialDicAreaModel)o).getId()));
	// }
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		if (null != type) {
			return (type.contains("1") && type.contains("0") ? text+"-"+id + "(亲子/医学)"
					: (type.contains("1") ? text +"-"+id + "(医学)"
							: (type.contains("0")) ? text+"-"+id + "(亲子)" : (type
									.contains("2") ? text+"-"+id + "(法医临床)" : (type
									.contains("3") ? text+"-"+id + "(酒精检测)" : (type
									.contains("4") ? text+"-"+id + "(儿童基因库)" : (type
									.contains("5") ? text+"-"+id + "(痕迹鉴定)" : text+"-"+id))))));
		} else
			return text+"-"+id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
