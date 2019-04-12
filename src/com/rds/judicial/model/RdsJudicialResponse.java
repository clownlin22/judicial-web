package com.rds.judicial.model;

import java.util.List;

import lombok.Data;
@Data
public class RdsJudicialResponse {
	private long count;//数据长度
    private List items;//数据条目
}
