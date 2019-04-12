package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsFinanceOAModel {
    private String case_id;
    //流程编号
    private String djbh;
    private String id;
    private String sqr;
    //申请人姓名
    private String sqrxm;
    private String tdr;
    //提单人姓名
    private String tdrxm;
    private String ssbm;
    //所属部门
    private String ssbmmc;
    private String ssgs;
    //所属公司
    private String ssgsmc;
    //申请日期
    private String sqrq;
    //金额
    private String mx1bxje;
    //报销说明
    private String bxsm;
    private String mx1fykm;
    //科目名称
    private String kmmc;
    private String mx1cdzt;
    private String ztmc1;
    private String mx1cdlx;
    
    private String ztejmc;
    //事业部名称
    private String ztsybmc;
    //操作日期
    private String operatedate;
    //操作时间
    private String operatetime;
    private String type;
    //申请人工号
    private String sqrworkcode;
    //提单人工号
    private String tdrworkcode;
    //财务出纳意见
    private String cwcnyj;
    //财务出纳操作情况
    private String isremark;
    private String user_dept_level1;
    private String user_dept_level2;
    private String user_dept_level3;
    private String user_dept_level4;
    private String user_dept_level5;
    private String fycdrxm;
    private String fycdrworkcode;
}
