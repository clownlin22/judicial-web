/*
Navicat MySQL Data Transfer

Source Server         : lin
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : judicial_version3

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2019-04-12 13:33:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_ability_department
-- ----------------------------
DROP TABLE IF EXISTS `tb_ability_department`;
CREATE TABLE `tb_ability_department` (
  `department_concatid` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `department_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '科室名称',
  `department_chargename` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '科室负责人',
  `experiment_chargename` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '实验负责人',
  `report_chargename` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '报告负责人',
  `finished_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '完成项目日期',
  `report_sendname` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '报告发送人',
  `report_concat` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '发送人联系方式',
  `report_senddate` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '发送日期',
  `report_sendinfo` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '发件信息',
  `report_reciveinfo` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '收件信息',
  `report_type` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '报告快递类型',
  `report_num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '报告快递单号',
  `report_status` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '报告快递状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_archive_read
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_archive_read`;
CREATE TABLE `tb_alcohol_archive_read` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `archive_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `read_per` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `read_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_attachment
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_attachment`;
CREATE TABLE `tb_alcohol_attachment` (
  `att_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `att_path` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '地址',
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例id',
  `att_type` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `path` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`att_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_case_archive
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_case_archive`;
CREATE TABLE `tb_alcohol_case_archive` (
  `archive_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `archive_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `archive_address` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `archive_date` date DEFAULT NULL,
  `archive_per` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `archive_path` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`archive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_case_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_case_info`;
CREATE TABLE `tb_alcohol_case_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `case_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例编号',
  `client` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '委托人',
  `checkper_phone` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '送检人电话',
  `area_code` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '地区编号',
  `event_desc` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '事件描述',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `address` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '采样地点',
  `mail_address` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '邮寄地址',
  `mail_per` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '邮寄人',
  `mail_phone` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '邮寄电话',
  `case_in_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '登记人',
  `attachment` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '附件',
  `report_model` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '报告类型',
  `sample_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT '0' COMMENT '0:登记状态，1是审核未通过，2可以实验中，3可以打印，4可以邮寄，5可以归档，6已归档,7删除',
  `close_time` date DEFAULT NULL,
  `accept_time` date DEFAULT NULL,
  `receiver_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `client_time` date DEFAULT NULL COMMENT '委托日期',
  `sample_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '样本描述',
  `isDoubleTube` int(2) DEFAULT NULL COMMENT '是否AB管 0 否 1 是',
  `sample_result` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '检验结果',
  `is_detection` int(2) DEFAULT '0' COMMENT '是否检出酒精',
  `is_detectionint` int(2) DEFAULT NULL COMMENT '是否检出酒精',
  `sample_remark2` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '样本描述2',
  `checkper` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '送检人',
  `bloodnumA` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '采血管编号A',
  `bloodnumB` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '采血管编号B',
  `case_num` int(100) DEFAULT NULL COMMENT '编号',
  `case_intr` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `case_det` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `is_check` int(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_case_info_identify
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_case_info_identify`;
CREATE TABLE `tb_alcohol_case_info_identify` (
  `cid` varchar(50) DEFAULT NULL,
  `pid` varchar(50) DEFAULT NULL,
  `id` varchar(50) NOT NULL,
  `per_time` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_alcohol_case_verify
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_case_verify`;
CREATE TABLE `tb_alcohol_case_verify` (
  `verify_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `verify_mark` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `verify_time` datetime DEFAULT NULL,
  `verify_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `verify_state` int(11) DEFAULT '0' COMMENT '1未通过，2是通过',
  PRIMARY KEY (`verify_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_experiment
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_experiment`;
CREATE TABLE `tb_alcohol_experiment` (
  `exper_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '实验ID',
  `exper_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '实验编号',
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例id',
  `reg_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '校验曲线ID',
  `exper_time` datetime NOT NULL COMMENT '实验时间',
  `exper_isdelete` int(2) NOT NULL DEFAULT '0' COMMENT '实验结果状态 0 为正常1为作废',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`exper_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_experiment_data
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_experiment_data`;
CREATE TABLE `tb_alcohol_experiment_data` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `exper_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '实验ID',
  `alcohol` double NOT NULL COMMENT '酒精值',
  `butanol` double NOT NULL COMMENT '叔丁醇值',
  `result` double NOT NULL COMMENT '计算结果',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_identify_per
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_identify_per`;
CREATE TABLE `tb_alcohol_identify_per` (
  `per_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '鉴定人id',
  `per_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '鉴定人姓名',
  `per_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '执业资格证号',
  `per_remark` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `per_sys` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `user_id` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `delstatus` int(100) DEFAULT '0',
  `user_name` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`per_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_mail_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_mail_info`;
CREATE TABLE `tb_alcohol_mail_info` (
  `mail_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `mail_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mail_time` date DEFAULT NULL,
  `mail_type` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mail_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `is_delete` int(11) DEFAULT '0',
  PRIMARY KEY (`mail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_regression
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_regression`;
CREATE TABLE `tb_alcohol_regression` (
  `reg_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '校验曲线ID',
  `reg_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '校验曲线编号',
  `reg_time` datetime NOT NULL COMMENT '校验线生成时间',
  `reg_A` double NOT NULL COMMENT '校验曲线公式参数A',
  `reg_B` double NOT NULL COMMENT '校验曲线公式参数B',
  `reg_R2` double NOT NULL COMMENT '校验曲线线性相关系数',
  `reg_qualify` int(2) NOT NULL DEFAULT '0' COMMENT '校验曲线是否合格0合格1不合格',
  PRIMARY KEY (`reg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_regression_data
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_regression_data`;
CREATE TABLE `tb_alcohol_regression_data` (
  `reg_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '校验曲线id',
  `concentration` int(10) DEFAULT NULL COMMENT '试剂浓度',
  `alcohol` double DEFAULT NULL COMMENT '酒精峰面积',
  `butanol` double DEFAULT NULL COMMENT '叔丁醇峰面积'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_alcohol_sample_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_alcohol_sample_info`;
CREATE TABLE `tb_alcohol_sample_info` (
  `sample_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `sample_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sample_sex` int(11) DEFAULT '0',
  `id_number` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sample_time` datetime DEFAULT NULL,
  `sample_ml` int(11) DEFAULT NULL,
  PRIMARY KEY (`sample_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_all_case_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_all_case_info`;
CREATE TABLE `tb_all_case_info` (
  `accept_time` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `case_code` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `real_sum` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `return_sum` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ptype` varchar(13) CHARACTER SET utf8 DEFAULT NULL,
  `rtype` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `user_dept_level1` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `user_dept_level2` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `user_dept_level3` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `receiver` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `agent` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `case_area` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `partner` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `remittanceDate` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `status` bigint(20) DEFAULT NULL,
  `sample_count` decimal(42,0) DEFAULT NULL,
  `remark` mediumtext COLLATE utf8_bin,
  `finance_remark` text COLLATE utf8_bin,
  `client` mediumtext COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_abstract_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_abstract_info`;
CREATE TABLE `tb_appraisal_abstract_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例id',
  `case_abstract` blob COMMENT '案情摘要',
  `sickness_abstract` blob COMMENT '病例摘要',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_advice_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_advice_info`;
CREATE TABLE `tb_appraisal_advice_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '鉴定意见案例id',
  `advice_text` blob COMMENT '鉴定意见',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_analysis_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_analysis_info`;
CREATE TABLE `tb_appraisal_analysis_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '分析说明案例id',
  `analysis_text` blob COMMENT '分析说明',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_attachment_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_attachment_info`;
CREATE TABLE `tb_appraisal_attachment_info` (
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例id',
  `attachment_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '附件id',
  `attachment_type` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '附件类型',
  `attachment_filename` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '附件地址',
  `creat_time` date DEFAULT NULL COMMENT '上传时间',
  `attachment_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '附件名称',
  `attachment_order` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '附件次序',
  PRIMARY KEY (`attachment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_base_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_base_info`;
CREATE TABLE `tb_appraisal_base_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '伤残鉴定基础信息表唯一id',
  `entrust_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '委托人',
  `case_number` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定号',
  `entrust_matter` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '委托事项',
  `identify_stuff` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定材料',
  `accept_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '受理日期',
  `identify_date_start` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定日期开始时间',
  `identify_place` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定地点',
  `identify_date_end` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定日期结束时间',
  `create_time` date DEFAULT NULL COMMENT '案例创建时间',
  `flag_status` int(2) DEFAULT '2' COMMENT '状态：[''全部'',0],[''未审核'',2 ],[''登记已审核'',3 ],[''已回退'',1 ],[''录入待审核'',4],[''录入审核未通过'',5],[''录入审核通过'',6]',
  `recrive_id` varbinary(50) DEFAULT NULL COMMENT '归属人',
  `entrust_num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '委托函号',
  `judgename` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定人',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_entrust_key
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_entrust_key`;
CREATE TABLE `tb_appraisal_entrust_key` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '委托人id',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '委托人姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_identify_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_identify_info`;
CREATE TABLE `tb_appraisal_identify_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '被鉴定人案例id',
  `identify_per_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `identify_per_sex` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `identify_per_both` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '出生日期',
  `identify_per_idcard` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证号',
  `identify_per_address` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '住址',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_judge
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_judge`;
CREATE TABLE `tb_appraisal_judge` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `userid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定人id',
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例id',
  `flag` int(2) DEFAULT '0' COMMENT '鉴定人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_log_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_log_info`;
CREATE TABLE `tb_appraisal_log_info` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例id',
  `time` date DEFAULT NULL COMMENT '日志生成时间',
  `nowstatus` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '当前状态',
  `prestatus` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '上一状态',
  `fquser` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '发起人',
  `jsuser` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '接收人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_mechanism
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_mechanism`;
CREATE TABLE `tb_appraisal_mechanism` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_process_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_process_info`;
CREATE TABLE `tb_appraisal_process_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '检验过程案例id',
  `process_method` blob COMMENT '检验方法',
  `process_check` blob COMMENT '体格检查',
  `process_read` blob COMMENT '阅片所见',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `read_flag` int(2) DEFAULT '0' COMMENT '0:无阅片所见，1：有阅片所见',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_relation
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_relation`;
CREATE TABLE `tb_appraisal_relation` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例id',
  `type_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '类型id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_standard_content
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_standard_content`;
CREATE TABLE `tb_appraisal_standard_content` (
  `standard_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '鉴定标准内容id',
  `content` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '标准内容',
  `series` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '级数',
  `type_id` varbinary(50) DEFAULT NULL COMMENT '所属类型',
  PRIMARY KEY (`standard_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_standard_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_standard_type`;
CREATE TABLE `tb_appraisal_standard_type` (
  `type_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '鉴定类型id',
  `standard_desc` varchar(4000) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定类型说明',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `appendix_status` int(2) DEFAULT '0' COMMENT '附录标识：0，无附录；1，有附录',
  `standard_name` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定类型名称',
  `appendix_desc` varchar(4000) COLLATE utf8_bin DEFAULT NULL COMMENT '附录说明',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_appraisal_template
-- ----------------------------
DROP TABLE IF EXISTS `tb_appraisal_template`;
CREATE TABLE `tb_appraisal_template` (
  `template_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '模版id',
  `keyword` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '关键字',
  `standard_explain` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '模版说明',
  `case_abstract` blob COMMENT '案情摘要',
  `sickness_abstract` blob COMMENT '病例摘要',
  `process_method` blob COMMENT '检验方法',
  `process_check` blob COMMENT '体格检查',
  `process_read` blob COMMENT '阅片所见',
  `read_flag` int(2) DEFAULT '0' COMMENT '0:无阅片所见，1：有阅片所见',
  `analysis_text` blob COMMENT '分析说明',
  `advice_text` blob COMMENT '鉴定意见',
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_bacera_hpv
-- ----------------------------
DROP TABLE IF EXISTS `tb_bacera_hpv`;
CREATE TABLE `tb_bacera_hpv` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案列编号',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `age` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '年龄',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '所属人',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否废除1是2否',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `date` date DEFAULT NULL COMMENT '日期',
  `gender` varchar(10) COLLATE utf8_bin DEFAULT '男' COMMENT '性别',
  `agent` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '代理商',
  `program` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '检测项目'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_bacera_program
-- ----------------------------
DROP TABLE IF EXISTS `tb_bacera_program`;
CREATE TABLE `tb_bacera_program` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `program_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目名称',
  `program_code` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目编码',
  `program_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目类型',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `is_delete` varchar(1) COLLATE utf8_bin DEFAULT '1' COMMENT '删除标志',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_baihui_attachment
-- ----------------------------
DROP TABLE IF EXISTS `tb_baihui_attachment`;
CREATE TABLE `tb_baihui_attachment` (
  `att_id` varchar(50) NOT NULL,
  `att_path` varchar(200) DEFAULT NULL,
  `case_id` varchar(50) DEFAULT NULL,
  `uploadtime` varchar(50) DEFAULT NULL,
  `is_send` int(10) DEFAULT '0',
  PRIMARY KEY (`att_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_baihui_express_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_baihui_express_info`;
CREATE TABLE `tb_baihui_express_info` (
  `num` int(50) DEFAULT NULL COMMENT '案列编号',
  `id` varchar(50) NOT NULL,
  `expresstype` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '快递类型',
  `expressnum` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '快递单号',
  `expresstime` varchar(50) DEFAULT NULL COMMENT '快递时间',
  `recive` varchar(50) DEFAULT NULL COMMENT '收件人',
  `expressremark` varchar(100) DEFAULT NULL COMMENT '备注',
  `input_person` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_baihui_finance_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_baihui_finance_info`;
CREATE TABLE `tb_baihui_finance_info` (
  `id` varchar(255) NOT NULL,
  `num` varchar(255) NOT NULL COMMENT '案列编号',
  `receivables` varchar(255) DEFAULT NULL COMMENT '应收款项',
  `payment` varchar(255) DEFAULT NULL COMMENT '所付款项',
  `paid` varchar(255) DEFAULT NULL COMMENT '实收款项',
  `paragraphtime` varchar(255) DEFAULT NULL COMMENT '到款时间',
  `case_type` varchar(255) DEFAULT NULL COMMENT '案例类型',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `account_type` varchar(255) DEFAULT NULL COMMENT '账户类型',
  `oa_num` varchar(255) DEFAULT NULL COMMENT 'OA编号',
  `remittanceName` varchar(255) DEFAULT NULL COMMENT '帐户名称',
  `remittanceDate` varchar(255) DEFAULT NULL COMMENT '汇款日期',
  `discountPrice` varchar(255) DEFAULT NULL COMMENT '优惠价格',
  `siteFee` varchar(255) DEFAULT NULL COMMENT '场地费',
  `fees` varchar(255) DEFAULT NULL COMMENT '手续费',
  `confirm_flag` varchar(255) DEFAULT '1' COMMENT '确认标志',
  `confirm_date` varchar(255) DEFAULT NULL COMMENT '确认时间',
  `input_person` varchar(255) DEFAULT NULL COMMENT '录入人',
  `confirm_per` varchar(255) DEFAULT NULL COMMENT '确认人',
  `report_model` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_baihui_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_baihui_info`;
CREATE TABLE `tb_baihui_info` (
  `case_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `report_model` int(10) DEFAULT NULL COMMENT '模板类型',
  `case_type` int(15) DEFAULT '0' COMMENT '案例类型',
  `num` varchar(100) DEFAULT NULL COMMENT '案例编号',
  `ownperson` varchar(150) DEFAULT NULL COMMENT '案例归属',
  `client_time` varchar(100) DEFAULT NULL COMMENT '日期',
  `testitems` varchar(300) DEFAULT NULL COMMENT '检测项目',
  `checkper` varchar(150) DEFAULT NULL COMMENT '送检人',
  `inspectionUnits` varchar(200) DEFAULT NULL COMMENT '送检单位',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `gender` varchar(50) DEFAULT NULL COMMENT '性别',
  `age` varchar(50) DEFAULT NULL COMMENT '年龄',
  `phonenum` varchar(100) DEFAULT NULL COMMENT '电话',
  `reportType` varchar(300) DEFAULT NULL COMMENT '报告种类',
  `orderPlatform` varchar(300) DEFAULT NULL COMMENT '接单平台',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `receivables` varchar(100) DEFAULT NULL COMMENT '应收款项',
  `diagnosis` varchar(100) DEFAULT NULL COMMENT '病理诊断',
  `barcode` varchar(100) DEFAULT NULL COMMENT '物流条码',
  `hospital` varchar(100) DEFAULT NULL COMMENT '送检医院',
  `sampletype` varchar(100) DEFAULT NULL COMMENT '样本类型',
  `samplecount` varchar(100) DEFAULT NULL COMMENT '样本数量',
  `codenum` varchar(100) DEFAULT NULL COMMENT '条形码编号',
  `fmname` varchar(100) DEFAULT NULL COMMENT '母亲/父亲姓名',
  `verify_state` int(10) DEFAULT '0' COMMENT '1未审核，2,待审核，3,审核不通过，4审核通过，5案例样本交接中,6样本确认,7,上传报告，8邮寄,9归档',
  `process_instance_id` varchar(50) DEFAULT NULL COMMENT '流程实例ID',
  `is_delete` int(10) DEFAULT '0' COMMENT '删除标识',
  `case_in_time` varchar(100) DEFAULT NULL COMMENT '案例登记时间',
  `case_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_baihui_relay_receive
-- ----------------------------
DROP TABLE IF EXISTS `tb_baihui_relay_receive`;
CREATE TABLE `tb_baihui_relay_receive` (
  `relay_id` varchar(50) NOT NULL,
  `receive_id` varchar(50) DEFAULT NULL COMMENT '接收样本id',
  `receive_per` varchar(50) DEFAULT NULL COMMENT '确认人',
  `receive_time` datetime DEFAULT NULL COMMENT '确认时间',
  `receive_remark` varchar(500) DEFAULT NULL COMMENT '确认备注',
  PRIMARY KEY (`relay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_baihui_sample_receive
-- ----------------------------
DROP TABLE IF EXISTS `tb_baihui_sample_receive`;
CREATE TABLE `tb_baihui_sample_receive` (
  `receive_id` varchar(50) NOT NULL,
  `transfer_num` varchar(20) DEFAULT NULL COMMENT '交接单号',
  `transfer_time` datetime DEFAULT NULL COMMENT '交接时间',
  `transfer_per` varchar(50) DEFAULT NULL COMMENT '交接人',
  `remark` varchar(200) DEFAULT NULL COMMENT '交接备注',
  `state` int(10) DEFAULT '1' COMMENT '状态1：未审核，2：已审核',
  PRIMARY KEY (`receive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_baihui_sample_receive_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_baihui_sample_receive_info`;
CREATE TABLE `tb_baihui_sample_receive_info` (
  `id` varchar(50) NOT NULL,
  `num` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '样本编号',
  `receive_id` varchar(50) DEFAULT NULL COMMENT '交接单id',
  `confirm_state` int(10) DEFAULT '0' COMMENT '交接确认状态，0：样本接收，1确认通过，2：确认不通过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_bioms_genetic_test
-- ----------------------------
DROP TABLE IF EXISTS `tb_bioms_genetic_test`;
CREATE TABLE `tb_bioms_genetic_test` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `bioms_genetic_test_id` int(11) DEFAULT NULL COMMENT '生信报告系统基因检测项目主键',
  `add_time` datetime NOT NULL COMMENT '添加时间',
  `consumer_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '检测用户姓名',
  `consumer_sex` varchar(10) COLLATE utf8_bin NOT NULL COMMENT '检测用户性别',
  `consumer_birthday` date DEFAULT NULL COMMENT '检测用户生日',
  `consumer_phone` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '检测用户手机',
  `sample_number` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '样本编号',
  `test_number` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '报告编号',
  `report_date` datetime DEFAULT NULL COMMENT '出报告时间',
  `test_package_id` int(11) NOT NULL COMMENT '检测套餐id',
  `test_package_name` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '检测套餐名称',
  `agency_id` int(11) NOT NULL DEFAULT '0' COMMENT '代理商id',
  `agency_name` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '代理商名称',
  `test_item_ids` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '检测项ids',
  `test_item_names` varchar(800) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '检测项名称',
  `charge_standard_id` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '归属人id',
  `charge_standard_name` varchar(200) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '归属人全称（包括归属地，归属人，代理商名称）',
  `price` int(11) DEFAULT NULL COMMENT '应收金额（分为单位）',
  `remark` varchar(2000) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='基因检测项目';

-- ----------------------------
-- Table structure for tb_bone_age
-- ----------------------------
DROP TABLE IF EXISTS `tb_bone_age`;
CREATE TABLE `tb_bone_age` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '编号',
  `date` date DEFAULT NULL COMMENT '日期',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '所属人id',
  `agent` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '代理商id',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注要求',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '登记时间',
  UNIQUE KEY `primary_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_case_finance_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_case_finance_info`;
CREATE TABLE `tb_case_finance_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '',
  `case_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `accept_time` date DEFAULT NULL,
  `case_user` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `case_agentuser` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `case_area` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `user_dept_level1` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `user_dept_level2` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `user_dept_level3` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `deptcode` varchar(45) CHARACTER SET utf8 DEFAULT NULL,
  `stand_sum` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `real_sum` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `return_sum` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `paragraphtime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `case_type` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `case_subtype` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `partner` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `sample_count` varchar(20) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_case_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_case_info`;
CREATE TABLE `tb_case_info` (
  `id` varchar(50) NOT NULL,
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单id',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `sex` int(10) DEFAULT NULL COMMENT '性别',
  `age` int(10) DEFAULT NULL COMMENT '年龄',
  `id_number` varchar(50) DEFAULT NULL COMMENT '身份证号',
  `pathology_number` varchar(50) DEFAULT NULL COMMENT '病理号',
  `affiliated_hospital` varchar(50) DEFAULT NULL COMMENT '所属医院',
  `hospital_number` varchar(50) DEFAULT NULL COMMENT '住院号',
  `telephone` int(20) DEFAULT NULL COMMENT '联系电话',
  `sickbed_number` varchar(50) DEFAULT NULL COMMENT '病床号',
  `patient_number` varchar(50) DEFAULT NULL COMMENT '病人编号',
  `outpatient_number` varchar(50) DEFAULT NULL COMMENT '门诊号',
  `marital_status` int(10) DEFAULT NULL COMMENT '婚姻状况',
  `inspection_department` varchar(100) DEFAULT NULL COMMENT '送检科室',
  `inspection_doctor` varchar(50) DEFAULT NULL COMMENT '送检医生',
  `delivery_date` date DEFAULT NULL COMMENT '送样日期',
  `sampling_date` date DEFAULT NULL COMMENT '接样日期',
  `report_doctor` varchar(50) DEFAULT NULL COMMENT '报告医生',
  `revisit_doctor` varchar(50) DEFAULT NULL COMMENT '复诊医生',
  `create_date` date DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(50) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='案例表';

-- ----------------------------
-- Table structure for tb_charge_standard
-- ----------------------------
DROP TABLE IF EXISTS `tb_charge_standard`;
CREATE TABLE `tb_charge_standard` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `areacode` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地code',
  `areaname` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '地区名',
  `equation` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '公式',
  `discountrate` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '折扣',
  `createuserid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人员',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `type` varchar(1) COLLATE utf8_bin DEFAULT '1' COMMENT '0:司法，1：医学',
  `userid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `username` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人姓名',
  `userinitials` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人简拼',
  `areainitials` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地简拼',
  `agentid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '被代理人id（在归属人为代理商时使用）',
  `agentname` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '被代理人姓名（在归属人为代理商时使用）',
  `attach_need` varchar(1) COLLATE utf8_bin DEFAULT '0' COMMENT '是否需要副本，0：需要；1：不需要',
  `companyid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人所属公司',
  `area_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '临时用作数据迁移',
  `is_delete` varchar(1) COLLATE utf8_bin DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_charge_standard_invasive
-- ----------------------------
DROP TABLE IF EXISTS `tb_charge_standard_invasive`;
CREATE TABLE `tb_charge_standard_invasive` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `areacode` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地编码',
  `areaname` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '地区名',
  `equation` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '公式',
  `createuserid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人员',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `program_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目类型:0,NIPT(博奥)，1，NIPT(贝瑞)，2,NIPT-plus(博奥),3.NIPT-plus(贝瑞)',
  `samplePrice` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '加样价格',
  `delete` int(10) DEFAULT '1' COMMENT '删除标识',
  `urgentPrice` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '48小时加急',
  `urgentPrice1` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '24小时加急',
  `urgentPrice2` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '8小时加急',
  `parnter_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '合作商',
  `hospital` varchar(100) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_charge_standard_new
-- ----------------------------
DROP TABLE IF EXISTS `tb_charge_standard_new`;
CREATE TABLE `tb_charge_standard_new` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `areacode` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地编码',
  `areaname` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '地区名',
  `equation` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '公式',
  `createuserid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人员',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `source_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '来源：网络1/市场0',
  `program_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目类型:0,亲子鉴定，1，亲缘鉴定，2同胞鉴定',
  `type` varchar(1) COLLATE utf8_bin DEFAULT '1' COMMENT '0:司法，1：医学',
  `userid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `username` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人姓名',
  `agentid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '被代理人id（在归属人为代理商时使用）',
  `agentname` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '被代理人姓名（在归属人为代理商时使用）',
  `singlePrice` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '单亲价格',
  `doublePrice` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '双亲价格',
  `samplePrice` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '加样价格',
  `gapPrice` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '差价',
  `specialPirce` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '特殊样本价格',
  `specialPirce1` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '特殊1样本价格',
  `specialPirce2` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '特殊2样本价格',
  `companyid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人所属公司',
  `area_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '临时用作数据迁移',
  `delete` int(10) DEFAULT '1' COMMENT '删除标识',
  `urgentPrice` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '48小时加急',
  `urgentPrice1` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '24小时加急',
  `urgentPrice2` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '8小时加急'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_charge_standard_temp
-- ----------------------------
DROP TABLE IF EXISTS `tb_charge_standard_temp`;
CREATE TABLE `tb_charge_standard_temp` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `areacode` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地编码',
  `areaname` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '地区名',
  `equation` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '公式',
  `createuserid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人员',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `source_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '来源：网络1/市场0',
  `program_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目类型',
  `type` varchar(1) COLLATE utf8_bin DEFAULT '1' COMMENT '0:司法，1：医学',
  `userid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `username` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人姓名',
  `agentid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '被代理人id（在归属人为代理商时使用）',
  `agentname` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '被代理人姓名（在归属人为代理商时使用）',
  `singlePrice` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '单亲价格',
  `doublePrice` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '双亲价格',
  `samplePrice` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '加样价格',
  `gapPrice` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '差价',
  `specialPirce` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '特殊样本价格',
  `specialPirce1` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `specialPirce2` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `companyid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人所属公司',
  `area_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '临时用作数据迁移',
  `delete` int(10) DEFAULT '1' COMMENT '删除标识',
  `urgentPrice` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '加急费用',
  `urgentPrice1` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `urgentPrice2` varchar(100) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_child_pcr
-- ----------------------------
DROP TABLE IF EXISTS `tb_child_pcr`;
CREATE TABLE `tb_child_pcr` (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '案列编号',
  `codenum` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条形码编号',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '孩子姓名',
  `fmname` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '父亲/母亲姓名',
  `age` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '年龄',
  `ownperson` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '所属人',
  `testitems` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '检测项目',
  `checkper` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '送检人',
  `cancelif` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '2' COMMENT '是否废除1是2否',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `inputperson` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `date` date DEFAULT NULL COMMENT '日期',
  `phonenum` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  `gender` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '男' COMMENT '性别',
  `agent` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '代理商',
  `reportType` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '报告形式',
  `orderPlatform` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '接单平台',
  `inspectionUnits` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '送检单位',
  `case_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '案例类型标识：1，儿童健康基因检测 2，儿童天赋基因检测',
  `create_time` datetime DEFAULT NULL COMMENT '案例创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_children_case_exception
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_case_exception`;
CREATE TABLE `tb_children_case_exception` (
  `exception_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `exception_type` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `exception_desc` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `exception_time` datetime DEFAULT NULL,
  `exception_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `is_handle` int(11) DEFAULT '0' COMMENT '0未处理，1已处理，2已删除',
  `handle_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `handle_time` datetime DEFAULT NULL,
  PRIMARY KEY (`exception_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_children_case_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_case_info`;
CREATE TABLE `tb_children_case_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `case_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '案例条形码',
  `sample_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '样本编号',
  `address` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '详细地址',
  `child_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '宝宝名称',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `child_sex` int(11) DEFAULT '0' COMMENT '0表示女，1表示男',
  `id_number` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证号码',
  `birth_hospital` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '出生医院',
  `house_area` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '户籍所在地',
  `life_area` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '生活所在地',
  `gather_time` date DEFAULT NULL COMMENT '采集时间',
  `gather_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '采集人信息',
  `case_in_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `case_in_time` date DEFAULT NULL COMMENT '录入时间',
  `agentia_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '试剂类型',
  `mail_area` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '反馈寄送地址',
  `invoice` int(2) DEFAULT '0' COMMENT '是否需要发票;0:不需要，1：需要',
  `tariff_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '套餐',
  `mail_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '快递名称',
  `mail_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '快递单号',
  `print_time` date DEFAULT NULL COMMENT '打印时间',
  `case_userid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人',
  `case_areacode` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地id',
  `case_areaname` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地名',
  `verify_state` int(10) DEFAULT '0' COMMENT '0未审核，1,待审核，2,审核不通过，3审核通过，4案例样本交接中,5实验中,6,报告打印中，7邮寄中,8归档中，9已归档',
  `process_instance_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '流程实例ID',
  `is_delete` int(11) DEFAULT '0' COMMENT '删除标识',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`case_id`),
  UNIQUE KEY `case_code_unique` (`case_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_children_case_info_old
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_case_info_old`;
CREATE TABLE `tb_children_case_info_old` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `case_code` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '案例条形码',
  `area_code` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '采集地',
  `address` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '详细地址',
  `receiver_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人信息',
  `child_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '宝宝名称',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `child_sex` int(11) DEFAULT '0' COMMENT '0表示女，1表示男',
  `id_number` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证号码',
  `birth_hospital` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '出生医院',
  `house_area` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '户籍所在地',
  `life_area` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '生活所在地',
  `gather_time` date DEFAULT NULL COMMENT '采集时间',
  `gather_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '采集人信息',
  `case_in_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `case_in_time` date DEFAULT NULL COMMENT '录入时间',
  `is_delete` int(11) DEFAULT '0',
  `agentia_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '试剂类型',
  `mail_area` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '反馈寄送地址',
  `tariff_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '套餐',
  `mail_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '快递名称',
  `mail_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '快递单号',
  `print_time` date DEFAULT NULL COMMENT '打印时间',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`case_id`),
  UNIQUE KEY `case_code_unique` (`case_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_children_case_photo
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_case_photo`;
CREATE TABLE `tb_children_case_photo` (
  `photo_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例id',
  `photo_path` varchar(1000) COLLATE utf8_bin NOT NULL COMMENT '路径',
  `upload_time` datetime NOT NULL COMMENT '上传时间',
  `upload_user` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '上传人员',
  `photo_type` varchar(1) COLLATE utf8_bin DEFAULT '1' COMMENT '照片类型，1：登记人员上传，2：处理后上传照片,3:生成照片正面，4:生成照片正面，5，登记表格',
  PRIMARY KEY (`photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_children_case_result
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_case_result`;
CREATE TABLE `tb_children_case_result` (
  `result_id` varchar(50) NOT NULL COMMENT '结果id',
  `case_id` varchar(50) NOT NULL COMMENT '案例id',
  `case_code` varchar(50) NOT NULL COMMENT '案例编号',
  `result_in_time` date NOT NULL COMMENT '录入时间',
  PRIMARY KEY (`result_id`),
  UNIQUE KEY `case_result_unique` (`case_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tb_children_custody_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_custody_info`;
CREATE TABLE `tb_children_custody_info` (
  `custody_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `custody_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '监护人姓名',
  `custody_call` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '称谓',
  `id_number` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证',
  `phone` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '所属案例',
  PRIMARY KEY (`custody_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_children_gather_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_gather_info`;
CREATE TABLE `tb_children_gather_info` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `gather_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '采集人姓名',
  `id_number` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '采集人身份证',
  `phone` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  `company_name` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '所在单位',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_children_locus_history
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_locus_history`;
CREATE TABLE `tb_children_locus_history` (
  `case_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例编号',
  `resultstr` text COLLATE utf8_bin,
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`case_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_children_locus_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_locus_info`;
CREATE TABLE `tb_children_locus_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例id',
  `locus_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '位点名称',
  `locus_value` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '位点值',
  PRIMARY KEY (`case_id`,`locus_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_children_relay_receive
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_relay_receive`;
CREATE TABLE `tb_children_relay_receive` (
  `relay_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `receive_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '接收样本id',
  `receive_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '确认人',
  `receive_time` datetime DEFAULT NULL COMMENT '确认时间',
  `receive_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '确认备注',
  PRIMARY KEY (`relay_id`),
  UNIQUE KEY `unqiue_revice_id` (`receive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_children_sample_receive
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_sample_receive`;
CREATE TABLE `tb_children_sample_receive` (
  `receive_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `transfer_num` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '交接单号',
  `transfer_time` datetime DEFAULT NULL COMMENT '交接时间',
  `transfer_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '交接人',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '交接备注',
  `state` int(1) DEFAULT '1' COMMENT '状态1：未审核，2：已审核',
  PRIMARY KEY (`receive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_children_sample_receive_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_sample_receive_info`;
CREATE TABLE `tb_children_sample_receive_info` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `sample_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '样本编号',
  `receive_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '交接单id',
  `confirm_state` int(2) DEFAULT '0' COMMENT '交接确认状态，0：样本接收，1确认通过，2：确认不通过',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_children_tariff
-- ----------------------------
DROP TABLE IF EXISTS `tb_children_tariff`;
CREATE TABLE `tb_children_tariff` (
  `tariff_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `tariff_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '名称',
  `tariff_price` decimal(10,2) NOT NULL COMMENT '价格',
  `tariff_state` int(2) NOT NULL DEFAULT '0' COMMENT '状态 0 可用 1 作废',
  `tariff_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`tariff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_company_code_case_id
-- ----------------------------
DROP TABLE IF EXISTS `tb_company_code_case_id`;
CREATE TABLE `tb_company_code_case_id` (
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `company_code` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `case_code` varchar(50) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_ct_dna
-- ----------------------------
DROP TABLE IF EXISTS `tb_ct_dna`;
CREATE TABLE `tb_ct_dna` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例编号',
  `date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '日期',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `sex` varchar(2) COLLATE utf8_bin DEFAULT '男' COMMENT '性别',
  `clinical_diagnosis` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '临床诊断',
  `histort_effect` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '具体用药史和疗效',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `cancelif` varchar(1) COLLATE utf8_bin DEFAULT '2' COMMENT '1:是，2：否',
  `create_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '创建日期'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_dic_agentia
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_agentia`;
CREATE TABLE `tb_dic_agentia` (
  `agentia_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '试剂id',
  `agentia_name` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '试剂名称',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '是否使用 0 是 1 否',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`agentia_id`,`agentia_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_dic_agentia_locus
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_agentia_locus`;
CREATE TABLE `tb_dic_agentia_locus` (
  `locus_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '位点名称',
  `agentia_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '试剂id',
  `order` int(2) NOT NULL COMMENT '次序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_dic_area_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_area_info`;
CREATE TABLE `tb_dic_area_info` (
  `id` varchar(10) NOT NULL COMMENT 'ID',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `parentID` varchar(10) DEFAULT '0' COMMENT '父ID,0标识跟省份',
  `initials` varchar(100) DEFAULT NULL COMMENT '首字母',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='省市县表';

-- ----------------------------
-- Table structure for tb_dic_area_info_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_area_info_bak`;
CREATE TABLE `tb_dic_area_info_bak` (
  `id` varchar(10) CHARACTER SET utf8 NOT NULL COMMENT 'ID',
  `name` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '名称',
  `parentID` varchar(10) CHARACTER SET utf8 DEFAULT '0' COMMENT '父ID,0标识跟省份',
  `initials` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '首字母'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_dic_bank_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_bank_info`;
CREATE TABLE `tb_dic_bank_info` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `bankaccount` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '银行账号',
  `bankname` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '银行名',
  `remark` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `companyid` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '所属企业id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_dic_keys1_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_keys1_bak`;
CREATE TABLE `tb_dic_keys1_bak` (
  `keycode` varchar(45) NOT NULL,
  `keyname` varchar(100) DEFAULT NULL,
  `status` varchar(45) DEFAULT '0',
  `sort` int(45) DEFAULT NULL,
  `id` varchar(45) NOT NULL,
  PRIMARY KEY (`keycode`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表总key';

-- ----------------------------
-- Table structure for tb_dic_print_company
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_print_company`;
CREATE TABLE `tb_dic_print_company` (
  `id` varchar(50) NOT NULL,
  `companyid` varchar(50) DEFAULT NULL,
  `pmodel_code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_dic_print_company_area
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_print_company_area`;
CREATE TABLE `tb_dic_print_company_area` (
  `id` varchar(50) NOT NULL,
  `print_company_id` varchar(50) DEFAULT NULL,
  `area_code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_dic_print_company_area_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_print_company_area_bak`;
CREATE TABLE `tb_dic_print_company_area_bak` (
  `print_company_id` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `area_code` varchar(50) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_dic_print_gene
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_print_gene`;
CREATE TABLE `tb_dic_print_gene` (
  `name` varchar(100) NOT NULL,
  `model` varchar(100) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `reagent_name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tb_dic_print_model
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_print_model`;
CREATE TABLE `tb_dic_print_model` (
  `code` varchar(50) COLLATE utf8_bin NOT NULL,
  `text` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `chart` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `reagent_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `reagent_name_ext` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`code`,`reagent_name`,`reagent_name_ext`),
  KEY `type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_dic_remittance_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_remittance_info`;
CREATE TABLE `tb_dic_remittance_info` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `remittanceAccount` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '回款账户',
  `accountName` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '账户名称',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `is_delete` varchar(1) COLLATE utf8_bin DEFAULT '1' COMMENT '未删除1，删除2',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_dic_values1
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_values1`;
CREATE TABLE `tb_dic_values1` (
  `id` varchar(45) NOT NULL,
  `key1` varchar(45) NOT NULL,
  `value` varchar(45) NOT NULL,
  `keycode` varchar(45) NOT NULL,
  `status` varchar(2) DEFAULT '0',
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表值表';

-- ----------------------------
-- Table structure for tb_diet_features_diseases_pcr
-- ----------------------------
DROP TABLE IF EXISTS `tb_diet_features_diseases_pcr`;
CREATE TABLE `tb_diet_features_diseases_pcr` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案列编号',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `age` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '年龄',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '所属人',
  `testitems` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '检测项目',
  `checkper` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '送检人',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否废除1是2否',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `date` date DEFAULT NULL COMMENT '日期',
  `phonenum` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  `gender` varchar(10) COLLATE utf8_bin DEFAULT '男' COMMENT '性别',
  `agent` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '代理商',
  `reportType` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '报告种类',
  `orderPlatform` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '接单平台',
  `inspectionUnits` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '送检单位',
  `case_type` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '案例类型标识：1，减肥美容基因检测 2，个体特征基因检测 3，重大慢病基因检测',
  `create_time` datetime DEFAULT NULL COMMENT '案例创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_document_appraisal
-- ----------------------------
DROP TABLE IF EXISTS `tb_document_appraisal`;
CREATE TABLE `tb_document_appraisal` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `num` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '案例编号',
  `date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '日期',
  `program` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人id',
  `remark` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '登记时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_document_appraisal_program
-- ----------------------------
DROP TABLE IF EXISTS `tb_document_appraisal_program`;
CREATE TABLE `tb_document_appraisal_program` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `program_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '文书鉴定项目名称',
  `program_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '文书鉴定项目名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_drug_detection
-- ----------------------------
DROP TABLE IF EXISTS `tb_drug_detection`;
CREATE TABLE `tb_drug_detection` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `num` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '案例编号',
  `name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '被鉴定人',
  `date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '委托日期',
  `entrusted_unit` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '委托单位',
  `sample_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '样品类型',
  `sample_count` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '样本数',
  `program` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定事项',
  `inspection` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '送检人',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人id',
  `remark` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '登记时间',
  `program_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目类型',
  PRIMARY KEY (`num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_drug_detection_program
-- ----------------------------
DROP TABLE IF EXISTS `tb_drug_detection_program`;
CREATE TABLE `tb_drug_detection_program` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `program_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '毒品检测项目名称',
  `program_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '毒品检测项目名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_express_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_express_info`;
CREATE TABLE `tb_express_info` (
  `num` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '案列编号',
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'id',
  `expresstype` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '快递类型',
  `expressnum` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '快递单号',
  `expresstime` varchar(50) DEFAULT NULL COMMENT '快递时间',
  `recive` varchar(50) DEFAULT NULL COMMENT '收件人',
  `expressremark` varchar(100) DEFAULT NULL COMMENT '备注',
  `input_person` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_finance_amoeba_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_amoeba_info`;
CREATE TABLE `tb_finance_amoeba_info` (
  `amoeba_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `amoeba_program` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '项目',
  `amoeba_deptment` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '一级部门',
  `amoeba_deptment1` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '二级部门',
  `amoeba_month` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '月份',
  `amoeba_sum` double DEFAULT NULL COMMENT '价格汇总',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `sort` int(10) DEFAULT NULL COMMENT '排序',
  `is_delete` int(1) DEFAULT '1' COMMENT '删除标志',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_case_aptitude
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_case_aptitude`;
CREATE TABLE `tb_finance_case_aptitude` (
  `aptitude_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `aptitude_month` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '资质费月份',
  `aptitude_partner` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资质费合作方',
  `aptitude_fee` double DEFAULT NULL COMMENT '资质费用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_case_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_case_detail`;
CREATE TABLE `tb_finance_case_detail` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例id',
  `case_user` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人',
  `webchart` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '工号',
  `case_area` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地',
  `accept_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '受理时间',
  `confirm_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '财务确认时间',
  `real_sum` double DEFAULT '0' COMMENT '应收',
  `return_sum` double DEFAULT '0' COMMENT '案例款项',
  `user_dept_level1` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '一级部门',
  `user_dept_level2` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '二级部门',
  `user_dept_level3` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '三级部门',
  `user_dept_level4` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '四级部门',
  `user_dept_level5` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '五级部门',
  `insideCost` double DEFAULT '0' COMMENT '内部结算费用',
  `insideCostUnit` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '内部结算部门',
  `manageCost` double DEFAULT '0' COMMENT '管理费',
  `manageCostUnit` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '管理费部门',
  `externalCost` double DEFAULT '0' COMMENT '委外成本',
  `partner` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '合作方',
  `aptitudeCost` double DEFAULT '0' COMMENT '资质费',
  `experimentCost` double DEFAULT '0' COMMENT '实验费',
  `case_type` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '项目',
  `case_subtype` varchar(500) COLLATE utf8_bin DEFAULT '' COMMENT '子项',
  `type` varchar(50) COLLATE utf8_bin DEFAULT '服务收入' COMMENT '类型',
  `case_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `client` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '委托人',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '案例备注',
  `finance_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '财务备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`,`case_id`),
  KEY `case_user` (`case_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_case_detail_all
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_case_detail_all`;
CREATE TABLE `tb_finance_case_detail_all` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例id',
  `case_user` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人',
  `webchart` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '工号',
  `case_area` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地',
  `accept_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '受理时间',
  `confirm_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '财务确认时间',
  `real_sum` varchar(100) COLLATE utf8_bin DEFAULT '0' COMMENT '应收',
  `return_sum` varchar(100) COLLATE utf8_bin DEFAULT '0' COMMENT '案例款项',
  `user_dept_level1` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '一级部门',
  `user_dept_level2` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '二级部门',
  `user_dept_level3` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '三级部门',
  `user_dept_level4` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '四级部门',
  `user_dept_level5` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '五级部门',
  `insideCost` varchar(100) COLLATE utf8_bin DEFAULT '0' COMMENT '内部结算费用',
  `insideCostUnit` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '内部结算部门',
  `manageCost` varchar(100) COLLATE utf8_bin DEFAULT '0' COMMENT '管理费',
  `manageCostUnit` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '管理费部门',
  `externalCost` varchar(100) COLLATE utf8_bin DEFAULT '0' COMMENT '委外成本',
  `partner` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '合作方',
  `aptitudeCost` varchar(100) COLLATE utf8_bin DEFAULT '0' COMMENT '资质费',
  `experimentCost` varchar(100) COLLATE utf8_bin DEFAULT '0' COMMENT '实验费',
  `case_type` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '项目',
  `case_subtype` varchar(500) COLLATE utf8_bin DEFAULT '' COMMENT '子项',
  `type` varchar(50) COLLATE utf8_bin DEFAULT '服务收入' COMMENT '类型',
  `case_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `client` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '委托人',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '案例备注',
  `finance_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '财务备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`,`case_id`),
  KEY `case_user` (`case_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_channel_case_aptitude
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_channel_case_aptitude`;
CREATE TABLE `tb_finance_channel_case_aptitude` (
  `aptitude_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `aptitude_month` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '资质费月份',
  `aptitude_partner` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资质费合作方',
  `user_dept_level1` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '部门',
  `aptitude_fee` double DEFAULT NULL COMMENT '资质费用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_config`;
CREATE TABLE `tb_finance_config` (
  `config_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `program` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '总类型',
  `finance_program` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '子类型',
  `finance_program_type` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '项目类型',
  `front_settlement` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '前端结算公式',
  `finance_manage` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '管理费公式',
  `back_settlement` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '后端结算公式',
  `back_remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '后端参与结算的部门分配',
  `business_support` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '业务支撑中信结算',
  `agency_price` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '代理价',
  `experiment_price` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '实验成本价',
  `price` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '定价',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `update_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `update_time` date DEFAULT NULL COMMENT '创建人',
  `finance_manage_dept` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '管理费部门',
  `back_settlement_dept` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '后端结算部门',
  `back_settlement_dept1` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '后端结算部门1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_config_aptitude
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_config_aptitude`;
CREATE TABLE `tb_finance_config_aptitude` (
  `config_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '资质费id',
  `settlement_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '结算费用配置id',
  `provice` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '省份',
  `partnername` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '合作方',
  `aptitude_sample` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '样本资质费',
  `aptitude_case` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例资质费',
  `experiment_sample` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '实验样本费',
  `experiment_case` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '实验案例费',
  `partner_start` date DEFAULT NULL COMMENT '合作起时间',
  `partner_end` date DEFAULT NULL COMMENT '合作截至时间',
  `partner_flag` int(1) DEFAULT '1' COMMENT '是否合作1：是',
  `month_num` int(11) DEFAULT NULL COMMENT '月度案例数',
  `month_num_reduce` int(11) DEFAULT NULL COMMENT '超过月度案例数减去值',
  `sample_special1` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '一类样本加价',
  `sample_special2` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '二类样本加价',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `update_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_contract_attachment
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_contract_attachment`;
CREATE TABLE `tb_finance_contract_attachment` (
  `id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `contract_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例ID',
  `contract_num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例条形码',
  `attachment_path` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '文件位置',
  `attachment_date` date NOT NULL COMMENT '上传时间',
  `attachment_type` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '文件类型',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '上传人员',
  `is_delete` varchar(1) COLLATE utf8_bin DEFAULT '1' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_contract_plan
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_contract_plan`;
CREATE TABLE `tb_finance_contract_plan` (
  `contract_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '合同id',
  `contract_price` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '合同总价',
  `contract_program` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '合同项目',
  `customer_name` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `contract_num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '合同编号',
  `contract_unit` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '合作单位',
  `contract_userid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `contract_areacode` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地',
  `contract_areaname` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地名称',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `status` int(2) DEFAULT '1' COMMENT '计划状态：1待执行，2执行中，3未执行,4执行结束',
  `oa_num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'oa编号',
  `remark` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_department
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_department`;
CREATE TABLE `tb_finance_department` (
  `usercode` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '用户登录名',
  `userlevel` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '级别：1，全公司，2，事业部，3，二级部门，4，三级部门',
  `department` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '级别管理部门'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_info`;
CREATE TABLE `tb_finance_info` (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '案列编号',
  `receivables` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '应收款项',
  `payment` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '所付款项',
  `paid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '实收款项',
  `paragraphtime` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '到款时间',
  `case_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '案例类型',
  `remarks` varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `account_type` varchar(200) DEFAULT NULL COMMENT '账户类型',
  `oa_num` varchar(50) DEFAULT NULL COMMENT 'OA编号',
  `remittanceName` varchar(100) DEFAULT NULL COMMENT '帐户名称',
  `remittanceDate` varchar(50) DEFAULT NULL COMMENT '汇款日期',
  `discountPrice` varchar(50) DEFAULT NULL COMMENT '优惠价格',
  `siteFee` varchar(50) DEFAULT NULL COMMENT '场地费',
  `fees` varchar(50) DEFAULT NULL COMMENT '手续费',
  `confirm_flag` varchar(1) DEFAULT '1' COMMENT '确认标志',
  `confirm_date` varchar(50) DEFAULT NULL COMMENT '确认时间',
  `input_person` varchar(50) DEFAULT NULL COMMENT '录入人',
  `confirm_per` varchar(50) DEFAULT NULL COMMENT '确认人',
  `report_model` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_finance_info_follow
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_info_follow`;
CREATE TABLE `tb_finance_info_follow` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案列编号',
  `receivables` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '应收款项',
  `payment` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '所付款项',
  `paid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '实收款项',
  `paragraphtime` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '到款时间',
  `case_type` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例类型',
  `remarks` varchar(2048) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `account_type` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '账户类型',
  `oa_num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'OA编号',
  `remittanceName` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '帐户名称',
  `remittanceDate` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '汇款日期',
  `discountPrice` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '优惠价格',
  `siteFee` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '场地费',
  `fees` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '手续费',
  `confirm_flag` varchar(1) COLLATE utf8_bin DEFAULT '1' COMMENT '确认标志',
  `confirm_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '确认时间',
  `input_person` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `confirm_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '确认人',
  `create_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `is_delete` int(1) DEFAULT '1' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_inside_cost
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_inside_cost`;
CREATE TABLE `tb_finance_inside_cost` (
  `cost_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `program_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目名称',
  `program_case_num` int(50) DEFAULT NULL COMMENT '案例数',
  `program_month` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '案例月份',
  `user_dept_level1` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '一级部门',
  `finance_cost` double DEFAULT NULL COMMENT '内部结算成本'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_kit_settlement
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_kit_settlement`;
CREATE TABLE `tb_finance_kit_settlement` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `kit_count` int(10) DEFAULT NULL COMMENT '数量',
  `kit_type` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '试剂盒类型',
  `kit_price` double DEFAULT NULL COMMENT '试剂盒单价',
  `kit_price_all` double DEFAULT NULL COMMENT '总价',
  `user_dept_level1` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收入结算部门',
  `manageCost` double DEFAULT NULL,
  `manageCostUnit` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `insideCostUnit` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `insideCost` double DEFAULT NULL,
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `confirm_state` varchar(1) COLLATE utf8_bin DEFAULT '1' COMMENT '2已确认，1，未确认',
  `confirm_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '确认人',
  `confirm_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '确认时间',
  `is_delete` varchar(1) COLLATE utf8_bin DEFAULT '1' COMMENT '删除标志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_oa_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_oa_log`;
CREATE TABLE `tb_finance_oa_log` (
  `id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '流程id',
  `requestid` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `operatePer` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '操作人员',
  `operateTime` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '操作时间',
  `operateLog` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '操作日志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_oa1
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_oa1`;
CREATE TABLE `tb_finance_oa1` (
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `requestid` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sqr` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sqrxm` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `tdr` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `tdrxm` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ssbm` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ssbmmc` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ssgs` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ssgsmc` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sqrq` date DEFAULT NULL,
  `mx1bxje` double DEFAULT NULL,
  `bxsm` text COLLATE utf8_bin,
  `mx1fykm` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `kmmc` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mx1cdzt` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ztmc1` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mx1cdlx` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ztejmc` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ztsybmc` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `operatedate` date DEFAULT NULL,
  `operatetime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '类型，1：报销2：合同付款3:采购',
  `djbh` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `sqrworkcode` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '申请人工号',
  `tdrworkcode` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '提单人工号',
  `cwcnyj` varchar(500) COLLATE utf8_bin DEFAULT '' COMMENT '财务出纳意见',
  `isremark` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '财务出纳操作情况',
  `user_dept_level1` varchar(50) COLLATE utf8_bin DEFAULT '',
  `user_dept_level2` varchar(50) COLLATE utf8_bin DEFAULT '',
  `user_dept_level3` varchar(50) COLLATE utf8_bin DEFAULT '',
  `user_dept_level4` varchar(50) COLLATE utf8_bin DEFAULT '',
  `user_dept_level5` varchar(50) COLLATE utf8_bin DEFAULT '',
  KEY `tdrxm` (`tdrxm`),
  FULLTEXT KEY `kmmc` (`kmmc`,`ssgsmc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_oa2
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_oa2`;
CREATE TABLE `tb_finance_oa2` (
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `requestid` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sqr` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sqrxm` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sqbm` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sqbmmc` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sqgs` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sqgsmc` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sqrq` date DEFAULT NULL,
  `jkje` double DEFAULT NULL,
  `nr` text COLLATE utf8_bin,
  `operatedate` date DEFAULT NULL,
  `operatetime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `lcbh` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `sqrworkcode` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '申请人工号',
  `cwcnyj` varchar(500) COLLATE utf8_bin DEFAULT '' COMMENT '财务出纳意见',
  `isremark` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '财务出纳操作情况',
  `user_dept_level1` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '一',
  `user_dept_level2` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '二',
  `user_dept_level3` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '三',
  `user_dept_level4` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '四',
  `user_dept_level5` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '五'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_program_price
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_program_price`;
CREATE TABLE `tb_finance_program_price` (
  `price_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `program_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目名称',
  `program_case_num` int(50) DEFAULT NULL COMMENT '案例数',
  `program_month` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '案例月份',
  `user_dept_level1` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '一级部门',
  `finance_dept` double DEFAULT NULL COMMENT '内部结算费'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_statement_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_statement_bak`;
CREATE TABLE `tb_finance_statement_bak` (
  `finance_id` varchar(50) NOT NULL COMMENT '财务id',
  `case_id` varchar(50) DEFAULT NULL COMMENT '对应案例id',
  `case_code` varchar(50) DEFAULT NULL COMMENT '案例编号',
  `stand_sum` varchar(10) DEFAULT NULL COMMENT '标准收费',
  `real_sum` varchar(10) DEFAULT NULL COMMENT '实收金额',
  `return_sum` varchar(10) DEFAULT NULL COMMENT '回款金额',
  `charge_id` varchar(50) DEFAULT NULL COMMENT '归属配置id',
  `status` varchar(1) DEFAULT '3' COMMENT '财务审核状态 0 正常 1 异常 2日报已结算 3登记状态',
  `type` varchar(1) DEFAULT '0' COMMENT '0为正常1为先出报告后付款2为免单',
  `case_type` varchar(10) DEFAULT NULL COMMENT '财务所属项目',
  `update_time` varchar(20) DEFAULT NULL COMMENT '财务更新时间',
  `confirm_date` varchar(20) DEFAULT NULL COMMENT '财务确认时间',
  `paragraph_time` varchar(20) DEFAULT NULL COMMENT '财务到款时间',
  `finance_account` varchar(100) DEFAULT NULL COMMENT '到款账户类型',
  `remittance_name` varchar(100) DEFAULT NULL COMMENT '汇款账户名称',
  `remittance_time` varchar(20) DEFAULT NULL COMMENT '汇款时间',
  `discount_price` varchar(10) DEFAULT NULL COMMENT '优惠价格',
  `finance_remark` varchar(200) DEFAULT NULL COMMENT '财务备注',
  `case_remark` varchar(200) DEFAULT NULL COMMENT '案例备注',
  `case_accepttime` varchar(20) DEFAULT NULL COMMENT '案例受理日期',
  `case_client` varchar(20) DEFAULT NULL COMMENT '案例委托人',
  `case_sample_fm` varchar(500) DEFAULT NULL COMMENT '案例父亲样本信息',
  `case_sample_child` varchar(500) DEFAULT NULL COMMENT '案例母亲样本信息',
  `case_sample_idCard` varchar(500) DEFAULT NULL COMMENT '案例样本id',
  `case_sample_birthDate` varchar(50) DEFAULT NULL COMMENT '案例样本生日',
  `case_sample_count` varchar(10) DEFAULT NULL COMMENT '样本数量',
  `finance_type` varchar(50) DEFAULT NULL COMMENT '财务类型（财务所属项目再划分）',
  `dailyid` varchar(50) DEFAULT NULL COMMENT '日报或月报id',
  `update_user` varchar(50) DEFAULT NULL COMMENT '操作人员',
  PRIMARY KEY (`finance_id`),
  UNIQUE KEY `unique_case_id` (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_finance_wages
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_wages`;
CREATE TABLE `tb_finance_wages` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `attachment_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '附件id',
  `user_dept_level1` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '一级部门',
  `user_dept_level2` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '二级部门',
  `user_dept_level3` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '三级部门',
  `user_dept_level4` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '四级部门',
  `user_dept_level5` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '五级部门',
  `workcode` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '工号',
  `wages_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '人员姓名',
  `wages_month` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '人工成本月份',
  `wages` double DEFAULT NULL COMMENT '人工成本',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `is_delete` int(1) DEFAULT '1' COMMENT '删除标志',
  `wages_social` double DEFAULT NULL COMMENT '社保',
  `wages_accumulation` double DEFAULT NULL COMMENT '公积金',
  `wages_middle` double DEFAULT NULL COMMENT '月中小计',
  `wages_end` double DEFAULT NULL COMMENT '月底小计',
  `wages_other` double DEFAULT NULL COMMENT '其他金额',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  KEY `wages_name` (`wages_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_finance_wages_attachment
-- ----------------------------
DROP TABLE IF EXISTS `tb_finance_wages_attachment`;
CREATE TABLE `tb_finance_wages_attachment` (
  `attachment_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `attachment_path` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '路径',
  `attachment_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '上传时间',
  `wages_month` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '工资月份',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '上传人员',
  `is_delete` int(1) DEFAULT '1',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  KEY `attachment_id` (`attachment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_folic_acid
-- ----------------------------
DROP TABLE IF EXISTS `tb_folic_acid`;
CREATE TABLE `tb_folic_acid` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案列编号',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `age` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '年龄',
  `sex` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '所属人',
  `diagnosis` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '病理诊断',
  `testitems` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '检测项目',
  `checkper` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '送检人',
  `phonenum` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否废除1是2否',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `date` date DEFAULT NULL COMMENT '日期',
  `hospital` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '送检医院',
  `agent` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '代理商id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_food_safety
-- ----------------------------
DROP TABLE IF EXISTS `tb_food_safety`;
CREATE TABLE `tb_food_safety` (
  `id` varchar(50) NOT NULL COMMENT 'id',
  `num` varchar(50) DEFAULT NULL COMMENT '编号',
  `samplename` varchar(50) DEFAULT NULL COMMENT '样品名称',
  `quantity` varchar(10) DEFAULT NULL COMMENT '数量',
  `sampleCount` varchar(200) DEFAULT NULL COMMENT '样本数',
  `testitems` varchar(200) DEFAULT NULL COMMENT '测试项目',
  `testmethod` varchar(100) DEFAULT NULL COMMENT '检验方法',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `date` date DEFAULT NULL COMMENT '日期',
  `serviceprovider` varchar(100) DEFAULT NULL COMMENT '服务商',
  `inputperson` varchar(50) DEFAULT NULL COMMENT '录入人',
  `ownperson` varchar(50) DEFAULT NULL COMMENT '归属人id',
  `cancelif` varchar(2) DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `agent` varchar(50) DEFAULT NULL COMMENT '代理商',
  `program_type` varchar(100) DEFAULT NULL COMMENT '项目类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_forensic_cases
-- ----------------------------
DROP TABLE IF EXISTS `tb_forensic_cases`;
CREATE TABLE `tb_forensic_cases` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '编号',
  `date` date DEFAULT NULL COMMENT '收案时间',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '所属人id',
  `client` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '委托人',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注要求',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `commitment` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '委托事项',
  `agent` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '代理商id',
  UNIQUE KEY `primary_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_invasive_dna
-- ----------------------------
DROP TABLE IF EXISTS `tb_invasive_dna`;
CREATE TABLE `tb_invasive_dna` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '编号',
  `date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '受理日期',
  `consigningDate` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '委托日期',
  `fatherName` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '父亲姓名',
  `motherName` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '母亲姓名',
  `fatherType` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '父本类型',
  `gestational` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '母本孕周',
  `client` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '委托方',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '市场归属人id',
  `agent` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '代理商id',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注要求',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_invasive_hosptial_areaname
-- ----------------------------
DROP TABLE IF EXISTS `tb_invasive_hosptial_areaname`;
CREATE TABLE `tb_invasive_hosptial_areaname` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `areaname` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地名',
  `areacode` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '归属地id',
  `hospital` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '医院',
  `remark` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_invasive_ownperson_toemails
-- ----------------------------
DROP TABLE IF EXISTS `tb_invasive_ownperson_toemails`;
CREATE TABLE `tb_invasive_ownperson_toemails` (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ownperson` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '市场归属人id',
  `toEmails` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '收件邮箱地址',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `ownpersonname` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '市场归属人名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_invasive_pre_attachment
-- ----------------------------
DROP TABLE IF EXISTS `tb_invasive_pre_attachment`;
CREATE TABLE `tb_invasive_pre_attachment` (
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `attachment_path` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '路径',
  `attachment_date` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '上传时间',
  `upload_userid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '上传人员',
  `down_flag` int(2) DEFAULT '0' COMMENT '下载标识',
  `download_time` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `down_userid` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `uuid` varchar(50) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_invasive_pre_email
-- ----------------------------
DROP TABLE IF EXISTS `tb_invasive_pre_email`;
CREATE TABLE `tb_invasive_pre_email` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例id',
  `emailFrom` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '发件人邮箱',
  `toEmails` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '收件人邮箱',
  `emailUserName` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '发件人名字',
  `attachFileNames` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '附件',
  `content` varchar(400) COLLATE utf8_bin DEFAULT NULL COMMENT '邮件内容',
  `subject` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '邮件主题',
  `emaildate` datetime DEFAULT NULL COMMENT '邮件时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_invasive_prenatal
-- ----------------------------
DROP TABLE IF EXISTS `tb_invasive_prenatal`;
CREATE TABLE `tb_invasive_prenatal` (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '编号',
  `date` date DEFAULT NULL COMMENT '日期',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '条码',
  `sampledate` date DEFAULT NULL COMMENT '采样日期',
  `inspectionunit` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '送检单位',
  `ownperson` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '市场归属人id',
  `agent` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '代理商id',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注要求',
  `cancelif` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `inputperson` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `hospital` varchar(100) DEFAULT NULL COMMENT '所属医院',
  `verify_state` varchar(2) DEFAULT '0',
  `process_instance_id` varchar(30) DEFAULT NULL,
  `verify_remark` varchar(500) DEFAULT NULL,
  `confirm_code` varchar(50) DEFAULT NULL COMMENT '优惠码',
  `areacode` varchar(100) DEFAULT NULL COMMENT '归属地区id',
  `areaname` varchar(100) DEFAULT NULL COMMENT '归属地名',
  `emailflag` varchar(20) DEFAULT '0' COMMENT '邮件发送标识，0未发送，1发送',
  UNIQUE KEY `primary_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_agent
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_agent`;
CREATE TABLE `tb_judicial_agent` (
  `id` varchar(100) COLLATE utf8_bin NOT NULL,
  `userid` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '代理商id',
  `peruserid` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '所属市场人员',
  `createtime` date DEFAULT NULL,
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `delstatus` varchar(10) COLLATE utf8_bin DEFAULT '0',
  `flag` int(10) DEFAULT '1' COMMENT '标识1：代理商管理，2：采样员管理',
  `createper` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_agent_attachment
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_agent_attachment`;
CREATE TABLE `tb_judicial_agent_attachment` (
  `id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例ID',
  `attachment_path` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '文件位置',
  `attachment_date` date NOT NULL COMMENT '上传时间',
  `attachment_type` int(2) NOT NULL COMMENT '文件类型1为登记表格0为其他',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '上传人员',
  `create_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_archive_read
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_archive_read`;
CREATE TABLE `tb_judicial_archive_read` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `archive_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '归档id',
  `read_per` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '查看人',
  `read_date` date NOT NULL COMMENT '查看时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_baseinfo_verify
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_baseinfo_verify`;
CREATE TABLE `tb_judicial_baseinfo_verify` (
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `verify_baseinfo_time` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `verify_baseinfo_state` int(11) DEFAULT '0',
  `verify_baseinfo_person` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `verify_baseinfo_remark` text COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_case_archive
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_archive`;
CREATE TABLE `tb_judicial_case_archive` (
  `archive_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `archive_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '归档编号',
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `archive_address` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '归档地址',
  `archive_date` date NOT NULL COMMENT '归档日期',
  `archive_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `archive_path` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '归档目录',
  `sample_archive_address` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '样本归档地址',
  PRIMARY KEY (`archive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_case_attachment
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_attachment`;
CREATE TABLE `tb_judicial_case_attachment` (
  `id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例ID',
  `case_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例条形码',
  `attachment_path` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '文件位置',
  `attachment_date` date NOT NULL COMMENT '上传时间',
  `attachment_type` int(2) NOT NULL COMMENT '文件类型1为登记表格0为其他',
  `is_print` int(11) DEFAULT '0' COMMENT '0打印，1未打印',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '上传人员',
  `create_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_case_attachment_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_attachment_bak`;
CREATE TABLE `tb_judicial_case_attachment_bak` (
  `id` varchar(100) COLLATE utf8_bin NOT NULL,
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `case_code` varchar(50) COLLATE utf8_bin NOT NULL,
  `attachment_path` varchar(255) COLLATE utf8_bin NOT NULL,
  `attachment_date` date NOT NULL,
  `attachment_type` int(11) NOT NULL,
  `is_print` int(11) DEFAULT '0',
  `bak_time` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `create_date` varchar(50) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_case_bill
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_bill`;
CREATE TABLE `tb_judicial_case_bill` (
  `bill_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例ID',
  `bill_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '发票编号',
  `bill_charge` decimal(10,2) DEFAULT NULL COMMENT '发票金额',
  `date` date DEFAULT NULL COMMENT '开票日期',
  `remark` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '开票说明',
  `bill_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '开票人',
  PRIMARY KEY (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_case_code
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_code`;
CREATE TABLE `tb_judicial_case_code` (
  `id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'id',
  `letter` varchar(2) COLLATE utf8_bin DEFAULT 'Z' COMMENT '首字母',
  `year` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '年份',
  `month` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '月份',
  `num` varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '编号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_case_confirm_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_confirm_info`;
CREATE TABLE `tb_judicial_case_confirm_info` (
  `confirm_id` varchar(50) NOT NULL COMMENT '确认ID',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `confirm_per` varchar(50) DEFAULT NULL COMMENT '确认人',
  `confirm_remark` varchar(500) DEFAULT NULL COMMENT '确认备注',
  `relay_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`confirm_id`),
  UNIQUE KEY `re_unique` (`relay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_case_exception
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_exception`;
CREATE TABLE `tb_judicial_case_exception` (
  `exception_id` varchar(50) NOT NULL,
  `case_id` varchar(50) NOT NULL,
  `exception_type` varchar(50) DEFAULT NULL,
  `exception_desc` varchar(500) DEFAULT NULL,
  `exception_time` datetime DEFAULT NULL,
  `exception_per` varchar(50) DEFAULT NULL,
  `is_handle` int(11) DEFAULT '0' COMMENT '0未处理，1已处理，2已删除',
  `handle_per` varchar(50) DEFAULT NULL,
  `handle_time` datetime DEFAULT NULL,
  PRIMARY KEY (`exception_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_case_express
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_express`;
CREATE TABLE `tb_judicial_case_express` (
  `id` varchar(50) NOT NULL COMMENT '快递单号',
  `code` varchar(10) DEFAULT NULL COMMENT '快递公司',
  `receiver` varchar(10) DEFAULT NULL COMMENT '收件人',
  `address` varchar(255) DEFAULT NULL COMMENT '收件地址',
  `date` date DEFAULT NULL COMMENT '寄送日期',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_case_param
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_param`;
CREATE TABLE `tb_judicial_case_param` (
  `PARAM_TYPE` varchar(30) CHARACTER SET gbk NOT NULL,
  `PARAM_NAME` varchar(30) CHARACTER SET gbk NOT NULL,
  `PARAM_VALUE` double DEFAULT NULL,
  `reagent_name` varchar(128) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`PARAM_TYPE`,`PARAM_NAME`,`reagent_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_case_relay_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_relay_info`;
CREATE TABLE `tb_judicial_case_relay_info` (
  `relay_id` varchar(50) NOT NULL COMMENT '交接ID',
  `relay_code` varchar(50) DEFAULT NULL COMMENT '交接编号',
  `relay_per` varchar(50) DEFAULT NULL COMMENT '交接人',
  `relay_time` datetime DEFAULT NULL COMMENT '交接时间',
  `relay_remark` varchar(500) DEFAULT NULL COMMENT '交接备注',
  `is_delete` int(11) DEFAULT '0',
  PRIMARY KEY (`relay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_case_sample
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_sample`;
CREATE TABLE `tb_judicial_case_sample` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'case_id唯一',
  `fandm` varchar(400) COLLATE utf8_bin DEFAULT NULL COMMENT '父母亲姓名',
  `child` varchar(400) COLLATE utf8_bin DEFAULT NULL COMMENT '孩子姓名',
  `id_card` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证合',
  `birth_date` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '出生日期合',
  `sample_count` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '样本数量',
  `case_area` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '案例身份证地址',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_case_second
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_second`;
CREATE TABLE `tb_judicial_case_second` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `case_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例编码',
  `case_code_second` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '补养或二次采样案例编码',
  `case_state` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '类型：5，二次采样，6，补样',
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例编码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_case_to_identify
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_case_to_identify`;
CREATE TABLE `tb_judicial_case_to_identify` (
  `case_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `identify_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `trans_date` varchar(64) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_casefee
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_casefee`;
CREATE TABLE `tb_judicial_casefee` (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'id',
  `case_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '案例id',
  `stand_sum` decimal(10,2) DEFAULT NULL COMMENT '标准收费',
  `real_sum` decimal(10,2) DEFAULT NULL COMMENT '实收金额',
  `return_sum` decimal(10,2) DEFAULT NULL COMMENT '回款金额',
  `discount` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '折扣',
  `status` int(2) DEFAULT '3' COMMENT '财务审核状态 0 正常 1 异常 2日报已结算 3登记状态',
  `update_date` datetime DEFAULT NULL COMMENT '生成日期',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `confirm_date` datetime DEFAULT NULL,
  `confirm_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `paragraphtime` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '到款时间',
  `account` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '账户类型',
  `remittanceName` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '汇款民称',
  `remittanceDate` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '汇款时间',
  `discountPrice` decimal(10,2) DEFAULT NULL COMMENT '优惠价格',
  `type` int(2) DEFAULT '0' COMMENT '0为正常1为先出报告后付款2为免单',
  `finance_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `case_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'dna',
  `dailyid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '日报或月报id',
  `finance_remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `remittance_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `case_id` (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_casefee_special
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_casefee_special`;
CREATE TABLE `tb_judicial_casefee_special` (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'id',
  `case_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '对应案例id',
  `confirm_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '激活码',
  `oa_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '对应oa编码',
  `discount_amount` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '优惠金额',
  `apply_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '申请时间',
  `apply_per` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '申请人',
  `estimate_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '先发报告预计缴费时间（月结时间缴费时间）',
  `monthly_per` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '月结人员',
  `monthly_area` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '月结地区',
  `confirm_per` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '激活人',
  `confirm_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '激活时间',
  `create_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `create_per` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `case_state` int(10) DEFAULT NULL COMMENT '案例状态：1为先出报告后付款，2为免单，3 优惠申请；4,月结申请；',
  `activation_state` int(10) DEFAULT '1' COMMENT '激活状态：1，待激活；2，已激活',
  `settlement_state` int(10) DEFAULT '1' COMMENT '当前结算状态：1，未结算；2，已结算，3：逾期未结算',
  `user_state` int(10) DEFAULT '1' COMMENT '使用状态：1，未使用；2，已使用；3，失效',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `delete` int(2) DEFAULT '0' COMMENT '删除标识',
  `delete_per` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `case_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'dna' COMMENT '项目类型',
  UNIQUE KEY `uniqe_code` (`confirm_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_casestatus
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_casestatus`;
CREATE TABLE `tb_judicial_casestatus` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例iD',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态 0为正常 1为异常',
  `statusmessage` varchar(20000) COLLATE utf8_bin NOT NULL COMMENT '状态信息',
  `date` date NOT NULL COMMENT '生成时间',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_checknegreport
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_checknegreport`;
CREATE TABLE `tb_judicial_checknegreport` (
  `case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sub_case_code` varchar(32) COLLATE utf8_bin NOT NULL,
  `check_flag` varchar(8) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`sub_case_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_checknegreport_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_checknegreport_bak`;
CREATE TABLE `tb_judicial_checknegreport_bak` (
  `case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sub_case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `check_flag` varchar(8) COLLATE utf8_bin DEFAULT NULL,
  `bak_time` varchar(32) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_compare_result
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_compare_result`;
CREATE TABLE `tb_judicial_compare_result` (
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '子案例编号',
  `parent1` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '父亲或母亲姓名',
  `parent2` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '父亲或母亲姓名',
  `child` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '孩子姓名',
  `sample_code1` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '比对样本编号1',
  `sample_code2` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '比对样本编号2',
  `sample_code3` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '比对样本编号3',
  `record_count` int(11) DEFAULT NULL,
  `ext_flag` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '是不是加位点',
  `unmatched_count` int(11) DEFAULT NULL COMMENT '不匹配点数量',
  `unmatched_node` text COLLATE utf8_bin COMMENT '不匹配点示例',
  `compare_date` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '比对日期',
  `final_result_flag` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '比对结果',
  `need_ext` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '是否需要做加位点',
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '实验室编号',
  `reagent_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '试剂名称',
  KEY `tb_judicial_compare_result_index1` (`case_code`),
  KEY `tb_judicial_compare_result_index2` (`compare_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_compare_result_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_compare_result_bak`;
CREATE TABLE `tb_judicial_compare_result_bak` (
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `parent1` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `parent2` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `child` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code1` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code2` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code3` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `record_count` int(11) DEFAULT NULL,
  `ext_flag` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `unmatched_count` int(11) DEFAULT NULL,
  `unmatched_node` longtext COLLATE utf8_bin,
  `compare_date` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `final_result_flag` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `bak_time` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `need_ext` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `reagent_name` varchar(128) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_compare_result_tongbao
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_compare_result_tongbao`;
CREATE TABLE `tb_judicial_compare_result_tongbao` (
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `case_code` varchar(32) CHARACTER SET utf8 DEFAULT NULL,
  `username_1` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `username_2` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code1` varchar(32) CHARACTER SET utf8 DEFAULT NULL,
  `sample_code2` varchar(32) CHARACTER SET utf8 DEFAULT NULL,
  `ibs_count` int(11) DEFAULT NULL,
  `unmatched_ystr_count` int(11) DEFAULT NULL,
  `unmatched_ystr_node` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `compare_date` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `final_result_flag` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `reagent_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  KEY `tb_judicial_compare_result_tongbao_index1` (`case_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_confirm_case
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_confirm_case`;
CREATE TABLE `tb_judicial_confirm_case` (
  `case_id` varchar(50) NOT NULL COMMENT '案例编号',
  `confirm_state` int(11) DEFAULT '0' COMMENT '案例状态 0.未确认，1已确认，2确认未通过',
  `relay_id` varchar(50) NOT NULL COMMENT '确认ID',
  PRIMARY KEY (`case_id`,`relay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_exception
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_exception`;
CREATE TABLE `tb_judicial_exception` (
  `uuid` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `exception_id` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code1` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code2` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `trans_date` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '异常产生时间',
  `state` int(11) DEFAULT NULL,
  `choose_flag` int(11) DEFAULT NULL COMMENT '1未处理0已经处理',
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '实验室编号',
  PRIMARY KEY (`uuid`),
  KEY `tb_judicial_exception_index1` (`case_code`),
  KEY `exception_id` (`exception_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_exception_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_exception_bak`;
CREATE TABLE `tb_judicial_exception_bak` (
  `uuid` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `exception_id` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code1` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code2` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `trans_date` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `choose_flag` int(11) DEFAULT NULL,
  `bak_time` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_exception_dic
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_exception_dic`;
CREATE TABLE `tb_judicial_exception_dic` (
  `exception_id` varchar(16) COLLATE utf8_bin NOT NULL,
  `exception_desc` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`exception_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_exception_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_exception_type`;
CREATE TABLE `tb_judicial_exception_type` (
  `type_id` varchar(50) NOT NULL,
  `type_desc` varchar(500) DEFAULT NULL,
  `is_delete` int(11) DEFAULT '0',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_experiment
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_experiment`;
CREATE TABLE `tb_judicial_experiment` (
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `experiment_date` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `experimenter` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `experiment_no` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `enable_flag` char(1) COLLATE utf8_bin DEFAULT NULL,
  `places` varchar(4096) COLLATE utf8_bin DEFAULT NULL,
  `laboratory_no` varchar(16) COLLATE utf8_bin NOT NULL,
  `reagent_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`experiment_no`,`laboratory_no`),
  KEY `FK_ID` (`reagent_name`,`laboratory_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_experiment_attachment
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_experiment_attachment`;
CREATE TABLE `tb_judicial_experiment_attachment` (
  `uuid` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `attachment_path` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '路径',
  `attachment_date` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '上传时间',
  `upload_userid` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '上传人员',
  `down_flag` int(2) DEFAULT '0' COMMENT '下载标识',
  `download_time` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `down_userid` varchar(50) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_experiment_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_experiment_bak`;
CREATE TABLE `tb_judicial_experiment_bak` (
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `experiment_date` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `experimenter` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `experiment_no` varchar(64) COLLATE utf8_bin NOT NULL,
  `enable_flag` char(1) COLLATE utf8_bin DEFAULT NULL,
  `places` varchar(4096) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`experiment_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_experiment_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_experiment_log`;
CREATE TABLE `tb_judicial_experiment_log` (
  `uuid` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `uploadPer` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `uploadTime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `downloadPer` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `downloadTime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sample_code` varchar(50) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_export_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_export_info`;
CREATE TABLE `tb_judicial_export_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案件编号',
  `case_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '条形码',
  `accept_time` date DEFAULT '1990-01-01' COMMENT '受理日期',
  `client` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '委托人',
  `sample_in_time` datetime DEFAULT NULL COMMENT '样本登记时间',
  `sample_in_per` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '采样人员',
  `phone` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '电话号码',
  `receiver_area` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '案例所属地区名称',
  `case_receiver` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `case_in_per` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `fandm` varchar(400) COLLATE utf8_bin DEFAULT NULL COMMENT '父母亲姓名',
  `child` varchar(400) COLLATE utf8_bin DEFAULT NULL COMMENT '孩子姓名',
  `id_card` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证合',
  `birth_date` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '出生日期合',
  `sample_count` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '样本数量',
  `return_sum` decimal(10,2) DEFAULT NULL COMMENT '回款金额',
  `real_sum` decimal(10,2) DEFAULT NULL COMMENT '实收金额',
  `paragraphtime` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '到款时间',
  `close_time` datetime DEFAULT NULL COMMENT '截止日期（即打印日期）',
  `type` int(2) DEFAULT '0' COMMENT '0为正常1为先出报告后付款2为免单',
  `account` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '账户类型',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '登记备注',
  `report_model` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '报告模板',
  `financeRemark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `financeStatus` int(2) DEFAULT '3' COMMENT '财务审核状态 0 正常 1 异常 2日报已结算 3登记状态',
  `remittanceDate` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '汇款时间',
  `case_type` varchar(50) COLLATE utf8_bin DEFAULT 'dna',
  `confirm_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '汇款确认时间',
  `parnter_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '合作商',
  `mail_info` text COLLATE utf8_bin,
  `update_date` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `agent` varchar(100) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_feeequation
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_feeequation`;
CREATE TABLE `tb_judicial_feeequation` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `type_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'tb_judicial_feetype id',
  `equation` varchar(2048) COLLATE utf8_bin NOT NULL COMMENT '人数',
  `discountrate` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '回款优惠比例',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_feepercase
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_feepercase`;
CREATE TABLE `tb_judicial_feepercase` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例ID',
  `case_fee` decimal(10,2) DEFAULT NULL COMMENT '此案例所缴纳的费用',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_feetype
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_feetype`;
CREATE TABLE `tb_judicial_feetype` (
  `type_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `area_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'tb_upc_area id',
  `feetype` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '1' COMMENT '1 亲子鉴定',
  `is_delete` int(2) NOT NULL DEFAULT '0' COMMENT '是否删除 0未删除 1删除',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_financedaily
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_financedaily`;
CREATE TABLE `tb_judicial_financedaily` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `userid` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '用户id',
  `sum` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '日报金额',
  `type` int(2) NOT NULL DEFAULT '1' COMMENT '日报类型 1,亲子鉴定 2合同计划,3无创产前',
  `daily_time` date NOT NULL COMMENT '日报生成时间',
  `status` int(2) DEFAULT '0' COMMENT '状态 0 为未汇款 1为已汇款 -1为已结算',
  `balancetype` int(2) NOT NULL DEFAULT '0' COMMENT '结算类型，0为日结算，1为月结算',
  `confirm_time` date DEFAULT NULL COMMENT '确认时间',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '月报审核备注',
  `discountsum` decimal(10,2) DEFAULT NULL COMMENT '月报扣除金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_financemonthly
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_financemonthly`;
CREATE TABLE `tb_judicial_financemonthly` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `userid` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '用户id',
  `sum` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '月报金额',
  `type` int(2) NOT NULL COMMENT '月报类型',
  `discountsum` decimal(10,2) DEFAULT '0.00' COMMENT '折扣金额',
  `monthly_time` date NOT NULL COMMENT '月报生成日期',
  `status` int(2) NOT NULL DEFAULT '2' COMMENT '状态 0为未汇款 1为已汇款 2为未审核状态 -1 为已结算',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_hand_report
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_hand_report`;
CREATE TABLE `tb_judicial_hand_report` (
  `report_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `sub_case_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `report_path` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `time` date DEFAULT NULL,
  `upload_per` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `is_delete` int(11) DEFAULT '0',
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_history_compare
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_history_compare`;
CREATE TABLE `tb_judicial_history_compare` (
  `sample_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `resultstr` text COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_identify_per
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_identify_per`;
CREATE TABLE `tb_judicial_identify_per` (
  `identify_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `identify_code` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `identify_per` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `laboratory_no` varchar(64) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_instrument_protocol
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_instrument_protocol`;
CREATE TABLE `tb_judicial_instrument_protocol` (
  `instrument_protocol` varchar(32) NOT NULL DEFAULT '',
  `enable_flag` varchar(16) DEFAULT NULL,
  `laboratory_no` varchar(16) NOT NULL DEFAULT '',
  PRIMARY KEY (`laboratory_no`,`instrument_protocol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_items
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_items`;
CREATE TABLE `tb_judicial_items` (
  `id` int(11) NOT NULL,
  `bgid` varchar(45) DEFAULT NULL,
  `itemid` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='委托事项';

-- ----------------------------
-- Table structure for tb_judicial_mail_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_mail_info`;
CREATE TABLE `tb_judicial_mail_info` (
  `mail_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `mail_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mail_type` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mail_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mail_info` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mail_time` datetime DEFAULT NULL,
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `is_delete` int(11) DEFAULT '0',
  `user_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `mail_address` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '收件地址',
  `mail_phone` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '收件联系方式',
  `case_type` varchar(10) COLLATE utf8_bin DEFAULT 'dna' COMMENT '案例类型',
  `mail_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`mail_id`),
  KEY `mail_info_index_1` (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_pi_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_pi_info`;
CREATE TABLE `tb_judicial_pi_info` (
  `sub_case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `param_type` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `parent` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `child` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `gene1` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `gene2` varchar(16) COLLATE utf8_bin DEFAULT '',
  `function` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `pi` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `parent2` varchar(32) COLLATE utf8_bin DEFAULT '',
  KEY `sub_case_index` (`sub_case_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_pi_info_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_pi_info_bak`;
CREATE TABLE `tb_judicial_pi_info_bak` (
  `sub_case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `param_type` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `parent` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `child` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `gene1` double DEFAULT NULL,
  `gene2` double DEFAULT NULL,
  `function` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `pi` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `bak_time` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `parent2` varchar(32) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_province_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_province_user`;
CREATE TABLE `tb_judicial_province_user` (
  `userid` varchar(50) COLLATE utf8_bin NOT NULL,
  `username` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `usercode` varchar(50) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_reagents
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_reagents`;
CREATE TABLE `tb_judicial_reagents` (
  `reagent_name` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '',
  `enable_flag` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `line_number` int(11) DEFAULT NULL,
  `laboratory_no` varchar(16) COLLATE utf8_bin NOT NULL DEFAULT '',
  `ext_flag` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `atelier` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`reagent_name`,`laboratory_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_receive_sample
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_receive_sample`;
CREATE TABLE `tb_judicial_receive_sample` (
  `sample_id` varchar(50) NOT NULL,
  `sample_code` varchar(50) DEFAULT NULL,
  `receive_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`sample_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_relay_sample
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_relay_sample`;
CREATE TABLE `tb_judicial_relay_sample` (
  `sample_id` varchar(50) NOT NULL COMMENT '样本ID',
  `confirm_state` int(11) DEFAULT '0' COMMENT '确认状态',
  `relay_id` varchar(50) NOT NULL COMMENT '样本交接单',
  PRIMARY KEY (`sample_id`,`relay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_remittance
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_remittance`;
CREATE TABLE `tb_judicial_remittance` (
  `remittance_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `remittance` double DEFAULT NULL COMMENT '汇款金额',
  `remittance_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '汇款时间',
  `remittance_account` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '汇款账户',
  `remittance_num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '汇款单号',
  `arrival_account` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '到款账户',
  `remittance_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '汇款人',
  `remittance_remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '汇款备注',
  `create_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `confirm_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '汇款确认时间',
  `confirm_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '汇款确认人',
  `confirm_remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '汇款确认备注',
  `daily_type` int(1) DEFAULT '1' COMMENT '1亲子鉴定 2 合同计划',
  `confirm_state` int(1) DEFAULT '-1' COMMENT '1：确认通过，2，确认不通过,-1:未确认',
  `urgent_state` int(1) DEFAULT '0' COMMENT '紧急程度',
  PRIMARY KEY (`remittance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_remittance_logs
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_remittance_logs`;
CREATE TABLE `tb_judicial_remittance_logs` (
  `remittance_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '汇款id',
  `confirm_state` varchar(10) COLLATE utf8_bin DEFAULT '-1' COMMENT '汇款状态',
  `confirm_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '操作人',
  `confirm_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '操作时间',
  `confirm_remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '确认备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_report
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_report`;
CREATE TABLE `tb_judicial_report` (
  `id` varchar(45) NOT NULL,
  `wtrlx` varchar(45) CHARACTER SET latin1 NOT NULL COMMENT '委托人类型',
  `wthh` varchar(45) NOT NULL COMMENT '委托函号',
  `wtsx` varchar(400) CHARACTER SET latin1 NOT NULL COMMENT '委托事项',
  `slrq` varchar(30) CHARACTER SET latin1 NOT NULL COMMENT '受理日期',
  `jdzl` varchar(400) CHARACTER SET latin1 NOT NULL COMMENT '鉴定资料',
  `jdksrq` date DEFAULT NULL COMMENT '鉴定开始时间',
  `jdjsrq` date DEFAULT NULL COMMENT '鉴定结束时间\n',
  `jddd` varchar(300) CHARACTER SET latin1 DEFAULT NULL COMMENT '鉴定地点',
  `slry` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '受理人员',
  `fpjds` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '分配鉴定师',
  `jdlx` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '鉴定报告类型',
  `jdmb` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '鉴定使用模版',
  `jjcd` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '紧急程度',
  `ajbh` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '案件编号',
  `djlsh` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '登记流水号',
  `bglsh` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '报告流水号',
  `cjsj` datetime DEFAULT NULL,
  `sqfs` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '报告收取方式',
  `zlry` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '助理',
  `dqzt` varchar(15) CHARACTER SET latin1 DEFAULT NULL COMMENT '当前状态',
  `jds1` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '实际鉴定师1',
  `jds2` varchar(45) CHARACTER SET latin1 DEFAULT NULL COMMENT '鉴定师2',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_report_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_report_type`;
CREATE TABLE `tb_judicial_report_type` (
  `typeid` varchar(45) NOT NULL,
  `typename` varchar(45) NOT NULL,
  `inputform` varchar(100) DEFAULT NULL COMMENT '录入表单',
  `displaygrid` varchar(100) DEFAULT NULL COMMENT '展示表格',
  `identify` varchar(45) DEFAULT NULL COMMENT '报告编号里面的‘法临’字样',
  `status` varchar(2) DEFAULT '0',
  `sort` varchar(4) DEFAULT NULL,
  `parentid` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`typeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='鉴定报告类型，临床、病理、酒精等';

-- ----------------------------
-- Table structure for tb_judicial_results_group
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_results_group`;
CREATE TABLE `tb_judicial_results_group` (
  `results_group` varchar(32) NOT NULL DEFAULT '',
  `enable_flag` varchar(16) DEFAULT NULL,
  `laboratory_no` varchar(16) NOT NULL DEFAULT '',
  PRIMARY KEY (`laboratory_no`,`results_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_returnfee
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_returnfee`;
CREATE TABLE `tb_judicial_returnfee` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `userid` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '回款人id',
  `sum` decimal(10,2) NOT NULL COMMENT '收款金额',
  `return_time` date NOT NULL COMMENT '回款金额',
  `bankaccount` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '回款银行账号',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_sample_code
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sample_code`;
CREATE TABLE `tb_judicial_sample_code` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `sample_code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '系统自动生成条码',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  UNIQUE KEY `sample_code` (`sample_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_sample_confirm_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sample_confirm_info`;
CREATE TABLE `tb_judicial_sample_confirm_info` (
  `confirm_id` varchar(50) NOT NULL,
  `confirm_time` datetime DEFAULT NULL,
  `confirm_per` varchar(50) DEFAULT NULL,
  `relay_id` varchar(50) DEFAULT NULL,
  `confirm_remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`confirm_id`),
  UNIQUE KEY `UQE_RE` (`relay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_sample_express
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sample_express`;
CREATE TABLE `tb_judicial_sample_express` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例编号',
  `express_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '快递类型',
  `express_num` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '快递编号',
  `express_time` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '快递时间',
  `express_recive` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '收件人',
  `express_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '快递备注',
  `update_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '更新时间',
  `express_concat` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '联系方式'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_sample_receive_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sample_receive_info`;
CREATE TABLE `tb_judicial_sample_receive_info` (
  `receive_id` varchar(50) NOT NULL,
  `receive_per` varchar(50) DEFAULT NULL,
  `receive_time` datetime DEFAULT NULL,
  `receive_remark` varchar(500) DEFAULT NULL,
  `is_delete` int(11) DEFAULT '0',
  PRIMARY KEY (`receive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_sample_relay_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sample_relay_info`;
CREATE TABLE `tb_judicial_sample_relay_info` (
  `relay_id` varchar(50) NOT NULL,
  `relay_code` varchar(50) DEFAULT NULL,
  `relay_per` varchar(50) DEFAULT NULL,
  `relay_time` datetime DEFAULT NULL,
  `relay_remark` varchar(500) DEFAULT NULL,
  `is_delete` int(11) DEFAULT '0',
  PRIMARY KEY (`relay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_judicial_sample_result
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sample_result`;
CREATE TABLE `tb_judicial_sample_result` (
  `experiment_no` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `sample_code` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `count` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `ext_flag` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `enable_flag` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `resultstr` text COLLATE utf8_bin,
  `md5` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`experiment_no`,`sample_code`),
  KEY `sample_result_index1` (`sample_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_sample_result_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sample_result_bak`;
CREATE TABLE `tb_judicial_sample_result_bak` (
  `experiment_no` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `sample_code` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `count` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `ext_flag` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `enable_flag` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `resultstr` longtext COLLATE utf8_bin,
  `md5` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `bak_time` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_sample_result_data_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sample_result_data_bak`;
CREATE TABLE `tb_judicial_sample_result_data_bak` (
  `experiment_no` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `trans_date` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `value` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `bak_time` varchar(32) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_sample_result_data_history
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sample_result_data_history`;
CREATE TABLE `tb_judicial_sample_result_data_history` (
  `experiment_no` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `sample_code` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `trans_date` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(16) COLLATE utf8_bin NOT NULL DEFAULT '',
  `value` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `filepath` varchar(4096) COLLATE utf8_bin DEFAULT NULL,
  `value2` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`experiment_no`,`sample_code`,`name`),
  KEY `tb_judicial_sample_result_data_history_2_index1` (`name`,`value`),
  KEY `tb_judicial_sample_result_data_history_2_index2` (`name`,`value2`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_sampleinfo_verify
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sampleinfo_verify`;
CREATE TABLE `tb_judicial_sampleinfo_verify` (
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `verify_sampleinfo_time` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `verify_sampleinfo_state` int(11) DEFAULT '0',
  `verify_sampleinfo_person` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `verify_sampleinfo_remark` text COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_serial_number
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_serial_number`;
CREATE TABLE `tb_judicial_serial_number` (
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例id',
  `case_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例条码',
  `serial_number` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例流水号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_sub_case_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sub_case_info`;
CREATE TABLE `tb_judicial_sub_case_info` (
  `case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sub_case_code` varchar(32) COLLATE utf8_bin NOT NULL,
  `sample_code1` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code2` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code3` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `result` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `up_case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `pi` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `rcp` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `parent1_pi` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `parent2_pi` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `con_pi` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`sub_case_code`),
  KEY `sub_case_info_index2` (`case_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_sub_case_info_bak
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_sub_case_info_bak`;
CREATE TABLE `tb_judicial_sub_case_info_bak` (
  `case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sub_case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code1` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code2` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `sample_code3` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `result` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `up_case_code` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `pi` double DEFAULT NULL,
  `rcp` double DEFAULT NULL,
  `bak_time` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `parent1_pi` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `parent2_pi` varchar(64) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_task_daily
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_task_daily`;
CREATE TABLE `tb_judicial_task_daily` (
  `daily_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `case_sum` int(10) NOT NULL DEFAULT '0' COMMENT '本日登记案例',
  `case_data_sum` int(10) NOT NULL DEFAULT '0' COMMENT '本日数据上传案例',
  `case_datareg_sum` int(10) NOT NULL DEFAULT '0' COMMENT '其中本日登记并上传的案例数',
  `case_print_sum` int(10) NOT NULL DEFAULT '0' COMMENT '本日打印案例',
  `case_printin2d_sum` int(10) NOT NULL DEFAULT '0' COMMENT '其中2日内登记并打印的案例',
  `case_exception_sum` int(10) NOT NULL DEFAULT '0' COMMENT '本日异常案例数',
  `case_comfirm_fee_sum` int(10) NOT NULL DEFAULT '0' COMMENT '本日确认回款案例数',
  `case_mail_sum` int(10) NOT NULL DEFAULT '0' COMMENT '本日快递案例',
  `case_mailin2d_sum` int(10) NOT NULL DEFAULT '0' COMMENT '其中2日内登记并快递的案例',
  `case_mailfromnow_sum` int(10) NOT NULL DEFAULT '0' COMMENT '截止本日已经快递的案例数',
  `case_nomail_sum` int(10) NOT NULL DEFAULT '0' COMMENT '截止本日已超期未快递的案例数',
  `case_exception2d_sum` int(10) NOT NULL DEFAULT '0' COMMENT '截止本日已超期异常的案例数',
  `case_exception2d_nodata_sum` int(10) NOT NULL DEFAULT '0' COMMENT '其中截止已超期数据未上传的案例数',
  `case_exception_nofee_sum` int(10) NOT NULL DEFAULT '0' COMMENT '其中截止已超期未回款的案例数',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `task_date` date NOT NULL COMMENT '日期',
  PRIMARY KEY (`daily_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_task_daily2
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_task_daily2`;
CREATE TABLE `tb_judicial_task_daily2` (
  `daily_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `case_sum` int(11) NOT NULL DEFAULT '0' COMMENT '案例数',
  `case_data_sum` int(11) NOT NULL DEFAULT '0' COMMENT '数据上传数',
  `case_print_sum` int(11) NOT NULL DEFAULT '0' COMMENT '案例打印数',
  `case_exception_sum` int(11) NOT NULL DEFAULT '0' COMMENT '异常案例数',
  `case_comfirm_fee_sum` int(11) NOT NULL DEFAULT '0' COMMENT '财务确认数',
  `case_mail_sum` int(11) NOT NULL DEFAULT '0' COMMENT '案例快递数',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `task_date` date NOT NULL COMMENT '日期',
  `case_accept` int(11) NOT NULL DEFAULT '0' COMMENT '受理案例数',
  `sample_receive` int(11) NOT NULL DEFAULT '0' COMMENT '样本受理数',
  `sample_relay` int(11) NOT NULL DEFAULT '0' COMMENT '样本交接数',
  `case_confirm` int(11) NOT NULL DEFAULT '0' COMMENT '案例送审数',
  `sample_verify` int(11) NOT NULL DEFAULT '0' COMMENT '样本审核数量',
  `data_today_register_yes` int(11) NOT NULL DEFAULT '0' COMMENT '昨日登记本日上传数',
  `print_today_data_yes` int(11) NOT NULL DEFAULT '0' COMMENT '昨日上传本日打印',
  `print_today_data_tod` int(11) NOT NULL DEFAULT '0' COMMENT '本日上传本日打印',
  `fee_today_register_yes` int(11) NOT NULL DEFAULT '0' COMMENT '本日登记昨日日付费',
  `fee_today_register_tod` int(11) NOT NULL DEFAULT '0' COMMENT '本日登记本日付费',
  `mail_today_data_yes` int(11) NOT NULL DEFAULT '0' COMMENT '本日邮寄昨日上传数',
  `mail_today_data_tod` int(11) NOT NULL DEFAULT '0' COMMENT '本日邮寄本日上传数',
  PRIMARY KEY (`daily_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_judicial_uploadfile
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_uploadfile`;
CREATE TABLE `tb_judicial_uploadfile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(200) NOT NULL,
  `filepath` varchar(400) NOT NULL,
  `uptime` varchar(45) DEFAULT NULL,
  `status` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tb_judicial_vacances
-- ----------------------------
DROP TABLE IF EXISTS `tb_judicial_vacances`;
CREATE TABLE `tb_judicial_vacances` (
  `holiday` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_mail_case_link
-- ----------------------------
DROP TABLE IF EXISTS `tb_mail_case_link`;
CREATE TABLE `tb_mail_case_link` (
  `mail_id` varchar(50) NOT NULL,
  `case_id` varchar(50) NOT NULL,
  `case_type` varchar(50) NOT NULL,
  PRIMARY KEY (`mail_id`,`case_id`,`case_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_mail_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_mail_info`;
CREATE TABLE `tb_mail_info` (
  `mail_id` varchar(50) NOT NULL,
  `mail_code` varchar(50) DEFAULT NULL,
  `mail_per` varchar(50) DEFAULT NULL,
  `mail_type` varchar(50) DEFAULT NULL,
  `mail_time` datetime DEFAULT NULL,
  `addressee` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`mail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_medical_examination
-- ----------------------------
DROP TABLE IF EXISTS `tb_medical_examination`;
CREATE TABLE `tb_medical_examination` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `num` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '案例编号',
  `barcode` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '物流条码',
  `date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '日期',
  `program` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人id',
  `remark` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '登记时间',
  `diagnosis` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '临床诊断',
  `age` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '年龄',
  `sex` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `sampletype` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '样本类型',
  `samplecount` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '样本数量',
  `hospital` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '送检医院'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_medical_examination_program
-- ----------------------------
DROP TABLE IF EXISTS `tb_medical_examination_program`;
CREATE TABLE `tb_medical_examination_program` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `program_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '医学检验项目名称',
  `program_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '医学检验项目名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_medical_examination_wuhan
-- ----------------------------
DROP TABLE IF EXISTS `tb_medical_examination_wuhan`;
CREATE TABLE `tb_medical_examination_wuhan` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `num` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '案例编号',
  `date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '日期',
  `program` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人id',
  `remark` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '登记时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_medical_examination_wuhan_program
-- ----------------------------
DROP TABLE IF EXISTS `tb_medical_examination_wuhan_program`;
CREATE TABLE `tb_medical_examination_wuhan_program` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `program_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '医学检验项目名称',
  `program_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '医学检验项目名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_medical_screening
-- ----------------------------
DROP TABLE IF EXISTS `tb_medical_screening`;
CREATE TABLE `tb_medical_screening` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `num` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '案例编号',
  `date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '日期',
  `program` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目',
  `inspection` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '送检人',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人id',
  `remark` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '登记时间',
  PRIMARY KEY (`num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_medical_screening_program
-- ----------------------------
DROP TABLE IF EXISTS `tb_medical_screening_program`;
CREATE TABLE `tb_medical_screening_program` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `program_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '转化医学检验项目名称',
  `program_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '转化医学检验项目名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_medical_thalassemia
-- ----------------------------
DROP TABLE IF EXISTS `tb_medical_thalassemia`;
CREATE TABLE `tb_medical_thalassemia` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `num` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '案例编号',
  `code` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '案例编号',
  `date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '日期',
  `program` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目',
  `inspection` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '送检人',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人id',
  `remark` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '登记时间',
  PRIMARY KEY (`num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_medical_thalassemia_program
-- ----------------------------
DROP TABLE IF EXISTS `tb_medical_thalassemia_program`;
CREATE TABLE `tb_medical_thalassemia_program` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `program_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '地中海贫血项目名称',
  `program_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '地中海贫血项目名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_medical_translational
-- ----------------------------
DROP TABLE IF EXISTS `tb_medical_translational`;
CREATE TABLE `tb_medical_translational` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `num` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '案例编号',
  `date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '日期',
  `program` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '项目',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人id',
  `remark` varchar(300) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废：1，是，2，否',
  `create_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '登记时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_medical_translational_program
-- ----------------------------
DROP TABLE IF EXISTS `tb_medical_translational_program`;
CREATE TABLE `tb_medical_translational_program` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `program_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '转化医学检验项目名称',
  `program_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '转化医学检验项目名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_narcotics_case_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_narcotics_case_info`;
CREATE TABLE `tb_narcotics_case_info` (
  `case_id` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '',
  `case_num` int(50) DEFAULT NULL COMMENT '鉴定号',
  `client` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '委托人',
  `client_time` date DEFAULT NULL COMMENT '受理日期',
  `person_sex` int(10) DEFAULT NULL COMMENT '被鉴定人性别[''男'', 0], [''女'', 1]',
  `person_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '被鉴定人',
  `person_card` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '被鉴定人身份证',
  `identification_materials` int(10) DEFAULT NULL COMMENT '鉴定材料 [''血液'', 0],[''尿液'', 1], [''头发'', 2],[''腋毛'', 3],[''阴毛'', 4]',
  `identification_start` date DEFAULT NULL COMMENT '鉴定时间',
  `identification_end` date DEFAULT NULL COMMENT '鉴定时间',
  `identification_site` int(10) DEFAULT NULL COMMENT '鉴定地点[''江苏苏博检测技术有限公司司法鉴定所'', 0]',
  `case_card` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '样品编号',
  `case_basic` varchar(400) COLLATE utf8_bin DEFAULT NULL COMMENT '基本案情',
  `instrument_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '仪器型号',
  `entrusted_matters` int(10) DEFAULT NULL COMMENT '委托事项[''苯丙胺类成分分析'', 0], [''阿片类成分分析'', 1],[''乙醇定性定量分析'',2]',
  `materials_totals` int(50) DEFAULT NULL COMMENT '材料总长',
  `materials_color` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '材料颜色',
  `materials_length` int(50) DEFAULT NULL COMMENT '材料距根部长度',
  `materials_weight` int(50) DEFAULT NULL COMMENT '材料重量',
  `partial_weight` int(50) DEFAULT NULL COMMENT '取样重量',
  `liquid_color` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '液体颜色',
  `ifleakage` int(50) DEFAULT NULL COMMENT '有无泄漏[''无'', 0], [''有'', 1]',
  `report_time` date DEFAULT NULL COMMENT '报告时间',
  `appraisal_opinion` varchar(400) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定意见',
  `case_in_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '登记人',
  `case_in_time` datetime DEFAULT NULL COMMENT '登记时间',
  `state` int(10) DEFAULT '1' COMMENT '0删除  1存在',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_narcotics_caseinfo_identify
-- ----------------------------
DROP TABLE IF EXISTS `tb_narcotics_caseinfo_identify`;
CREATE TABLE `tb_narcotics_caseinfo_identify` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `cid` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `pid` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `create_sort` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_narcotics_identify_per
-- ----------------------------
DROP TABLE IF EXISTS `tb_narcotics_identify_per`;
CREATE TABLE `tb_narcotics_identify_per` (
  `per_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `per_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `per_code` varchar(50) COLLATE utf8_bin NOT NULL,
  `per_remark` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `per_sys` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `user_id` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `delstatus` int(100) DEFAULT '0',
  `user_name` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`per_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_partner_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_partner_config`;
CREATE TABLE `tb_partner_config` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `parnter_name` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '合作商名称',
  `areacode` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '地区编码',
  `create_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `qualificationFee` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资质费用',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `delete` int(2) DEFAULT '1' COMMENT '删除标识默认1未删除',
  `laboratory_no` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '合作方实验室编号',
  `report_model` varchar(100) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_partner_model_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_partner_model_config`;
CREATE TABLE `tb_partner_model_config` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `userid` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '人员id',
  `code` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '模版类型',
  `create_date` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_partner_user_model
-- ----------------------------
DROP TABLE IF EXISTS `tb_partner_user_model`;
CREATE TABLE `tb_partner_user_model` (
  `id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `userid` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `code` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '模版',
  `create_date` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_physical_examination
-- ----------------------------
DROP TABLE IF EXISTS `tb_physical_examination`;
CREATE TABLE `tb_physical_examination` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例编号',
  `date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '日期',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `sex` varchar(2) COLLATE utf8_bin DEFAULT '男' COMMENT '1:男，2：女',
  `model_type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '套餐类型',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `cancelif` varchar(1) COLLATE utf8_bin DEFAULT '2' COMMENT '1:是，2：否',
  `create_date` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '创建日期'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_safe_medication
-- ----------------------------
DROP TABLE IF EXISTS `tb_safe_medication`;
CREATE TABLE `tb_safe_medication` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '编号',
  `date` date DEFAULT NULL COMMENT '日期',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `testitems` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '检测项目',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人id',
  `agent` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '代理商id',
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '备注要求',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废1，是，2，否',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `type` varchar(10) COLLATE utf8_bin DEFAULT 'audlt' COMMENT 'audlt:成人，child：儿童'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_sample_statistics
-- ----------------------------
DROP TABLE IF EXISTS `tb_sample_statistics`;
CREATE TABLE `tb_sample_statistics` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `sample_in_per_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sample_in_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `countSample` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `month` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `deptname` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `deptcode` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `money` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '财务'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_sample_statistics_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_sample_statistics_info`;
CREATE TABLE `tb_sample_statistics_info` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `case_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例编号',
  `accept_time` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '受理时间',
  `client` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '委托人',
  `sample_username` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '样本姓名',
  `sample_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '样本编号',
  `sample_in_per` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '采样人员',
  `sample_in_per_id` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '采样人员id',
  `month` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '月份',
  `deptname` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '采样人员部门',
  `deptcode` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '部门编号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_statistics_finance
-- ----------------------------
DROP TABLE IF EXISTS `tb_statistics_finance`;
CREATE TABLE `tb_statistics_finance` (
  `id` varchar(50) NOT NULL,
  `case_type` varchar(50) DEFAULT NULL COMMENT '案例类型',
  `receivables` varchar(50) DEFAULT NULL COMMENT '应收款项',
  `payment` varchar(50) DEFAULT NULL COMMENT '所付款项',
  `date` date DEFAULT NULL COMMENT '到款时间',
  `ownperson` varchar(50) DEFAULT NULL COMMENT '归属人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ynit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_tj_day
-- ----------------------------
DROP TABLE IF EXISTS `tb_tj_day`;
CREATE TABLE `tb_tj_day` (
  `datetime` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `t_reg` int(11) DEFAULT NULL,
  `t_print` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_trace_archive_read
-- ----------------------------
DROP TABLE IF EXISTS `tb_trace_archive_read`;
CREATE TABLE `tb_trace_archive_read` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `archive_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `read_per` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `read_date` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_trace_baseinfo_verify
-- ----------------------------
DROP TABLE IF EXISTS `tb_trace_baseinfo_verify`;
CREATE TABLE `tb_trace_baseinfo_verify` (
  `uuid` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `case_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `verify_baseinfo_time` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `verify_baseinfo_state` int(11) DEFAULT NULL,
  `verify_baseinfo_person` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `verify_baseinfo_remark` varchar(2048) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_trace_case_archive
-- ----------------------------
DROP TABLE IF EXISTS `tb_trace_case_archive`;
CREATE TABLE `tb_trace_case_archive` (
  `archive_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `archive_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `archive_address` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `archive_date` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `archive_per` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `archive_path` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`archive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_trace_case_attachment
-- ----------------------------
DROP TABLE IF EXISTS `tb_trace_case_attachment`;
CREATE TABLE `tb_trace_case_attachment` (
  `uuid` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `case_id` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `attachment_path` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `attachment_date` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `attachment_type` int(11) DEFAULT '0',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_trace_case_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_trace_case_info`;
CREATE TABLE `tb_trace_case_info` (
  `case_id` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `case_no` int(11) DEFAULT NULL,
  `year` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `month` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `day` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `company_name` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '委托单位',
  `case_type` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '委托鉴定事项',
  `case_attachment_desc` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定材料',
  `case_local` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定地点',
  `identification_requirements` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定要求',
  `receive_time` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '受理时间',
  `identification_date` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '鉴定日期',
  `status` int(11) DEFAULT NULL COMMENT '状态 0未上传报告 1已上传报告 2审核报告通过 3审核报告不通过',
  `is_delete` int(11) DEFAULT NULL,
  `receiver_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `area_code` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '归属人',
  `remark` varchar(2048) COLLATE utf8_bin DEFAULT NULL,
  `clients` varchar(2048) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`case_id`),
  UNIQUE KEY `tb_trace_case_info_index1` (`case_no`,`year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_trace_document_dic
-- ----------------------------
DROP TABLE IF EXISTS `tb_trace_document_dic`;
CREATE TABLE `tb_trace_document_dic` (
  `document_id` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `document_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create_time` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_trace_mail_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_trace_mail_info`;
CREATE TABLE `tb_trace_mail_info` (
  `mail_id` varchar(50) COLLATE utf8_bin NOT NULL,
  `mail_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mail_time` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mail_type` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mail_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `case_id` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `is_delete` int(11) DEFAULT '0',
  PRIMARY KEY (`mail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_trace_person_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_trace_person_info`;
CREATE TABLE `tb_trace_person_info` (
  `uuid` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `case_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `person_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `id_number` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `address` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_trace_type_dic
-- ----------------------------
DROP TABLE IF EXISTS `tb_trace_type_dic`;
CREATE TABLE `tb_trace_type_dic` (
  `type_id` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `type_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `create_time` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `create_per` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_trace_vehicle
-- ----------------------------
DROP TABLE IF EXISTS `tb_trace_vehicle`;
CREATE TABLE `tb_trace_vehicle` (
  `uuid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `case_id` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `plate_number` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `vehicle_identification_number` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `engine_number` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `vehicle_type` varchar(64) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_tumor_individual
-- ----------------------------
DROP TABLE IF EXISTS `tb_tumor_individual`;
CREATE TABLE `tb_tumor_individual` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案列编号',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `age` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '年龄',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '所属人',
  `diagnosis` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '病理诊断',
  `testitems` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '检测项目',
  `checkper` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '送检人',
  `phonenum` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否废除1是2否',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `date` date DEFAULT NULL COMMENT '日期',
  `hospital` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '送检医院',
  `agent` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '代理商id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_tumor_marker
-- ----------------------------
DROP TABLE IF EXISTS `tb_tumor_marker`;
CREATE TABLE `tb_tumor_marker` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id编号',
  `num` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '案例编号',
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `sex` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `date` varchar(50) COLLATE utf8_bin NOT NULL,
  `program` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '检测项目',
  `inspection` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '送检人',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `cancelif` varchar(10) COLLATE utf8_bin DEFAULT '2' COMMENT '是否作废，1，作废，2默认正常',
  `reportif` varchar(10) COLLATE utf8_bin DEFAULT '2' COMMENT '是否发送报告，1是，2否',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案例归属人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for tb_tumor_susceptibility
-- ----------------------------
DROP TABLE IF EXISTS `tb_tumor_susceptibility`;
CREATE TABLE `tb_tumor_susceptibility` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT 'id',
  `num` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '案列编号',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `age` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '年龄',
  `ownperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '所属人',
  `testitems` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '检测项目',
  `checkper` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '送检人',
  `cancelif` varchar(2) COLLATE utf8_bin DEFAULT '2' COMMENT '是否废除1是2否',
  `remark` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `inputperson` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '录入人',
  `date` date DEFAULT NULL COMMENT '日期',
  `phonenum` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  `gender` varchar(10) COLLATE utf8_bin DEFAULT '男' COMMENT '性别',
  `agent` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '代理商'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
