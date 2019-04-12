package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsExperimentalReportModel {
  private String id;
  private String subject;//任务名
  private String flag;//标识，区分实验室
  private String date;
 
  private String uuid;//附件编号
  private String upload_username;//上传人员
  private String upload_date;//上传时间
  private String report;//报告时间
  private int times;//下载次数
 
  
}
