package com.rds.children.model;

import java.util.List;

import lombok.Data;

@Data
public class RdsChildrenResponse {
	private long count;//数据长度
    private List items;//数据条目
	public RdsChildrenResponse(long count, List items) {
		super();
		this.count = count;
		this.items = items;
	}
	public RdsChildrenResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
}
