package com.rds.judicial.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/28
 */
@Data
public class RdsJudicialPiInfoModel {
    private String sub_case_code;
    private String param_type;
    private String parent;
    private String parent2;
    private String child;
    private String gene1;
    private String gene2;
    private String function;
    private String pi;
}
