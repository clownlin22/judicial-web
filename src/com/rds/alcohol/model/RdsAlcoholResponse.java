package com.rds.alcohol.model;

import lombok.Data;

import java.util.List;

@Data
public class RdsAlcoholResponse   {
	private long count;//数据长度
    private List items;//数据条目
}
