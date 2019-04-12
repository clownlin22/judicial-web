package com.rds.alcohol.model;

import lombok.Data;

@Data
public class RdsAlcoholQueryParam {
    private String case_code;
    private int state=-1;
    private String starttime;
    private String endtime;
}
