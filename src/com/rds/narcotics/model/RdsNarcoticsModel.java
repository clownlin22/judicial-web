package com.rds.narcotics.model;

import lombok.Data;

/**
 * @Author: lxy
 * @Date: 2019/3/20 11:21
 */
@Data
public class RdsNarcoticsModel {

    private String case_id;
    private Integer case_num;//鉴定号
    private String client;//委托人
    private String client_time;//受理日期
    private String person_name;//被鉴定人

    private Integer person_sex;//被鉴定人性别['男', 0], ['女', 1]
    private String person_card;//被鉴定人身份证
    private Integer identification_materials;//鉴定材料 ['血液', 0],['尿液', 1], ['头发', 2],['腋毛', 3],['阴毛', 4]
    private String identification_start;//鉴定时间
    private String identification_end;//鉴定时间
    private String report_time;//报告时间

    private Integer identification_site;//鉴定地点['江苏苏博检测技术有限公司司法鉴定所', 0]
    private String case_card;//样品编号
    private String case_basic;//基本案情
    private String instrument_type;//仪器型号
    private Integer entrusted_matters;//委托事项['苯丙胺类成分分析', 0], ['阿片类成分分析', 1]

    private Integer materials_totals;//材料总长
    private String materials_color;//材料颜色
    private Integer materials_length;//材料距根部长度
    private Integer materials_weight;//材料重量
    private Integer partial_weight;//取样重量
    private String liquid_color;//液体颜色
    private Integer ifleakage;//有无泄漏['有', 0], ['无', 1]

    private String appraisal_opinion;//鉴定意见
    private String case_in_per;//登记人
    private String username;//登记人
    private String case_in_time;//登记时间

    private String identification_per;//鉴定人
    private Integer state;



}
