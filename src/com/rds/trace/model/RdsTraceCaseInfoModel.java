package com.rds.trace.model;

import lombok.Data;

@Data
public class RdsTraceCaseInfoModel {
    //主键
    private String case_id;
    //案例测序号
    private int case_no;
    //受理日期年
    private String year;
    //受理日期月
    private String month;
    //受理日期日
    private String day;
    //委托单位
    private String company_name;
    //委托鉴定事项
    private String case_type;
    //鉴定材料
    private String case_attachment_desc;
    //鉴定要求
    private String identification_requirements;
    //鉴定地点
    private String case_local;
    //受理时间
    private String receive_time;
    //状态 -2基本信息审核不通过 0基本信息未审核 1基本信息审核通过 2报告已编写 3报告审核不通过 4报告审核通过
    private int status;
    //归属人
    private String receiver_id;
    //是否删除 0未废除 1已废除
    private int is_delete;
    //鉴定日期
    private String identification_date;

    public static int NO_WORD_UPLOAD=0;
    public static int WORD_UPLOAD=1;
    public static int VERIFY_PASSED=2;
    public static int VERIFY_REFUSED=3;
    public static int PRINTED=4;
    public static int DELIVERED=5;
    public static int ARCHIVED=6;
}
