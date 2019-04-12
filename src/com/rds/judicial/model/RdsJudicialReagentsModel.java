package com.rds.judicial.model;

import lombok.Data;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/9/7
 */
@Data
public class RdsJudicialReagentsModel {
    private String reagent_name;
    private String laboratory_no;
    private int line_number;
    private String enable_flag;
}
