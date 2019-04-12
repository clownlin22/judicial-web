package com.rds.statistics.model;

import lombok.Data;

@Data
public class RdsExperimentalDataModel {
  private String case_code;//案例编号
  private String sample_codes;//采样编号
  private String consignment_time;//采样日期
  private String accept_time;//受理日期
  private String experiment_no;//实验编号
  private String case_id;//id号
  private String fandm;//父母亲
  private String id_card;//身份证
  private String child;
  private String birth_date;
  private String identify_per;//司法鉴定人
  private String assistant;//鉴定助理
  private String experiment_date;//实验日期
  private String compare_date;//对比日期
  private String ext_flag;//位点
  private String final_result_flag;
  private String close_time;//报告打印时间
  private String mail_time;//报告邮寄时间
  private String mail_address;//邮寄地址
  private String mail_phone;//
  private String data_photo;//数据和照片
  private String sample_count;//样本数
  
}
