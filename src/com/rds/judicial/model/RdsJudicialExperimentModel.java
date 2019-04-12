package com.rds.judicial.model;

import lombok.Data;

import java.util.Date;

/**
 * @author linys
 * @ClassName: RdsJudicialExperimentModel
 * @Description: 试验管理映射类
 * @date 2015年4月7日 上午10:28:24
 */
@Data
public class RdsJudicialExperimentModel {
    //UUID
    private String uuid;
    //实验编号
    private String experiment_no;
    //实验日期
    private String experiment_date;
    //实验执行人
    private String experimenter;
    //实验是否成功，Y，N
    private char enable_flag;
    //样本位置
    private String places;
    //试剂名称
    private String reagent_name;
}
